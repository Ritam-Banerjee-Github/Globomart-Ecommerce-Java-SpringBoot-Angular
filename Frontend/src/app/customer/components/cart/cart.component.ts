import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { CustomerService } from '../services/customer.service';
import { MatDialog } from '@angular/material/dialog';
import { PlaceOrderComponent } from '../place-order/place-order.component';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {

  cartItems:any[]=[]
  order:any

  couponForm:FormGroup;

  constructor(private customerService: CustomerService,
    private fb:FormBuilder,
    private snackBar:MatSnackBar,
    private router:Router,
    private dialog:MatDialog) { }

  ngOnInit(): void {
    this.couponForm=this.fb.group({
      code:[null,[Validators.required]]
    })
      this.getCart()
  }

  applyCoupon(){
    this.customerService.applyCoupon(this.couponForm.get(['code'])!.value).subscribe(res=>{
      this.snackBar.open('Coupon Applied Successfully','Close',{duration: 5000})
      this.getCart()
    },error=>{
      this.snackBar.open(error.error,'Close',{duration: 5000})
    })
  }

  getCart(){
    this.cartItems=[]
    console.log("Inside getCart()")
    this.customerService.getCartByUserId().subscribe(res=>{
      this.order=res;
      res.cartItemsWrappers.forEach(element =>{
        element.processedImg='data:image/jpeg;base64,'+element.returnedImg
        this.cartItems.push(element)
      })
    })
  }

  increaseQuantity(productId:any){
    this.customerService.increaseQuantity(productId).subscribe(res=>{
      this.snackBar.open("Product quantity increased successfully","Close",{duration:5000})
      this.getCart()
    })
  }

  decreaseQuantity(productId:any){
    this.customerService.decreaseQuantity(productId).subscribe(res=>{
      this.snackBar.open("Product quantity deducted successfully","Close",{duration:5000})
      this.getCart()
    })
  }

  placeOrder(){
    this.dialog.open(PlaceOrderComponent)
  }

}
