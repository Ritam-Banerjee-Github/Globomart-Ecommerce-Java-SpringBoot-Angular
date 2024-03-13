package com.in.ecommerce.dto;

import lombok.Data;

@Data
public class WishListDto {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String productDescription;
    private byte[] returnedImg;
    private Long price;
}
