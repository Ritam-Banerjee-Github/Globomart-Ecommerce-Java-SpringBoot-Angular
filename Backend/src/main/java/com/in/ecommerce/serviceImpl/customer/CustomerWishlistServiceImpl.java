package com.in.ecommerce.serviceImpl.customer;

import com.in.ecommerce.daoImpl.CustomerWishlistDaoImpl;
import com.in.ecommerce.daoImpl.ProductDaoImpl;
import com.in.ecommerce.daoImpl.UserDaoImpl;
import com.in.ecommerce.dto.User;
import com.in.ecommerce.dto.WishListDto;
import com.in.ecommerce.service.customer.CustomerWishlistService;
import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CustomerWishlistServiceImpl implements CustomerWishlistService {
    @Autowired
    private CustomerWishlistDaoImpl customerWishlistDaoImpl;
    @Autowired
    private UserDaoImpl userDaoImpl;
    @Autowired
    private ProductDaoImpl productDaoImpl;


    @Override
    public WishListDto addProductToWishlist(WishListDto wishListDto) {
        Optional<ProductWrapper> optionalProductWrapper=this.productDaoImpl.findById(wishListDto.getProductId());
        Optional<User> optionalUser=this.userDaoImpl.findById(wishListDto.getUserId());
        Long wishlistid=this.customerWishlistDaoImpl.findIdByUserIdandProductId(wishListDto);

        if(wishlistid==null && optionalUser.isPresent() && optionalProductWrapper.isPresent()){
            this.customerWishlistDaoImpl.addProductToWishlist(wishListDto);

           WishListDto postedWishlistDto=new WishListDto();
           postedWishlistDto.setId(this.customerWishlistDaoImpl.findIdByUserIdandProductId(wishListDto));
           postedWishlistDto.setProductId(wishListDto.getProductId());
           postedWishlistDto.setReturnedImg(optionalProductWrapper.get().getByteImage());
           postedWishlistDto.setProductName(optionalProductWrapper.get().getName());
           postedWishlistDto.setProductDescription(optionalProductWrapper.get().getDescription());
           postedWishlistDto.setPrice(optionalProductWrapper.get().getPrice());
           postedWishlistDto.setUserId(wishListDto.getUserId());

           return postedWishlistDto;
        }
        return null;
    }

    @Override
    public List<WishListDto> getWishlistByUserid(Long userId) {
        return this.customerWishlistDaoImpl.findAllByUSerId(userId);
    }
}
