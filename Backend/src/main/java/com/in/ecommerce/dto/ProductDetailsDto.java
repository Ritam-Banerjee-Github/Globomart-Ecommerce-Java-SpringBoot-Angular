package com.in.ecommerce.dto;

import com.in.ecommerce.wrapper.ProductWrapper;
import com.in.ecommerce.wrapper.ReviewWrapper;
import lombok.Data;

import java.util.*;
@Data
public class ProductDetailsDto {
    private ProductWrapper productWrapper;
    private List<FAQDto> faqDtoList;
    private List<ReviewWrapper> reviewWrapperList;
}
