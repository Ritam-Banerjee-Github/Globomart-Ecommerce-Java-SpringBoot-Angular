package com.in.ecommerce.service.customer;

import com.in.ecommerce.dto.ProductDetailsDto;
import com.in.ecommerce.wrapper.ProductWrapper;

import java.util.List;

public interface CustomerProductService {
    List<ProductWrapper> getAllProducts();

    List<ProductWrapper> getProductByName(String name);

    ProductDetailsDto getProductDetailsById(Long productId);
}
