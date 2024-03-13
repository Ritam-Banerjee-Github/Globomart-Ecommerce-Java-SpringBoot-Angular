import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute } from '@angular/router';
import { AdminService } from 'src/app/admin/service/admin.service';
import { CustomerService } from '../services/customer.service';
import { UserStorageService } from 'src/app/storage/user-storage.service';

@Component({
  selector: 'app-review-ordered-product',
  templateUrl: './review-ordered-product.component.html',
  styleUrls: ['./review-ordered-product.component.css']
})
export class ReviewOrderedProductComponent implements OnInit {

  productId:number=this.activatedRoute.snapshot.params["productId"]

  reviewForm:FormGroup

  selectedFile:File | null
  imagePreview: String | ArrayBuffer | null

  constructor(private fb:FormBuilder,
    private router:Router,
    private snackBar:MatSnackBar,
    private customerService:CustomerService,
    private activatedRoute:ActivatedRoute) { }

  ngOnInit(): void {
    this.reviewForm=this.fb.group({
      rating:[null,[Validators.required]],
      description:[null,[Validators.required]]
    })
  }

  onFileSelected(event:any){
    this.selectedFile=event.target.files[0]
    this.previewImage()
 }

 previewImage(){
   const reader=new FileReader()
  
   reader.onload = () =>{
     this.imagePreview=reader.result
   } 
   reader.readAsDataURL(this.selectedFile)
 }

 submitForm(){
  const formData: FormData=new FormData()
  formData.append('img',this.selectedFile)
  formData.append('productId',this.productId.toString())
  formData.append('userId',UserStorageService.getUserId())
  formData.append('rating',this.reviewForm.get('rating').value)
  formData.append('description',this.reviewForm.get('description').value)

  this.customerService.giveReview(formData).subscribe(res=>{
    if(res.id!=null){
      this.snackBar.open('Review Posted Successfully','Close',{duration: 5000})
      this.router.navigateByUrl("/customer/my_orders")
    }else{
      this.snackBar.open(res.message,'ERROR',{duration: 5000, panelClass: 'error-snackbar'})
    }
  })
 }

}
