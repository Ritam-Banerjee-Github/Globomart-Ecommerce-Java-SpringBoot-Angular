package com.in.ecommerce.dto;

import lombok.Data;

@Data
public class PlaceOrderDto {
    private Long userId;
    private String orderDescription;
    private String address;
}
