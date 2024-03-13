package com.in.ecommerce.wrapper;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class ProductWrapper {
    private Long id;
    private String name;
    private String description;
    private Long price;
    private byte[] byteImage;
    private Long categoryId;
    private String categoryName;
    private Long quantity;
}
