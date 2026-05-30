package com.marcioaraki.coupon_api.domain.entity;

import com.marcioaraki.coupon_api.domain.exception.CouponExpiredException;
import com.marcioaraki.coupon_api.domain.exception.InvalidCouponCodeException;
import com.marcioaraki.coupon_api.domain.exception.InvalidDiscountValueException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Coupon {

    private static final int CODE_LENGTH = 6;
    private static final BigDecimal MINIMUM_DISCOUNT = new BigDecimal("0.5");

    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDateTime expirationDate;
    private boolean published;
    private boolean redeemed;
    private CouponStatus status;

    // Construtor de criação — valida e sanitiza
    public Coupon(String code, String description, BigDecimal discountValue,
                  LocalDateTime expirationDate, boolean published) {
        this.id = UUID.randomUUID();
        this.code = sanitizeCode(code);
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
        this.redeemed = false;
        this.status = published ? CouponStatus.ACTIVE : CouponStatus.INACTIVE;

        validate();
    }

    public Coupon(UUID id, String code, String description, BigDecimal discountValue,
                  LocalDateTime expirationDate, boolean published, boolean redeemed, CouponStatus status) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.published = published;
        this.redeemed = redeemed;
        this.status = status;
    }

    private String sanitizeCode(String rawCode) {
        return rawCode.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
    }

    private void validate() {
        if (code == null || code.length() != CODE_LENGTH) {
            throw new InvalidCouponCodeException(
                "O código do cupom deve ter exatamente 6 caracteres alfanuméricos. Código gerado: " + code
            );
        }
        if (discountValue == null || discountValue.compareTo(MINIMUM_DISCOUNT) < 0) {
            throw new InvalidDiscountValueException(
                "O valor de desconto deve ser no mínimo " + MINIMUM_DISCOUNT
            );
        }
        if (expirationDate == null || expirationDate.isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException(
                "A data de expiração não pode estar no passado."
            );
        }
    }

    public void delete() {
        this.status = CouponStatus.DELETED;
    }

    public boolean isDeleted() {
        return CouponStatus.DELETED.equals(this.status);
    }

}
