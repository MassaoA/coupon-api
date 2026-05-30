package com.marcioaraki.coupon_api.domain.entity;

import com.marcioaraki.coupon_api.domain.exception.CouponExpiredException;
import com.marcioaraki.coupon_api.domain.exception.InvalidCouponCodeException;
import com.marcioaraki.coupon_api.domain.exception.InvalidDiscountValueException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class CouponTest {

    private static final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(10);

    @Test
    void shouldCreateValidCouponSuccessfully() {
        var coupon = new Coupon("ABC123", "Desconto", new BigDecimal("1.0"), FUTURE_DATE, false);

        assertThat(coupon.getCode()).isEqualTo("ABC123");
        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.INACTIVE);
        assertThat(coupon.isRedeemed()).isFalse();
        assertThat(coupon.getId()).isNotNull();
    }

    @Test
    void shouldRemoveSpecialCharactersFromCode() {
        var coupon = new Coupon("AB-C1@23", "Desconto", new BigDecimal("1.0"), FUTURE_DATE, false);

        assertThat(coupon.getCode()).isEqualTo("ABC123");
    }

    @Test
    void shouldThrowExceptionWhenCodeHasLessThan6CharsAfterSanitization() {
        assertThatThrownBy(() ->
                new Coupon("AB-C", "Desconto", new BigDecimal("1.0"), FUTURE_DATE, false))
                .isInstanceOf(InvalidCouponCodeException.class);
    }

    @Test
    void shouldThrowExceptionWhenCodeHasMoreThan6CharsAfterSanitization() {
        assertThatThrownBy(() ->
                new Coupon("ABCDEFG", "Desconto", new BigDecimal("1.0"), FUTURE_DATE, false))
                .isInstanceOf(InvalidCouponCodeException.class);
    }

    @Test
    void shouldThrowExceptionWhenDiscountIsBelowMinimum() {
        assertThatThrownBy(() ->
                new Coupon("ABC123", "Desconto", new BigDecimal("0.4"), FUTURE_DATE, false))
                .isInstanceOf(InvalidDiscountValueException.class);
    }

    @Test
    void shouldAcceptDiscountExactlyAtMinimum() {
        var coupon = new Coupon("ABC123", "Desconto", new BigDecimal("0.5"), FUTURE_DATE, false);

        assertThat(coupon.getDiscountValue()).isEqualByComparingTo("0.5");
    }

    @Test
    void shouldThrowExceptionWhenExpirationDateIsInThePast() {
        var pastDate = LocalDateTime.now().minusDays(1);

        assertThatThrownBy(() ->
                new Coupon("ABC123", "Desconto", new BigDecimal("1.0"), pastDate, false))
                .isInstanceOf(CouponExpiredException.class);
    }

    @Test
    void shouldCreatePublishedCouponWithActiveStatus() {
        var coupon = new Coupon("ABC123", "Desconto", new BigDecimal("1.0"), FUTURE_DATE, true);

        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.ACTIVE);
        assertThat(coupon.isPublished()).isTrue();
    }

    @Test
    void shouldCreateUnpublishedCouponWithInactiveStatus() {
        var coupon = new Coupon("ABC123", "Desconto", new BigDecimal("1.0"), FUTURE_DATE, false);

        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.INACTIVE);
        assertThat(coupon.isPublished()).isFalse();
    }

    @Test
    void shouldSetStatusToDeletedWhenDeleted() {
        var coupon = new Coupon("ABC123", "Desconto", new BigDecimal("1.0"), FUTURE_DATE, true);

        coupon.delete();

        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.DELETED);
        assertThat(coupon.isDeleted()).isTrue();
    }

    @Test
    void shouldConvertCodeToUpperCase() {
        var coupon = new Coupon("abc123", "Desconto", new BigDecimal("1.0"), FUTURE_DATE, false);

        assertThat(coupon.getCode()).isEqualTo("ABC123");
    }
}
