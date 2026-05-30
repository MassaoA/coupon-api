package com.marcioaraki.coupon_api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marcioaraki.coupon_api.api.dto.CreateCouponRequest;
import com.marcioaraki.coupon_api.infrastructure.persistance.CouponJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CouponIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @BeforeEach
    void setUp() {
        couponJpaRepository.deleteAll();
    }

    @Test
    void shouldCreateAndRetrieveCoupon() throws Exception {
        var request = buildRequest("ABC123", false);

        var response = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andReturn();

        var body = objectMapper.readTree(response.getResponse().getContentAsString());
        var id = body.get("id").asText();

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.code").value("ABC123"));
    }

    @Test
    void shouldSanitizeCodeAndPersist() throws Exception {
        var request = buildRequest("AB-C1@23", false);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"));
    }

    @Test
    void shouldCreatePublishedCouponWithActiveStatus() throws Exception {
        var request = buildRequest("ABC123", true);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    void shouldSoftDeleteCouponAndKeepInDatabase() throws Exception {
        var request = buildRequest("ABC123", false);

        var response = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        var body = objectMapper.readTree(response.getResponse().getContentAsString());
        var id = UUID.fromString(body.get("id").asText());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        // soft delete — registro ainda existe no banco
        assertThat(couponJpaRepository.findById(id)).isPresent();
        assertThat(couponJpaRepository.findById(id).get().getStatus().name()).isEqualTo("DELETED");
    }

    @Test
    void shouldReturnBadRequestWhenDeletingAlreadyDeletedCoupon() throws Exception {
        var request = buildRequest("ABC123", false);

        var response = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        var body = objectMapper.readTree(response.getResponse().getContentAsString());
        var id = body.get("id").asText();

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenCouponDoesNotExist() throws Exception {
        mockMvc.perform(get("/coupon/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenExpirationDateIsInThePast() throws Exception {
        var request = new CreateCouponRequest("ABC123", "Desconto", new BigDecimal("1.0"),
                LocalDateTime.now().minusDays(1), false);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private CreateCouponRequest buildRequest(String code, boolean published) {
        return new CreateCouponRequest(code, "Desconto", new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(10), published);
    }
}
