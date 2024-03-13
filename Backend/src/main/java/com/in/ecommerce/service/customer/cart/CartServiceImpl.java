package com.in.ecommerce.service.customer.cart;

import com.in.ecommerce.daoImpl.CartItemsDaoImpl;
import com.in.ecommerce.daoImpl.OrderDaoImpl;
import com.in.ecommerce.daoImpl.ProductDaoImpl;
import com.in.ecommerce.daoImpl.UserDaoImpl;
import com.in.ecommerce.dto.*;
import com.in.ecommerce.entity.Coupon;
import com.in.ecommerce.enums.OrderStatus;
import com.in.ecommerce.exceptions.ValidationException;
import com.in.ecommerce.repository.CouponRepository;
import com.in.ecommerce.wrapper.CartItemsWrapper;
import com.in.ecommerce.wrapper.OrderWrapper;
import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class CartServiceImpl implements CartService{
    @Autowired
    private UserDaoImpl userDaoImpl;

    @Autowired
    private OrderDaoImpl orderDaoImpl;

    @Autowired
    private CartItemsDaoImpl cartItemsDaoImpl;

    @Autowired
    private ProductDaoImpl productDaoImpl;

    @Autowired
    private CouponRepository couponRepository;


    @Override
    public ResponseEntity<?> addProductInCart(AddProductInCartDto addProductInCartDto) {
        log.info("Inside addProductInCart with details -> "+addProductInCartDto.getProductId()+" "+addProductInCartDto.getUserId());
        OrderDto activeOrderDto=orderDaoImpl.findByUserIdAndStatus(addProductInCartDto.getUserId(), String.valueOf(OrderStatus.Pending));
        Optional<CartItemsDto> optionalCartItemsDto=cartItemsDaoImpl.findByProductIdandOrderIdandUserId(
                addProductInCartDto.getProductId(),activeOrderDto.getId(),addProductInCartDto.getUserId()
        );

        if(optionalCartItemsDto.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }else{
             Optional<ProductWrapper> optionalProductWrapper=productDaoImpl.findById(addProductInCartDto.getProductId());
             Optional<User> optionalUser=userDaoImpl.findById(addProductInCartDto.getUserId());

             if(optionalUser.isPresent() && optionalProductWrapper.isPresent()){
                     CartItemsDto cartItemsDto=new CartItemsDto();
                     cartItemsDto.setPrice(optionalProductWrapper.get().getPrice());
                     cartItemsDto.setQuantity(1L);
                     cartItemsDto.setProductId(addProductInCartDto.getProductId());
                     cartItemsDto.setUserId(addProductInCartDto.getUserId());
                     cartItemsDto.setOrderId(activeOrderDto.getId());

                     int insertedCartItems=cartItemsDaoImpl.addToCartItems(cartItemsDto);
                     log.info("Number of new cart item inserted -> "+insertedCartItems);

                     activeOrderDto.setTotalAmount(activeOrderDto.getTotalAmount()+cartItemsDto.getPrice());
                     activeOrderDto.setAmount(activeOrderDto.getAmount()+cartItemsDto.getPrice());

                     cartItemsDto.setId(cartItemsDaoImpl.findByProductIdandOrderIdandUserId(
                             addProductInCartDto.getProductId(),activeOrderDto.getId(),addProductInCartDto.getUserId()
                     ).get().getId());

                   //  activeOrderDto.getCartItemsDto().add(cartItemsDto);

                      orderDaoImpl.updateTotalAmountandAmountandCartItemsById(activeOrderDto.getTotalAmount(),activeOrderDto.getAmount(),
                              cartItemsDto.getId(),activeOrderDto.getId());

                      return ResponseEntity.status(HttpStatus.CREATED).body(cartItemsDto);


             }else{
                 return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Product not found");
             }
        }

    }

    @Override
    public OrderWrapper getCartByUserId(Long userId) {
        OrderDto activeOrderDto=orderDaoImpl.findByUserIdAndStatus(userId, String.valueOf(OrderStatus.Pending));
        OrderWrapper orderWrapper=new OrderWrapper();
        orderWrapper.setAmount(activeOrderDto.getAmount());
        orderWrapper.setId(activeOrderDto.getId());
        orderWrapper.setOrderStatus(activeOrderDto.getOrderStatus());
        orderWrapper.setDiscount(activeOrderDto.getDiscount());
        orderWrapper.setTotalAmount(activeOrderDto.getTotalAmount());
        List<CartItemsWrapper> cartItemsWrapperList=cartItemsDaoImpl.getAllCartItemsByUserIdandOrderId(userId,activeOrderDto.getId());

        orderWrapper.setCartItemsWrappers(cartItemsWrapperList);
        if(activeOrderDto.getCouponCode()!=null && !activeOrderDto.getCouponCode().isEmpty()){
            orderWrapper.setCouponName(activeOrderDto.getCouponCode());
        }

        return orderWrapper;
    }

    @Override
    public OrderWrapper applyCoupon(Long userId, String code) {
        OrderDto activeOrderDto=orderDaoImpl.findByUserIdAndStatus(userId, String.valueOf(OrderStatus.Pending));
        Coupon coupon=couponRepository.findByCode(code).orElseThrow(()->new ValidationException("Coupon not found"));

        if(couponIsExpired(coupon)){
            throw new ValidationException("Coupon has expired");
        }

        double discountAmount=(coupon.getDiscount()/100.0)*activeOrderDto.getTotalAmount();
        double netAmount=activeOrderDto.getTotalAmount()-discountAmount;

        activeOrderDto.setAmount((long) netAmount);
        activeOrderDto.setDiscount((long) discountAmount);
        activeOrderDto.setCouponCode(code);

        orderDaoImpl.updateAmountandDiscountandCouponnameById(activeOrderDto,coupon.getCode());

        OrderWrapper orderWrapper=new OrderWrapper();

        orderWrapper.setId(activeOrderDto.getId());
        orderWrapper.setAmount(activeOrderDto.getAmount());
        orderWrapper.setOrderStatus(activeOrderDto.getOrderStatus());
        orderWrapper.setDiscount(activeOrderDto.getDiscount());
        if(activeOrderDto.getCouponCode()!=null && !activeOrderDto.getCouponCode().isEmpty()){
            orderWrapper.setCouponName(activeOrderDto.getCouponCode());
        }

        return orderWrapper;
    }

    @Override
    public OrderWrapper increaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        OrderDto activeOrderDto=orderDaoImpl.findByUserIdAndStatus(addProductInCartDto.getUserId(), String.valueOf(OrderStatus.Pending));
        Optional<ProductWrapper> optionalProductWrapper=productDaoImpl.findById(addProductInCartDto.getProductId());
        Optional<CartItemsDto> optionalCartItemsDto=cartItemsDaoImpl.findByProductIdandOrderIdandUserId(addProductInCartDto.getProductId(),activeOrderDto.getId(),addProductInCartDto.getUserId());

        if(optionalCartItemsDto.isPresent() && optionalProductWrapper.isPresent()){
            CartItemsDto cartItemsDto=optionalCartItemsDto.get();
            ProductWrapper productWrapper=optionalProductWrapper.get();

            activeOrderDto.setAmount(activeOrderDto.getAmount()+productWrapper.getPrice());
            activeOrderDto.setTotalAmount(activeOrderDto.getTotalAmount()+productWrapper.getPrice());

            cartItemsDto.setQuantity(cartItemsDto.getQuantity()+1);

            if(activeOrderDto.getCouponCode()!=null && !activeOrderDto.getCouponCode().isEmpty()){
                Coupon coupon=couponRepository.findByCode(activeOrderDto.getCouponCode()).orElseThrow(()->new ValidationException("Coupon not found"));

                double discountAmount=(coupon.getDiscount()/100.0)*activeOrderDto.getTotalAmount();
                double netAmount=activeOrderDto.getTotalAmount()-discountAmount;

                activeOrderDto.setAmount((long) netAmount);
                activeOrderDto.setDiscount((long) discountAmount);
            }

            orderDaoImpl.updateAmountandTotalAmountandDiscountById(activeOrderDto);
            cartItemsDaoImpl.updateQuantityById(cartItemsDto);

            OrderWrapper orderWrapper=new OrderWrapper();

            orderWrapper.setId(activeOrderDto.getId());
            orderWrapper.setAmount(activeOrderDto.getAmount());
            orderWrapper.setTotalAmount(activeOrderDto.getTotalAmount());
            orderWrapper.setOrderStatus(activeOrderDto.getOrderStatus());
            orderWrapper.setDiscount(activeOrderDto.getDiscount());
            if(activeOrderDto.getCouponCode()!=null && !activeOrderDto.getCouponCode().isEmpty()){
                orderWrapper.setCouponName(activeOrderDto.getCouponCode());
            }

            return orderWrapper;

        }

        return null;
    }

    @Override
    public OrderWrapper decreaseProductQuantity(AddProductInCartDto addProductInCartDto) {
        OrderDto activeOrderDto=orderDaoImpl.findByUserIdAndStatus(addProductInCartDto.getUserId(), String.valueOf(OrderStatus.Pending));
        Optional<ProductWrapper> optionalProductWrapper=productDaoImpl.findById(addProductInCartDto.getProductId());
        Optional<CartItemsDto> optionalCartItemsDto=cartItemsDaoImpl.findByProductIdandOrderIdandUserId(addProductInCartDto.getProductId(),activeOrderDto.getId(),addProductInCartDto.getUserId());

        if(optionalCartItemsDto.isPresent() && optionalProductWrapper.isPresent()){
            CartItemsDto cartItemsDto=optionalCartItemsDto.get();
            ProductWrapper productWrapper=optionalProductWrapper.get();

            activeOrderDto.setAmount(activeOrderDto.getAmount()-productWrapper.getPrice());
            activeOrderDto.setTotalAmount(activeOrderDto.getTotalAmount()-productWrapper.getPrice());

            cartItemsDto.setQuantity(cartItemsDto.getQuantity()-1);

            if(activeOrderDto.getCouponCode()!=null && !activeOrderDto.getCouponCode().isEmpty()){
                Coupon coupon=couponRepository.findByCode(activeOrderDto.getCouponCode()).orElseThrow(()->new ValidationException("Coupon not found"));

                double discountAmount=(coupon.getDiscount()/100.0)*activeOrderDto.getTotalAmount();
                double netAmount=activeOrderDto.getTotalAmount()-discountAmount;

                activeOrderDto.setAmount((long) netAmount);
                activeOrderDto.setDiscount((long) discountAmount);
            }

            orderDaoImpl.updateAmountandTotalAmountandDiscountById(activeOrderDto);
            cartItemsDaoImpl.updateQuantityById(cartItemsDto);

            OrderWrapper orderWrapper=new OrderWrapper();

            orderWrapper.setId(activeOrderDto.getId());
            orderWrapper.setAmount(activeOrderDto.getAmount());
            orderWrapper.setTotalAmount(activeOrderDto.getTotalAmount());
            orderWrapper.setOrderStatus(activeOrderDto.getOrderStatus());
            orderWrapper.setDiscount(activeOrderDto.getDiscount());
            if(activeOrderDto.getCouponCode()!=null && !activeOrderDto.getCouponCode().isEmpty()){
                orderWrapper.setCouponName(activeOrderDto.getCouponCode());
            }

            return orderWrapper;

        }

        return null;
    }

    @Override
    public OrderWrapper placeOrder(PlaceOrderDto placeOrderDto) {
        OrderDto activeOrderDto=orderDaoImpl.findByUserIdAndStatus(placeOrderDto.getUserId(), String.valueOf(OrderStatus.Pending));
        Optional<User> optionalUser=userDaoImpl.findById(placeOrderDto.getUserId());

        if(optionalUser.isPresent()){
            activeOrderDto.setAddress(placeOrderDto.getAddress());
            activeOrderDto.setOrderDescription(placeOrderDto.getOrderDescription());
            activeOrderDto.setDate(new Date());
            activeOrderDto.setOrderStatus(OrderStatus.Placed);
            activeOrderDto.setTrackingId(UUID.randomUUID());

            orderDaoImpl.updateAddressandDescriptionandStatusandDateandTrackingId(activeOrderDto);



            OrderDto orderDto=new OrderDto();
            orderDto.setAmount(0l);
            orderDto.setTotalAmount(0l);
            orderDto.setDiscount(0L);
            orderDto.setUserId(placeOrderDto.getUserId());
            orderDto.setOrderStatus(OrderStatus.Pending);

            this.orderDaoImpl.createOrderForNewUser(orderDto);


            OrderWrapper orderWrapper=new OrderWrapper();

            orderWrapper.setId(activeOrderDto.getId());
            orderWrapper.setAmount(activeOrderDto.getAmount());
            orderWrapper.setTotalAmount(activeOrderDto.getTotalAmount());
            orderWrapper.setOrderStatus(activeOrderDto.getOrderStatus());
            orderWrapper.setDiscount(activeOrderDto.getDiscount());
            if(activeOrderDto.getCouponCode()!=null && !activeOrderDto.getCouponCode().isEmpty()){
                orderWrapper.setCouponName(activeOrderDto.getCouponCode());
            }

            return orderWrapper;

        }

        return null;
    }

    @Override
    public List<OrderWrapper> getMyPlacedOrders(Long userId) {
        return orderDaoImpl.getMyPlacedOrders(userId);
    }

    @Override
    public OrderDto searchOrderByTrackingId(UUID trackingId) {
        Optional<OrderDto> optionalOrderDto=this.orderDaoImpl.findOrderByTrackingId(trackingId);
        if(optionalOrderDto.isPresent()) return optionalOrderDto.get();

        return null;
    }

    private boolean couponIsExpired(Coupon coupon){
        Date currDate=new Date();
        Date expirationDate=coupon.getExpirationDate();

        return expirationDate!=null && currDate.after(expirationDate);
    }


}
