package com.marcioaraki.coupon_api.domain.usecase;

import com.marcioaraki.coupon_api.domain.entity.Coupon;

import java.util.UUID;

public interface GetCouponUseCase {
    Coupon execute(UUID id);
}
