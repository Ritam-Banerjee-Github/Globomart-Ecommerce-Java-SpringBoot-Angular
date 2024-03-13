package com.in.ecommerce.service;

import com.in.ecommerce.dto.SignupRequest;
import com.in.ecommerce.dto.User;

public interface AuthService {
    boolean hasUserWithEmail(String email);

    User createUser(SignupRequest signupRequest);
}
