package com.marcioaraki.coupon_api.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CouponJpaRepository extends JpaRepository<CouponJpaEntity, UUID> {
}
