package com.in.ecommerce.wrapper;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewWrapper {
    private Long id;
    private Long rating;
    private String description;
    private byte[] returnedImg;
    private Long userId;
    private Long productId;
    private String userName;
}
