package com.marcioaraki.coupon_api.domain.usecase;

import java.util.UUID;

public interface DeleteCouponUseCase {
    void execute(UUID id);
}
