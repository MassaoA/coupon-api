package com.marcioaraki.coupon_api.domain.exception;

public class InvalidDiscountValueException extends RuntimeException {
    public InvalidDiscountValueException(String message) {
        super(message);
    }
}
