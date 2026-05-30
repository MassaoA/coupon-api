package com.marcioaraki.coupon_api.application.service;

import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.exception.CouponAlreadyDeletedException;
import com.marcioaraki.coupon_api.domain.exception.CouponNotFoundException;
import com.marcioaraki.coupon_api.domain.repository.CouponRepository;
import com.marcioaraki.coupon_api.domain.usecase.DeleteCouponUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteCouponService implements DeleteCouponUseCase {

    private final CouponRepository couponRepository;

    @Override
    public void execute(UUID id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Cupom não encontrado: " + id));

        if (coupon.isDeleted()) {
            throw new CouponAlreadyDeletedException("Cupom já foi deletado: " + id);
        }

        coupon.delete();
        couponRepository.save(coupon);
    }
}
