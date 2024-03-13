package com.in.ecommerce.wrapper;

import com.in.ecommerce.enums.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrderWrapper {
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
    private String userName;
    private Long userId;
    private List<CartItemsWrapper> cartItemsWrappers;
    private String couponName;
}
