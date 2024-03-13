package com.in.ecommerce.serviceImpl;

import com.in.ecommerce.daoImpl.OrderDaoImpl;
import com.in.ecommerce.daoImpl.UserDaoImpl;
import com.in.ecommerce.dto.OrderDto;
import com.in.ecommerce.dto.SignupRequest;
import com.in.ecommerce.dto.User;
import com.in.ecommerce.enums.OrderStatus;
import com.in.ecommerce.enums.UserRole;
import com.in.ecommerce.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserDaoImpl userDaoImpl;
    @Autowired
    private OrderDaoImpl orderDaoImpl;

    @Override
    public boolean hasUserWithEmail(String email) {
        return (userDaoImpl.findByUsername(email)==null)?false:true;
    }

    @Override
    public User createUser(SignupRequest signupRequest) {
        User user = null;
        try {
            user = new User();
            user.setName(signupRequest.getName());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
            user.setRole(UserRole.CUSTOMER);


            int insertedRow=userDaoImpl.createUser(user);
            log.info("New user inserted count -> "+insertedRow);

            Optional<User> optionalUser = userDaoImpl.findByUsername(signupRequest.getEmail());

            user.setId(optionalUser.get().getId());

            OrderDto orderDto=new OrderDto();
            orderDto.setAmount(0l);
            orderDto.setTotalAmount(0l);
            orderDto.setDiscount(0L);
            orderDto.setUserId(user.getId());
            orderDto.setOrderStatus(OrderStatus.Pending);

            this.orderDaoImpl.createOrderForNewUser(orderDto);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return user;
    }
}
