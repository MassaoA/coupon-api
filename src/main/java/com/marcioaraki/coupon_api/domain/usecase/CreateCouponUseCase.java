package com.marcioaraki.coupon_api.domain.usecase;

import com.marcioaraki.coupon_api.domain.entity.Coupon;

public interface CreateCouponUseCase {
    Coupon execute(Coupon coupon);
}
