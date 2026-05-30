package com.marcioaraki.coupon_api.application.service;

import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.entity.CouponStatus;
import com.marcioaraki.coupon_api.domain.exception.CouponAlreadyDeletedException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private DeleteCouponService deleteCouponService;

    @Test
    void shouldDeleteCouponSuccessfully() {
        var id = UUID.randomUUID();
        var coupon = buildCoupon(id, CouponStatus.ACTIVE);
        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        deleteCouponService.execute(id);

        verify(couponRepository).save(coupon);
        assertThat(coupon.isDeleted()).isTrue();
    }

    @Test
    void shouldThrowExceptionWhenCouponNotFound() {
        var id = UUID.randomUUID();
        when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteCouponService.execute(id))
                .isInstanceOf(CouponNotFoundException.class);

        verify(couponRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCouponAlreadyDeleted() {
        var id = UUID.randomUUID();
        var coupon = buildCoupon(id, CouponStatus.DELETED);
        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        assertThatThrownBy(() -> deleteCouponService.execute(id))
                .isInstanceOf(CouponAlreadyDeletedException.class);

        verify(couponRepository, never()).save(any());
    }

    private Coupon buildCoupon(UUID id, CouponStatus status) {
        return new Coupon(
                id,
                "ABC123",
                "Desconto",
                new BigDecimal("1.0"),
                LocalDateTime.now().plusDays(10),
                true,
                false,
                status
        );
    }
}
