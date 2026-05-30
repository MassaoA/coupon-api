package com.marcioaraki.coupon_api.api.exception;

import com.marcioaraki.coupon_api.domain.exception.CouponAlreadyDeletedException;
import com.marcioaraki.coupon_api.domain.exception.CouponExpiredException;
import com.marcioaraki.coupon_api.domain.exception.CouponNotFoundException;
import com.marcioaraki.coupon_api.domain.exception.InvalidCouponCodeException;
import com.marcioaraki.coupon_api.domain.exception.InvalidDiscountValueException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return Map.of("status", 400, "errors", errors);
    }

    @ExceptionHandler({
            InvalidCouponCodeException.class,
            InvalidDiscountValueException.class,
            CouponExpiredException.class,
            CouponAlreadyDeletedException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleDomainErrors(RuntimeException ex) {
        return Map.of("status", 400, "error", ex.getMessage());
    }

    @ExceptionHandler(CouponNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(CouponNotFoundException ex) {
        return Map.of("status", 404, "error", ex.getMessage());
    }
}
