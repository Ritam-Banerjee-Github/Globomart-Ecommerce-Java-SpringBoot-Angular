package com.in.ecommerce.service.customer;

import com.in.ecommerce.dto.WishListDto;

import java.util.List;

public interface CustomerWishlistService{

    WishListDto addProductToWishlist(WishListDto wishListDto);

    List<WishListDto> getWishlistByUserid(Long userId);
}
