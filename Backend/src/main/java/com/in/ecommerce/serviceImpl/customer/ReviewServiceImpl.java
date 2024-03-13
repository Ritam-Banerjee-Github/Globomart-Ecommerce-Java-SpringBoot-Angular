package com.in.ecommerce.serviceImpl.customer;

import com.in.ecommerce.daoImpl.*;
import com.in.ecommerce.dto.OrderDto;
import com.in.ecommerce.dto.OrderedProductResponseDto;
import com.in.ecommerce.dto.ReviewDto;
import com.in.ecommerce.dto.User;
import com.in.ecommerce.service.customer.ReviewService;
import com.in.ecommerce.wrapper.CartItemsWrapper;
import com.in.ecommerce.wrapper.ProductWrapper;
import com.in.ecommerce.wrapper.ReviewWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.Optional;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private OrderDaoImpl orderDaoImpl;
    @Autowired
    private CartItemsDaoImpl cartItemsDaoImpl;

    @Autowired
    private ProductDaoImpl productDaoImpl;

    @Autowired
    private ReviewDaoImpl reviewDaoImpl;

    @Autowired
    private UserDaoImpl userDaoImpl;


    @Override
    public OrderedProductResponseDto getOrderedProuctsDetailsByOrderId(Long orderId) {
        Optional<OrderDto> optionalOrderDto=orderDaoImpl.findById(orderId);
        OrderedProductResponseDto orderedProductResponseDto=new OrderedProductResponseDto();

        if(optionalOrderDto.isPresent()){
           orderedProductResponseDto.setOrderAmount(optionalOrderDto.get().getAmount());

           List<ProductWrapper> list=new ArrayList<>();
           for(CartItemsWrapper cartItemsWrapper:cartItemsDaoImpl.getAllCartItemsByOrderId(orderId)){
               ProductWrapper productWrapper=new ProductWrapper();
               productWrapper.setId(cartItemsWrapper.getProductId());
               productWrapper.setName(cartItemsWrapper.getProductName());
               productWrapper.setPrice(cartItemsWrapper.getPrice());
               productWrapper.setQuantity(cartItemsWrapper.getQuantity());

               productWrapper.setByteImage(cartItemsWrapper.getReturnedImg());

               list.add(productWrapper);
           }

           orderedProductResponseDto.setProductWrapperList(list);

           return orderedProductResponseDto;
        }

        return null;
    }

    @Override
    public ReviewWrapper giveReview(ReviewDto reviewDto) throws IOException {
        Optional<User> optionalUser=userDaoImpl.findById(reviewDto.getUserId());

        if(optionalUser.isPresent()){
            int rowCount=this.reviewDaoImpl.giveReview(reviewDto);
            log.info("No of row inserted for review --> "+rowCount);

            ReviewWrapper reviewWrapper= this.reviewDaoImpl.findByUserIdandProductId(reviewDto);

            reviewWrapper.setUserName(optionalUser.get().getName());
            return reviewWrapper;
        }

        return null;
    }
}
