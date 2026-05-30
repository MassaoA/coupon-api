package com.marcioaraki.coupon_api.api.controller;

import com.marcioaraki.coupon_api.api.dto.CreateCouponRequest;
import com.marcioaraki.coupon_api.api.dto.CouponResponse;
import com.marcioaraki.coupon_api.domain.entity.Coupon;
import com.marcioaraki.coupon_api.domain.usecase.CreateCouponUseCase;
import com.marcioaraki.coupon_api.domain.usecase.DeleteCouponUseCase;
import com.marcioaraki.coupon_api.domain.usecase.GetCouponUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;
    private final GetCouponUseCase getCouponUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse create(@RequestBody @Valid CreateCouponRequest request) {
        Coupon coupon = new Coupon(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );
        return CouponResponse.from(createCouponUseCase.execute(coupon));
    }

    @GetMapping("/{id}")
    public CouponResponse getById(@PathVariable UUID id) {
        return CouponResponse.from(getCouponUseCase.execute(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        deleteCouponUseCase.execute(id);
    }
}
