package com.in.ecommerce.controller.customer;

import com.in.ecommerce.dto.WishListDto;
import com.in.ecommerce.service.customer.CustomerWishlistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/customer")
@Slf4j
public class WishlistController {
    @Autowired
    private CustomerWishlistService customerWishlistService;

    @PostMapping("/wishlist")
    public ResponseEntity<?> addProductToWishlist(@RequestBody WishListDto wishListDto){
        WishListDto postedWishlistDto=this.customerWishlistService.addProductToWishlist(wishListDto);


        if(postedWishlistDto==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(postedWishlistDto);
    }

    @GetMapping("/wishlist/{userId}")
    public ResponseEntity<List<WishListDto>> getWishlistByUserid(@PathVariable Long userId){
        return ResponseEntity.ok(this.customerWishlistService.getWishlistByUserid(userId));
    }
}
