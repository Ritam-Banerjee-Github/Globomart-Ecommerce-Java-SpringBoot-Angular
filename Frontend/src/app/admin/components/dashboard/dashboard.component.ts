import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { FormBuilder, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  products:any=[]
  searchProductForm:FormGroup

  constructor(private adminService: AdminService,
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
    this.adminService.getAllProducts().subscribe(res=>{
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
    this.adminService.getAllProductsByName(title).subscribe(res=>{
     res.forEach(element => {
        element.processedImage='data:image/jpeg;base64,'+element.byteImage
        this.products.push(element)
     });
     console.log(this.products)
    })
  }

  deleteProductById(productId:any){
    this.adminService.deleteProductById(productId).subscribe(res=>{
      if(res===null){
        this.snackBar.open('Product Deleted Successfully','Close',{duration: 5000})
        this.getAllProducts()
      }else{
        this.snackBar.open(res.message,'Close',{duration: 5000, panelClass: 'error-snackbar'})
      }
    })
  }

}
