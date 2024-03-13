package com.in.ecommerce.controller.admin;

import com.in.ecommerce.dto.FAQDto;
import com.in.ecommerce.dto.ProductDto;
import com.in.ecommerce.service.admin.AdminProductService;
import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminProductController {
    private final AdminProductService adminProductService;

    @PostMapping("/product")
    public ResponseEntity<ProductWrapper> addProduct(@ModelAttribute ProductDto productDto){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(adminProductService.addProduct(productDto));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductWrapper>> getAllProducts(){
        try{
             return ResponseEntity.ok(adminProductService.getAllProducts());
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductWrapper>> getProductByName(@PathVariable String name){
        try{
             return ResponseEntity.ok(adminProductService.getProductByName(name));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        boolean deleted=adminProductService.deleteProductById(productId);
        if(deleted==true){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/faq/{productId}")
    public ResponseEntity<FAQDto> postFAQ(@PathVariable Long productId, @RequestBody FAQDto faqDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(adminProductService.postFAQ(productId,faqDto));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductWrapper> getProductById(@PathVariable Long productId){
        ProductWrapper productWrapper=adminProductService.getProductById(productId);
        if(productWrapper!=null){
            return ResponseEntity.ok(productWrapper);
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductWrapper> updateProduct(@PathVariable Long productId, @ModelAttribute ProductDto productDto) throws IOException {
        ProductWrapper updatedProductWrapper=adminProductService.updateProduct(productId,productDto);
        if(updatedProductWrapper!=null){
            return ResponseEntity.ok(updatedProductWrapper);
        }

        return ResponseEntity.notFound().build();
    }
}
