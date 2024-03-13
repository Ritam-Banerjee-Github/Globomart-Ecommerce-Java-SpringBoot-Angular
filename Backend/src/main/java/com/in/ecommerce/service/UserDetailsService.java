package com.in.ecommerce.service;

import com.in.ecommerce.dto.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
//    UserDetails loadUserByUsername(String username);

    Optional<User> findByUsernameEmail(String username);
}
