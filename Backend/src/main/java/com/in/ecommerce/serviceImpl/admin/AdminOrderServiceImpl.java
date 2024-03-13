package com.in.ecommerce.serviceImpl.admin;

import com.in.ecommerce.daoImpl.OrderDaoImpl;
import com.in.ecommerce.dto.AnalyticsResponseDto;
import com.in.ecommerce.dto.OrderDto;
import com.in.ecommerce.service.admin.AdminOrderService;
import com.in.ecommerce.wrapper.OrderWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {
    @Autowired
    private OrderDaoImpl orderDaoImpl;


    @Override
    public List<OrderWrapper> getAllPlacesOrders() {
        return orderDaoImpl.getAllPlacedOrders();
    }

    @Override
    public OrderWrapper changeOrderStatus(Long orderId, String status) {
        this.orderDaoImpl.changeOrderStatusById(orderId,status);
        return null;

    }

    @Override
    public AnalyticsResponseDto getAnalytics() {
        LocalDate currentDate=LocalDate.now();
        LocalDate previousMonthDate=currentDate.minusMonths(1);

        Long currentMonthOrders=getTotalOrdersForMonth(currentDate.getMonthValue(),currentDate.getYear());
        Long previousMonthOrders=getTotalOrdersForMonth(previousMonthDate.getMonthValue(),previousMonthDate.getYear());

        Long currMonthEarnings=getTotalEarningsForMonth(currentDate.getMonthValue(),currentDate.getYear());
        Long previousMonthEarnings=getTotalEarningsForMonth(previousMonthDate.getMonthValue(),previousMonthDate.getYear());

        Long placed=this.orderDaoImpl.countByOrderStatus("Placed");
        Long shipped=this.orderDaoImpl.countByOrderStatus("Shipped");
        Long delivered=this.orderDaoImpl.countByOrderStatus("Delivered");

        return new AnalyticsResponseDto(placed,shipped,delivered,currentMonthOrders,previousMonthOrders,currMonthEarnings,
                previousMonthEarnings);
    }

    private Long getTotalEarningsForMonth(int monthValue, int year) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,monthValue-1); //Because Month value in calendar starts from 0
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Date startOfMonth=calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);

        Date endOfMonth=calendar.getTime();

        List<OrderWrapper> orderWrappers=this.orderDaoImpl.findByDateBetweenAndStatus(startOfMonth,endOfMonth,"Delivered");

        long sum=0;

        for(OrderWrapper orderWrapper:orderWrappers){
            sum+=orderWrapper.getAmount();
        }

        return sum;
    }

    private Long getTotalOrdersForMonth(int monthValue, int year) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,monthValue-1); //Because Month value in calendar starts from 0
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Date startOfMonth=calendar.getTime();

        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);

        Date endOfMonth=calendar.getTime();

        List<OrderWrapper> orderWrappers=this.orderDaoImpl.findByDateBetweenAndStatus(startOfMonth,endOfMonth,"Delivered");

        return (long) orderWrappers.size();

    }
}
