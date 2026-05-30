package com.marcioaraki.coupon_api.application.service;

import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.entity.CouponStatus;
import com.marcioaraki.coupon_api.domain.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CreateCouponService createCouponService;

    @Test
    void shouldSaveAndReturnCreatedCoupon() {
        var coupon = buildCoupon();
        when(couponRepository.save(coupon)).thenReturn(coupon);

        var result = createCouponService.execute(coupon);

        assertThat(result).isEqualTo(coupon);
        verify(couponRepository, times(1)).save(coupon);
    }

    private Coupon buildCoupon() {
        return new Coupon(
                UUID.randomUUID(),
                "ABC123",
                "Desconto",
                new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(10),
                false,
                false,
                CouponStatus.INACTIVE
        );
    }
}
