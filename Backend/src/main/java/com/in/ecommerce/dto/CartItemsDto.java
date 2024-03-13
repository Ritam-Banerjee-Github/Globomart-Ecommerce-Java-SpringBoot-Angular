package com.in.ecommerce.dto;

import lombok.Data;

@Data
public class CartItemsDto {
private Long id;
private Long price;
private Long quantity;

private Long productId;
private Long userId;
private Long orderId;

}
