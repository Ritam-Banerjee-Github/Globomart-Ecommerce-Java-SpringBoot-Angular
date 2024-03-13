package com.in.ecommerce.service.admin;

import com.in.ecommerce.dto.FAQDto;
import com.in.ecommerce.dto.ProductDto;
import com.in.ecommerce.wrapper.ProductWrapper;

import java.io.IOException;
import java.util.List;

public interface AdminProductService {
    ProductWrapper addProduct(ProductDto productDto) throws IOException;

    List<ProductWrapper> getAllProducts();

    List<ProductWrapper> getProductByName(String name);

    boolean deleteProductById(Long productId);

    FAQDto postFAQ(Long productId, FAQDto faqDto);

    ProductWrapper getProductById(Long productId);

    ProductWrapper updateProduct(Long productId, ProductDto productDto) throws IOException;
}
