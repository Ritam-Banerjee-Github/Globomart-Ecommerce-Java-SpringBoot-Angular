import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute } from '@angular/router';
import { CustomerService } from '../services/customer.service';
import { UserStorageService } from 'src/app/storage/user-storage.service';

@Component({
  selector: 'app-view-product-detail',
  templateUrl: './view-product-detail.component.html',
  styleUrls: ['./view-product-detail.component.css']
})
export class ViewProductDetailComponent implements OnInit {
 productId:number=this.activatedRoute.snapshot.params["productId"]

 product:any;
 FAQS:any[]=[]
 reviews:any[]=[]
 
  constructor(private fb:FormBuilder,
    private router:Router,
    private snackBar:MatSnackBar,
    private customerService:CustomerService,
    private activatedRoute:ActivatedRoute) { }

  ngOnInit(): void {
    this.getProductDetailsById()
  }

  getProductDetailsById(){
    this.customerService.getProductDetailsById(this.productId).subscribe(res=>{
      this.product=res.productWrapper;
      this.product.processedImg='data:image/jpeg;base64,'+res.productWrapper.byteImage;
      
      this.FAQS=res.faqDtoList
 
      res.reviewWrapperList.forEach(element=>{
        element.processedImage='data:image/jpeg;base64,'+element.returnedImg
        this.reviews.push(element)
      })
    })
  }

  addToWishlist(){
    const wishlistDto={
      productId:this.productId,
      userId:UserStorageService.getUserId()
    }

    this.customerService.addProductToWishlist(wishlistDto).subscribe(res=>{
      console.log(res)
      if(res.id!=null){
        this.snackBar.open('Product added to wishlist','Close',{duration: 5000})
      }
    },
    error => {
      console.error('An error occurred:', error);
      this.snackBar.open('Product already added in wishlist','ERROR',{duration: 5000, panelClass: 'error-snackbar'})
    })
  }

}
