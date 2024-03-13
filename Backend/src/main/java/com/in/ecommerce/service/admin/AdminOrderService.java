package com.in.ecommerce.service.admin;

import com.in.ecommerce.dto.AnalyticsResponseDto;
import com.in.ecommerce.wrapper.OrderWrapper;

import java.util.List;

public interface AdminOrderService {
    List<OrderWrapper> getAllPlacesOrders();

    OrderWrapper changeOrderStatus(Long orderId, String status);

    AnalyticsResponseDto getAnalytics();
}
