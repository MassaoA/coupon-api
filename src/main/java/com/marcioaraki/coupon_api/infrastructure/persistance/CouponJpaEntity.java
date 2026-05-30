package com.marcioaraki.coupon_api.infrastructure.persistance;

import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.entity.CouponStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    public static CouponJpaEntity from(Coupon coupon) {
        return CouponJpaEntity.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .description(coupon.getDescription())
                .discountValue(coupon.getDiscountValue())
                .expirationDate(coupon.getExpirationDate())
                .published(coupon.isPublished())
                .redeemed(coupon.isRedeemed())
                .status(coupon.getStatus())
                .build();
    }

    public Coupon toDomain() {
        return new Coupon(
                this.id,
                this.code,
                this.description,
                this.discountValue,
                this.expirationDate,
                this.published,
                this.redeemed,
                this.status
        );
    }
}
