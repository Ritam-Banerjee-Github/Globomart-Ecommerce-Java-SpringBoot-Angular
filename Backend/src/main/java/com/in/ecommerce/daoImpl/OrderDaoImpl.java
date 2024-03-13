package com.in.ecommerce.daoImpl;

import com.in.ecommerce.dto.OrderDto;
import com.in.ecommerce.dto.User;
import com.in.ecommerce.enums.OrderStatus;
import com.in.ecommerce.wrapper.OrderWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Slf4j
public class OrderDaoImpl {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDaoImpl userDaoImpl;

    public void createOrderForNewUser(OrderDto orderDto) {
        try {
            String sql = "insert into orders(amount,totalamount,discount,userid,orderstatus) values(?,?,?,?,?)";
            String orderStatus= String.valueOf(orderDto.getOrderStatus());
            this.jdbcTemplate.update(sql, orderDto.getAmount(), orderDto.getTotalAmount(), orderDto.getDiscount(),
                    orderDto.getUserId(), orderStatus);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return;
    }

    public OrderDto findByUserIdAndStatus(Long userId, String orderStatus) {
        OrderDto orderDto=new OrderDto();
        String sql="select * from orders where userid=? and orderstatus=?";
        orderDto=this.jdbcTemplate.queryForObject(sql, new RowMapper<OrderDto>() {
            @Override
            public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderDto orderDto1=new OrderDto();
                orderDto1.setId(rs.getLong(1));
                orderDto1.setAmount(rs.getLong(4));
                orderDto1.setTotalAmount(rs.getLong(8));
                orderDto1.setDiscount(rs.getLong(9));
                orderDto1.setUserId(rs.getLong(11));
                String orderStatus=rs.getString(7);
                if(orderStatus!=null) {
                    orderDto1.setOrderStatus(OrderStatus.valueOf(orderStatus));
                }
                orderDto1.setCouponCode(rs.getString(13));

                return orderDto1;
            }
        }, new Object[]{userId, orderStatus});
                return orderDto;
    }

    public Optional<OrderDto> findById(Long orderId) {
            String sql = "select * from orders where id=?";

        OrderDto orderDto = this.jdbcTemplate.queryForObject(sql, new RowMapper<OrderDto>() {
                @Override
                public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    OrderDto orderDto1=new OrderDto();
                    orderDto1.setId(rs.getLong(1));
                    orderDto1.setAmount(rs.getLong(4));
                    orderDto1.setTotalAmount(rs.getLong(8));
                    orderDto1.setDiscount(rs.getLong(9));
                    orderDto1.setUserId(rs.getLong(11));
                    String orderStatus=rs.getString(7);
                    if(orderStatus!=null) {
                        orderDto1.setOrderStatus(OrderStatus.valueOf(orderStatus));
                    }
                    orderDto1.setCouponCode(rs.getString(13));

                    return orderDto1;
                }
            }, orderId);

