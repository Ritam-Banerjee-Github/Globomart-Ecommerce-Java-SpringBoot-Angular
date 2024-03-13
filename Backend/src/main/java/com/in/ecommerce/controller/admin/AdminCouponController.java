package com.in.ecommerce.controller.admin;

import com.in.ecommerce.entity.Coupon;
import com.in.ecommerce.exceptions.ValidationException;
import com.in.ecommerce.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon){
        try{
            Coupon createdCoupon=couponService.creaceCoupon(coupon);
            return ResponseEntity.ok(createdCoupon);
        }catch (ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAlLCoupons(){
        return ResponseEntity.ok(couponService.getAllCoupons());
    }
}
