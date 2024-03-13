import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../service/admin.service';

@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html',
  styleUrls: ['./update-product.component.css']
})
export class UpdateProductComponent implements OnInit {
  productId:number=this.activatedRoute.snapshot.params["productId"]
  isImageUploaded:boolean=false

  productForm:FormGroup
  listOfCategories:any=[]
  selectedFile:File | null
  imagePreview: String | ArrayBuffer | null

  existingImage:string|null=null

  constructor(private fb:FormBuilder,
    private router:Router,
    private snackBar:MatSnackBar,
    private adminService:AdminService,
    private activatedRoute:ActivatedRoute) { }

  onFileSelected(event:any){
     this.selectedFile=event.target.files[0]
     this.isImageUploaded=true
     this.previewImage()

     this.existingImage=null
  }

  previewImage(){
    const reader=new FileReader()
    reader.readAsDataURL(this.selectedFile)
    reader.onload = () =>{
      this.imagePreview=reader.result
    } 
  }

  ngOnInit(): void {
    this.productForm=this.fb.group({
      categoryId:[null,[Validators.required]],
      name:[null,[Validators.required]],
      description:[null,[Validators.required]],
      price:[null,[Validators.required]]
    })
     this.getAllCategories()
     this.getProductById()

  }

  getAllCategories(){
   this.adminService.getAllCategories().subscribe(res=>{
    this.listOfCategories=res
   })
  }

  updateProduct():void{
    console.log("Inside Add Product")
    if(this.productForm.valid){
        console.log("Inside Form Valid")
         const formData:FormData=new FormData()

         if(this.isImageUploaded==true && this.selectedFile!=null){
          formData.append('image',this.selectedFile)
         }
         
         formData.append('categoryId',this.productForm.get('categoryId').value)
         formData.append('name',this.productForm.get('name').value)
         formData.append('description',this.productForm.get('description').value)
         formData.append('price',this.productForm.get('price').value)

         this.adminService.updateProductById(this.productId,formData).subscribe(res=>{
          if(res.id!=null){
            this.snackBar.open('Product Updated Successfully','Close',{duration: 5000})
            this.router.navigateByUrl("/admin/dashboard")
          }else{
            this.snackBar.open(res.message,'ERROR',{duration: 5000, panelClass: 'error-snackbar'})
          }
         })
    }else{
      console.log("Inside Form Not Valid")
      console.log("Values are -> "+this.productForm.get('categoryId').value,this.productForm.get('name').value,
      this.productForm.get('description').value,this.productForm.get('price').value)
      for(const i in this.productForm.controls){
        this.productForm.controls[i].markAsDirty()
        this.productForm.controls[i].updateValueAndValidity()
      }
    }
  }

  getProductById(){
    this.adminService.getProductById(this.productId).subscribe(res=>{
      this.productForm.patchValue(res)
      this.existingImage='data:image/jpeg;base64,'+res.byteImage
    })
  }
  

}
