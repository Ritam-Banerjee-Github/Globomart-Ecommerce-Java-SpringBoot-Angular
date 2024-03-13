package com.in.ecommerce.serviceImpl.customer;

import com.in.ecommerce.daoImpl.CustomerProductDaoImpl;
import com.in.ecommerce.daoImpl.FAQDaoImpl;
import com.in.ecommerce.daoImpl.ProductDaoImpl;
import com.in.ecommerce.daoImpl.ReviewDaoImpl;
import com.in.ecommerce.dto.FAQDto;
import com.in.ecommerce.dto.ProductDetailsDto;
import com.in.ecommerce.service.customer.CustomerProductService;
import com.in.ecommerce.wrapper.ProductWrapper;
import com.in.ecommerce.wrapper.ReviewWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerProductServiceImpl implements CustomerProductService {
    private final CustomerProductDaoImpl customerProductDaoImpl;
    private final ProductDaoImpl productDaoImpl;
    private final ReviewDaoImpl reviewDaoImpl;
    private final FAQDaoImpl faqDaoImpl;

    @Override
    public List<ProductWrapper> getAllProducts() {
        return customerProductDaoImpl.getAllProducts();
    }

    @Override
    public List<ProductWrapper> getProductByName(String name) {
        return customerProductDaoImpl.getAllProductsByName(name);
    }

    @Override
    public ProductDetailsDto getProductDetailsById(Long productId) {
        Optional<ProductWrapper> productWrapperOptional=this.productDaoImpl.findById(productId);

        if(productWrapperOptional.isPresent()){
            ProductWrapper productWrapper=productWrapperOptional.get();

            List<FAQDto> faqDtoList=this.faqDaoImpl.findAllByProductId(productId);
            List<ReviewWrapper> reviewWrapperList=this.reviewDaoImpl.findAllByProductId(productId);

            ProductDetailsDto productDetailsDto=new ProductDetailsDto();
            productDetailsDto.setProductWrapper(productWrapper);
            productDetailsDto.setFaqDtoList(faqDtoList);
            productDetailsDto.setReviewWrapperList(reviewWrapperList);

            return productDetailsDto;

        }

        return null;
    }
}
