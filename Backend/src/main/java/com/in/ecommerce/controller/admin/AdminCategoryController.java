package com.in.ecommerce.controller.admin;

import com.in.ecommerce.dto.CategoryDto;
import com.in.ecommerce.service.admin.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminCategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping(path = "/category")
    public ResponseEntity<CategoryDto> createCateory(@RequestBody CategoryDto categoryDto){
        try{
            return  ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDto));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new CategoryDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        try{
              return ResponseEntity.ok(categoryService.getAllCategories());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