            return Optional.ofNullable(orderDto);

    }

    public void updateTotalAmountandAmountandCartItemsById(Long totalAmount, Long amount, Long cartItemsId, Long orderId) {
        String sql="update orders set totalamount=?,amount=?,cartitemsid=? where id=?";
        this.jdbcTemplate.update(sql,totalAmount,amount,cartItemsId,orderId);
    }

    public void updateAmountandDiscountandCouponnameById(OrderDto activeOrderDto, String code) {
        String sql="update orders set amount=?,discount=?,couponname=? where id=?";
        this.jdbcTemplate.update(sql,activeOrderDto.getAmount(),activeOrderDto.getDiscount(),code,activeOrderDto.getId());
    }

    public void updateAmountandTotalAmountandDiscountById(OrderDto activeOrderDto) {
        String sql="update orders set amount=?,totalamount=?,discount=? where id=?";
        this.jdbcTemplate.update(sql,activeOrderDto.getAmount(),activeOrderDto.getTotalAmount(),activeOrderDto.getDiscount(),activeOrderDto.getId());
    }

    public void updateAddressandDescriptionandStatusandDateandTrackingId(OrderDto activeOrderDto) {
        String sql="update orders set address=?,orderdescription=?,date=?,orderstatus=?,trackingid=? where id=?";
        this.jdbcTemplate.update(sql,activeOrderDto.getAddress(),activeOrderDto.getOrderDescription(),activeOrderDto.getDate(),
                activeOrderDto.getOrderStatus().toString(),activeOrderDto.getTrackingId(),activeOrderDto.getId());
    }

    public List<OrderWrapper> getAllPlacedOrders() {
        List<OrderWrapper> list=new ArrayList<>();
        String sql="select * from orders where orderstatus not in ('Pending')";

        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql);
        for(Map<String,Object> row:rows){
            OrderWrapper orderWrapper=new OrderWrapper();
            orderWrapper.setId((Long) row.get("id"));
            orderWrapper.setOrderDescription((String) row.get("orderdescription"));
            orderWrapper.setDate((Date) row.get("date"));
            orderWrapper.setAmount((Long) row.get("amount"));
            orderWrapper.setAddress((String) row.get("address"));

            String statusString = (String) row.get("orderstatus");
            OrderStatus status = OrderStatus.valueOf(statusString);
            orderWrapper.setOrderStatus(status);

            orderWrapper.setTotalAmount((Long) row.get("totalamount"));
            orderWrapper.setDiscount((Long) row.get("discount"));
            orderWrapper.setTrackingId((UUID) row.get("trackingid"));
            orderWrapper.setUserId((Long) row.get("userid"));

            Optional<User> optionalUser=userDaoImpl.findById(orderWrapper.getUserId());

            orderWrapper.setUserName(optionalUser.get().getName());

            list.add(orderWrapper);
        }
        return list;
    }


    public void changeOrderStatusById(Long orderId, String status) {
        String sql="update orders set orderstatus=? where id=?";
        this.jdbcTemplate.update(sql,status,orderId);

    }

    public List<OrderWrapper> getMyPlacedOrders(Long userId) {
        List<OrderWrapper> list=new ArrayList<>();
        String sql="select * from orders where orderstatus not in ('Pending') and userid=?";

        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql,userId);
        for(Map<String,Object> row:rows){
            OrderWrapper orderWrapper=new OrderWrapper();
            orderWrapper.setId((Long) row.get("id"));
            orderWrapper.setOrderDescription((String) row.get("orderdescription"));
            orderWrapper.setDate((Date) row.get("date"));
            orderWrapper.setAmount((Long) row.get("amount"));
            orderWrapper.setAddress((String) row.get("address"));

            String statusString = (String) row.get("orderstatus");
            OrderStatus status = OrderStatus.valueOf(statusString);
            orderWrapper.setOrderStatus(status);

            orderWrapper.setTotalAmount((Long) row.get("totalamount"));
            orderWrapper.setDiscount((Long) row.get("discount"));
            orderWrapper.setTrackingId((UUID) row.get("trackingid"));
            orderWrapper.setUserId((Long) row.get("userid"));

            Optional<User> optionalUser=userDaoImpl.findById(orderWrapper.getUserId());

            orderWrapper.setUserName(optionalUser.get().getName());

            list.add(orderWrapper);
        }
        return list;
    }

    public Optional<OrderDto> findOrderByTrackingId(UUID trackingId) {
        String sql = "select * from orders where trackingid=?";

        OrderDto orderDto = this.jdbcTemplate.queryForObject(sql, new RowMapper<OrderDto>() {
            @Override
            public OrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                OrderDto orderDto1=new OrderDto();
                orderDto1.setId(rs.getLong(1));
                orderDto1.setAmount(rs.getLong(4));
                orderDto1.setTotalAmount(rs.getLong(8));
                orderDto1.setDiscount(rs.getLong(9));
                orderDto1.setUserId(rs.getLong(11));
                String orderStatus=rs.getString(7);
                if(orderStatus!=null) {
                    orderDto1.setOrderStatus(OrderStatus.valueOf(orderStatus));
                }
                orderDto1.setCouponCode(rs.getString(13));
                orderDto1.setTrackingId(trackingId);
                orderDto1.setAddress(rs.getString(5));
                orderDto1.setDate(rs.getDate(3));

                return orderDto1;
            }
        }, trackingId);

        return Optional.ofNullable(orderDto);

    }

    public List<OrderWrapper> findByDateBetweenAndStatus(Date startOfMonth, Date endOfMonth, String delivered) {
        List<OrderWrapper> list=new ArrayList<>();
        String sql="select * from orders where date between ? and ? and orderstatus=?";

        List<Map<String,Object>> rows=this.jdbcTemplate.queryForList(sql,startOfMonth,endOfMonth,delivered);
        for(Map<String,Object> row:rows){
            OrderWrapper orderWrapper=new OrderWrapper();
            orderWrapper.setId((Long) row.get("id"));
            orderWrapper.setOrderDescription((String) row.get("orderdescription"));
            orderWrapper.setDate((Date) row.get("date"));
            orderWrapper.setAmount((Long) row.get("amount"));
            orderWrapper.setAddress((String) row.get("address"));

            String statusString = (String) row.get("orderstatus");
            OrderStatus status = OrderStatus.valueOf(statusString);
            orderWrapper.setOrderStatus(status);

            orderWrapper.setTotalAmount((Long) row.get("totalamount"));
            orderWrapper.setDiscount((Long) row.get("discount"));
            orderWrapper.setTrackingId((UUID) row.get("trackingid"));
            orderWrapper.setUserId((Long) row.get("userid"));

            Optional<User> optionalUser=userDaoImpl.findById(orderWrapper.getUserId());

            orderWrapper.setUserName(optionalUser.get().getName());

            list.add(orderWrapper);
        }
        return list;
    }

    public Long countByOrderStatus(String status) {
        String sql="select count(id) from orders where orderstatus=?";

        return this.jdbcTemplate.queryForObject(sql,Long.class,status);
    }
}
