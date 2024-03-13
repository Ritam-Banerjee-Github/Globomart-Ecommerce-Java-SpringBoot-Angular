package com.in.ecommerce.service.customer.cart;

import com.in.ecommerce.dto.AddProductInCartDto;
import com.in.ecommerce.dto.OrderDto;
import com.in.ecommerce.dto.PlaceOrderDto;
import com.in.ecommerce.wrapper.OrderWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface CartService {
    ResponseEntity addProductInCart(AddProductInCartDto addProductInCartDto);


    OrderWrapper getCartByUserId(Long userId);

    OrderWrapper applyCoupon(Long userId, String code);

    OrderWrapper increaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderWrapper decreaseProductQuantity(AddProductInCartDto addProductInCartDto);

    OrderWrapper placeOrder(PlaceOrderDto placeOrderDto);

    List<OrderWrapper> getMyPlacedOrders(Long userId);

    OrderDto searchOrderByTrackingId(UUID trackingId);
}
