package com.marcioaraki.coupon_api.application.service;

import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.exception.CouponNotFoundException;
import com.marcioaraki.coupon_api.domain.repository.CouponRepository;
import com.marcioaraki.coupon_api.domain.usecase.GetCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCouponService implements GetCouponUseCase {

    private final CouponRepository couponRepository;

    @Override
    public Coupon execute(UUID id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Cupom não encontrado: " + id));
    }
}
