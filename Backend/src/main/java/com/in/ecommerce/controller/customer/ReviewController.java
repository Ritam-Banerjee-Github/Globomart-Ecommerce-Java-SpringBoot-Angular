package com.in.ecommerce.controller.customer;

import com.in.ecommerce.dto.OrderedProductResponseDto;
import com.in.ecommerce.dto.ReviewDto;
import com.in.ecommerce.service.customer.ReviewService;
import com.in.ecommerce.wrapper.ReviewWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/customer")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/ordered-products/{orderId}")
    public ResponseEntity<OrderedProductResponseDto> getOrderedProductsDetailsByOrderId(@PathVariable Long orderId){
        return ResponseEntity.ok(reviewService.getOrderedProuctsDetailsByOrderId(orderId));
    }

    @PostMapping("/review")
    public ResponseEntity<?> giveReview(@ModelAttribute ReviewDto reviewDto) throws IOException {
        ReviewWrapper reviewWrapper=reviewService.giveReview(reviewDto);
        if(reviewWrapper==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).body(reviewWrapper);
    }
}
