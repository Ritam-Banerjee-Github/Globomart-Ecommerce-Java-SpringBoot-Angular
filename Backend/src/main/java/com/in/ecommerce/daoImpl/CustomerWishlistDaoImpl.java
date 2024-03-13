package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.WishListDto;
import com.in.ecommerce.wrapper.ReviewWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.*;

@Repository
@Slf4j
public class CustomerWishlistDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addProductToWishlist(WishListDto wishListDto) {
        String sql="insert into wishlist(userid,productid) values(?,?)";
        this.jdbcTemplate.update(sql,wishListDto.getUserId(),wishListDto.getProductId());
    }

    public Long findIdByUserIdandProductId(WishListDto wishListDto) {
        try {
            String sql = "select id from wishlist where userid=? and productid=?";
            return this.jdbcTemplate.queryForObject(sql, Long.class, wishListDto.getUserId(), wishListDto.getProductId());
        }catch(EmptyResultDataAccessException ex){
            return null;
        }
    }

    public List<WishListDto> findAllByUSerId(Long userId) {
     List<WishListDto> list=new ArrayList<>();
     String sql="select w.id, w.userid, w.productid, p.name,p.description,p.price,p.image from wishlist w JOIN product p ON w.productid=p.id where w.userid=?";
     List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql,userId);

     for(Map<String,Object> row:rows){
         WishListDto wishListDto=new WishListDto();

         wishListDto.setId((Long) row.get("id"));
         wishListDto.setUserId(userId);
         wishListDto.setProductId((Long) row.get("productid"));
         wishListDto.setProductName((String) row.get("name"));
         wishListDto.setProductDescription((String) row.get("description"));
         wishListDto.setReturnedImg((byte[]) row.get("image"));
         wishListDto.setPrice((Long) row.get("price"));

         list.add(wishListDto);
     }
     return list;
    }

}
