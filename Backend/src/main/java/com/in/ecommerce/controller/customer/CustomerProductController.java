package com.in.ecommerce.controller.customer;

import com.in.ecommerce.dto.ProductDetailsDto;
import com.in.ecommerce.service.customer.CustomerProductService;
import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/customer")
public class CustomerProductController {
    @Autowired
    private CustomerProductService customerProductService;

    @GetMapping("/product")
    public ResponseEntity<List<ProductWrapper>> getAllProducts(){
        try{
            return ResponseEntity.ok(customerProductService.getAllProducts());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductWrapper>> getProductByName(@PathVariable String name){
        try{
            return ResponseEntity.ok(customerProductService.getProductByName(name));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDetailsDto> getProductDetailsById(@PathVariable Long productId){
        ProductDetailsDto productDetailsDto=this.customerProductService.getProductDetailsById(productId);

        if(productDetailsDto==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productDetailsDto);
    }
}
