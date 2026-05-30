package com.marcioaraki.coupon_api.application.service;

import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.entity.CouponStatus;
import com.marcioaraki.coupon_api.domain.exception.CouponNotFoundException;
import com.marcioaraki.coupon_api.domain.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private GetCouponService getCouponService;

    @Test
    void shouldReturnCouponWhenFound() {
        var id = UUID.randomUUID();
        var coupon = buildCoupon(id);
        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        var result = getCouponService.execute(id);

        assertThat(result).isEqualTo(coupon);
    }

    @Test
    void shouldThrowExceptionWhenCouponNotFound() {
        var id = UUID.randomUUID();
        when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getCouponService.execute(id))
                .isInstanceOf(CouponNotFoundException.class);
    }

    private Coupon buildCoupon(UUID id) {
        return new Coupon(
                id,
                "ABC123",
                "Desconto",
                new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(10),
                true,
                false,
                CouponStatus.ACTIVE
        );
    }
}
