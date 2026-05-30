package com.marcioaraki.coupon_api.domain.exception;

public class CouponAlreadyDeletedException extends RuntimeException {
    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
