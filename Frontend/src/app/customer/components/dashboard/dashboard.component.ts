import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AdminService } from 'src/app/admin/service/admin.service';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  products:any=[]
  searchProductForm:FormGroup
  
  constructor(private customerService: CustomerService,
    private fb:FormBuilder,
    private snackBar:MatSnackBar,
    private router:Router ) { }

  ngOnInit(): void {
    this.getAllProducts()
    this.searchProductForm=this.fb.group({
      title:[null,[Validators.required]]
    })
  }


  getAllProducts(){
    this.products=[]
    this.customerService.getAllProducts().subscribe(res=>{
     res.forEach(element => {
        element.processedImage='data:image/jpeg;base64,'+element.byteImage
        this.products.push(element)
     });
     console.log(this.products)
    })
  }

  submitForm(){
    this.products=[]
    const title=this.searchProductForm.get('title').value;
    this.customerService.getAllProductsByName(title).subscribe(res=>{
     res.forEach(element => {
        element.processedImage='data:image/jpeg;base64,'+element.byteImage
        this.products.push(element)
     });
     console.log(this.products)
    })
  }

  addToCart(id:any){
    console.log("User Id is ->"+id);
    
    this.customerService.addToCart(id).subscribe(res=>{
      this.snackBar.open("Product added to cart successfully","Close",{duration:5000})
    })
  }

}
