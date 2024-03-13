package com.in.ecommerce.wrapper;

import lombok.Data;

@Data
public class CartItemsWrapper {
    private Long id;
    private Long price;
    private Long quantity;

    private Long productId;
    private Long userId;
    private Long orderId;

    private String productName;
    private byte[] returnedImg;
}
