package com.in.ecommerce.service.admin;

import com.in.ecommerce.entity.Coupon;
import com.in.ecommerce.exceptions.ValidationException;

import java.util.List;

public interface CouponService {
    Coupon creaceCoupon(Coupon coupon);

    List<Coupon> getAllCoupons();
}
