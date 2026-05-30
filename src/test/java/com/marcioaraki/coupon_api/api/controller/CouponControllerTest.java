package com.marcioaraki.coupon_api.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marcioaraki.coupon_api.api.dto.CreateCouponRequest;
import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.entity.CouponStatus;
import com.marcioaraki.coupon_api.domain.exception.CouponAlreadyDeletedException;
import com.marcioaraki.coupon_api.domain.exception.CouponNotFoundException;
import com.marcioaraki.coupon_api.domain.usecase.CreateCouponUseCase;
import com.marcioaraki.coupon_api.domain.usecase.DeleteCouponUseCase;
import com.marcioaraki.coupon_api.domain.usecase.GetCouponUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private CreateCouponUseCase createCouponUseCase;

    @MockitoBean
    private DeleteCouponUseCase deleteCouponUseCase;

    @MockitoBean
    private GetCouponUseCase getCouponUseCase;

    @Test
    void shouldReturnCreatedWhenCouponIsValid() throws Exception {
        var coupon = buildCoupon(CouponStatus.INACTIVE);
        when(createCouponUseCase.execute(any())).thenReturn(coupon);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.redeemed").value(false));
    }

    @Test
    void shouldReturnBadRequestWhenCodeIsMissing() throws Exception {
        var request = new CreateCouponRequest(null, "Desconto", new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(10), false);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDescriptionIsMissing() throws Exception {
        var request = new CreateCouponRequest("ABC123", null, new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(10), false);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDiscountIsBelowMinimum() throws Exception {
        var request = new CreateCouponRequest("ABC123", "Desconto", new BigDecimal("0.4"),
                LocalDateTime.now().plusDays(10), false);

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
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

    @Test
    void shouldReturnOkWhenCouponFound() throws Exception {
        var id = UUID.randomUUID();
        var coupon = buildCoupon(id, CouponStatus.ACTIVE);
        when(getCouponUseCase.execute(id)).thenReturn(coupon);

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.code").value("ABC123"));
    }

    @Test
    void shouldReturnNotFoundWhenCouponDoesNotExist() throws Exception {
        var id = UUID.randomUUID();
        when(getCouponUseCase.execute(id)).thenThrow(new CouponNotFoundException("Cupom não encontrado"));

        mockMvc.perform(get("/coupon/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNoContentWhenCouponDeleted() throws Exception {
        var id = UUID.randomUUID();
        doNothing().when(deleteCouponUseCase).execute(id);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnBadRequestWhenCouponAlreadyDeleted() throws Exception {
        var id = UUID.randomUUID();
        doThrow(new CouponAlreadyDeletedException("Cupom já deletado"))
                .when(deleteCouponUseCase).execute(id);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isBadRequest());
    }

    private CreateCouponRequest buildRequest() {
        return new CreateCouponRequest("ABC123", "Desconto", new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(10), false);
    }

    private Coupon buildCoupon(CouponStatus status) {
        return buildCoupon(UUID.randomUUID(), status);
    }

    private Coupon buildCoupon(UUID id, CouponStatus status) {
        return new Coupon(id, "ABC123", "Desconto", new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(10), false, false, status);
    }
}
