package com.in.ecommerce.serviceImpl.admin;

import com.in.ecommerce.entity.Coupon;
import com.in.ecommerce.exceptions.ValidationException;
import com.in.ecommerce.repository.CouponRepository;
import com.in.ecommerce.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;


    @Override
    public Coupon creaceCoupon(Coupon coupon) {
        if(couponRepository.existsByCode(coupon.getCode())){
            throw  new ValidationException("Coupon code already exists");
        }
          return  couponRepository.save(coupon);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

}
