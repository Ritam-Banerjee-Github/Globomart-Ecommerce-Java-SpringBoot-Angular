package com.in.ecommerce.serviceImpl;

import com.in.ecommerce.daoImpl.UserDaoImpl;
import com.in.ecommerce.dto.User;
import com.in.ecommerce.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDaoImpl userDaoImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser=userDaoImpl.findByUsername(username);
        if(optionalUser.isEmpty()) throw new UsernameNotFoundException("Username not found",null);
        return new org.springframework.security.core.userdetails.User(optionalUser.get().getEmail(),optionalUser.get().getPassword(),
                new ArrayList<>());
    }


    @Override
    public Optional<User> findByUsernameEmail(String username) {
        try{
            return userDaoImpl.findByUsername(username);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


}
