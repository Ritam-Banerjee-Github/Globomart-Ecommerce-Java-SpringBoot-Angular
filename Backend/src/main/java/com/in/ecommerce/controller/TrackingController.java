package com.in.ecommerce.controller;

import com.in.ecommerce.dto.OrderDto;
import com.in.ecommerce.service.customer.cart.CartService;
import com.in.ecommerce.wrapper.OrderWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TrackingController {
    @Autowired
    private CartService cartService;

    @GetMapping("/order/{trackingId}")
    public ResponseEntity<OrderDto> searchOrderByTrackingId(@PathVariable UUID trackingId){
        OrderDto orderDto=this.cartService.searchOrderByTrackingId(trackingId);
        if(orderDto==null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(orderDto);
    }
}
