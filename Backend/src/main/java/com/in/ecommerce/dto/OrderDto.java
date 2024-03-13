package com.in.ecommerce.dto;
import com.in.ecommerce.enums.OrderStatus;
import lombok.Data;

import java.util.*;

@Data
public class OrderDto {
    private Long id;
    private String orderDescription;
    private Date date;
    private Long amount;
    private String address;
    private String payment;
    private OrderStatus orderStatus;
    private Long totalAmount;
    private Long discount;
    private UUID trackingId;
    private Long userId;
    private List<CartItemsDto> cartItemsDto;
    private String couponCode;

}
