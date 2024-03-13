package com.in.ecommerce.dto;

import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.Data;

import java.util.*;

@Data
public class OrderedProductResponseDto {
    private List<ProductWrapper> productWrapperList;
    private Long orderAmount;
}
