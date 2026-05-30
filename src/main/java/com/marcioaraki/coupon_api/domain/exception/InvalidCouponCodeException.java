package com.marcioaraki.coupon_api.domain.exception;

public class InvalidCouponCodeException extends RuntimeException {
    public InvalidCouponCodeException(String message) {
        super(message);
    }
}
