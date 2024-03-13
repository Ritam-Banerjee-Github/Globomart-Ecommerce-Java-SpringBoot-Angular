package com.in.ecommerce.service.customer;

import com.in.ecommerce.dto.OrderedProductResponseDto;
import com.in.ecommerce.dto.ReviewDto;
import com.in.ecommerce.wrapper.ReviewWrapper;

import java.io.IOException;

public interface ReviewService {
    OrderedProductResponseDto getOrderedProuctsDetailsByOrderId(Long orderId);

    ReviewWrapper giveReview(ReviewDto reviewDto) throws IOException;
}
