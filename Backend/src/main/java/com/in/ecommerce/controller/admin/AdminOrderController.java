package com.in.ecommerce.controller.admin;

import com.in.ecommerce.dto.AnalyticsResponseDto;
import com.in.ecommerce.dto.OrderDto;
import com.in.ecommerce.service.admin.AdminOrderService;
import com.in.ecommerce.wrapper.OrderWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
    @Autowired
    private AdminOrderService adminOrderService;

    @GetMapping("/placedOrders")
    public ResponseEntity<List<OrderWrapper>> getAllPlacedOrders(){
        return ResponseEntity.ok(adminOrderService.getAllPlacesOrders());
    }

    @GetMapping("/order/{orderId}/{status}")
    public ResponseEntity<OrderWrapper> changeOrderStatus(@PathVariable Long orderId, @PathVariable String status){
        return ResponseEntity.ok(adminOrderService.changeOrderStatus(orderId,status));
    }

    @GetMapping("/order/analytics")
    public ResponseEntity<AnalyticsResponseDto> getAnalytics(){
        return ResponseEntity.ok(adminOrderService.getAnalytics());
    }
}

