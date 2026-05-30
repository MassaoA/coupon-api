package com.marcioaraki.coupon_api.infrastructure.persistance;

import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository jpaRepository;

    @Override
    public Coupon save(Coupon coupon) {
        CouponJpaEntity entity = CouponJpaEntity.from(coupon);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return jpaRepository.findById(id).map(CouponJpaEntity::toDomain);
    }
}
