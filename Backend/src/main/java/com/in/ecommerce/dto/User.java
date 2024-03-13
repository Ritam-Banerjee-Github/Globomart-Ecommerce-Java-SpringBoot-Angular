package com.in.ecommerce.dto;

import com.in.ecommerce.enums.UserRole;
import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private byte[] image;
}
