package com.marcioaraki.coupon_api.application.service;

import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.repository.CouponRepository;
import com.marcioaraki.coupon_api.domain.usecase.CreateCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCouponService implements CreateCouponUseCase {

    private final CouponRepository couponRepository;

    @Override
    public Coupon execute(Coupon coupon) {
        return couponRepository.save(coupon);
    }
}
