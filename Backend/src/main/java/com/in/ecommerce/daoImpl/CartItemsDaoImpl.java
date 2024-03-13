package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.CartItemsDto;
import com.in.ecommerce.dto.User;
import com.in.ecommerce.enums.UserRole;
import com.in.ecommerce.wrapper.CartItemsWrapper;
import com.in.ecommerce.wrapper.OrderWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class CartItemsDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<CartItemsDto> findByProductIdandOrderIdandUserId(Long productId, Long id, Long userId) {

           try {
               String sql = "select * from cart_items where productid=? and userid=? and orderid=?";
               CartItemsDto cartItemsDto = this.jdbcTemplate.queryForObject(sql, new RowMapper<CartItemsDto>() {
                   @Override
                   public CartItemsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                       CartItemsDto cartItemsDto1 = new CartItemsDto();
                       cartItemsDto1.setId(rs.getLong(1));
                       cartItemsDto1.setPrice(rs.getLong(2));
                       cartItemsDto1.setQuantity(rs.getLong(3));
                       cartItemsDto1.setProductId(rs.getLong(4));
                       cartItemsDto1.setUserId(rs.getLong(5));
                       cartItemsDto1.setOrderId(rs.getLong(6));

                       return cartItemsDto1;
                   }
               }, productId, userId, id);

               return Optional.ofNullable(cartItemsDto);
           }catch (EmptyResultDataAccessException ex){
               Optional.empty();
           }

           return Optional.empty();
    }

    public int addToCartItems(CartItemsDto cartItemsDto) {
        String sql="insert into cart_items(price,quantity,productid,userid,orderid) values(?,?,?,?,?)";

        return this.jdbcTemplate.update(sql,cartItemsDto.getPrice(),cartItemsDto.getQuantity(),cartItemsDto.getProductId(),
                cartItemsDto.getUserId(),cartItemsDto.getOrderId());
    }

    public List<CartItemsWrapper> getAllCartItemsByUserIdandOrderId(Long userId, Long orderId) {
        List<CartItemsWrapper> list=new ArrayList<>();
        String sql="select c.id,c.price,c.quantity,c.productid,c.userid,c.orderid,p.name,p.image from cart_items c JOIN product p ON c.productid=p.id where c.userid=? and c.orderid=?";
        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql,userId,orderId);

        for(Map<String,Object> row:rows){
            CartItemsWrapper cartItemsWrapper=new CartItemsWrapper();
            cartItemsWrapper.setId((Long) row.get("id"));
            cartItemsWrapper.setPrice((Long) row.get("price"));
            cartItemsWrapper.setQuantity((Long) row.get("quantity"));
            cartItemsWrapper.setProductId((Long) row.get("productid"));
            cartItemsWrapper.setUserId((Long) row.get("userid"));
            cartItemsWrapper.setOrderId((Long) row.get("orderid"));
            cartItemsWrapper.setProductName((String) row.get("name"));
            cartItemsWrapper.setReturnedImg((byte[]) row.get("image"));

            list.add(cartItemsWrapper);
        }

        return list;
    }

    public List<CartItemsWrapper> getAllCartItemsByOrderId(Long orderId) {
        List<CartItemsWrapper> list=new ArrayList<>();
        String sql="select c.id,c.price,c.quantity,c.productid,p.name,p.image from cart_items c JOIN product p ON c.productid=p.id where c.orderid=?";
        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql,orderId);

        for(Map<String,Object> row:rows){
            CartItemsWrapper cartItemsWrapper=new CartItemsWrapper();

            cartItemsWrapper.setId((Long) row.get("id"));
            cartItemsWrapper.setPrice((Long) row.get("price"));
            cartItemsWrapper.setQuantity((Long) row.get("quantity"));
            cartItemsWrapper.setProductId((Long) row.get("productid"));
            cartItemsWrapper.setProductName((String) row.get("name"));
            cartItemsWrapper.setReturnedImg((byte[]) row.get("image"));

            list.add(cartItemsWrapper);
        }

        return list;
    }

    public void updateQuantityById(CartItemsDto cartItemsDto) {
        String sql="update cart_items set quantity=? where id=?";
        this.jdbcTemplate.update(sql,cartItemsDto.getQuantity(),cartItemsDto.getId());
    }

}
