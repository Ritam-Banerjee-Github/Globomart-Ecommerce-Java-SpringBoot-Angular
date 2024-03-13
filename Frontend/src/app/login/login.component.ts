import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { UserStorageService } from '../storage/user-storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  hidePassword=true

  constructor(private fb:FormBuilder,
    private snackbar:MatSnackBar,
    private authService:AuthService,
    private router:Router) { }

  ngOnInit(): void {
    this.loginForm=this.fb.group({
      email:[null,[Validators.required]],
      password:[null,[Validators.required]]
    })
  }

  togglePasswordVisibility(){
    this.hidePassword!=this.hidePassword
  }

   onSubmit():void{
    const username=this.loginForm.get('email').value
    const password=this.loginForm.get('password').value

    this.authService.login(username,password).subscribe(
      (response)=>{
        if(UserStorageService.isAdminLoggedIn()){
          this.router.navigateByUrl('admin/dashboard')
        }else if(UserStorageService.isCustomerLoggedIn()){
          this.router.navigateByUrl('customer/dashboard')
        }
      },
      (error)=>{
        this.snackbar.open('Bad Credentials','Error',{duration: 5000})
      }
    )
   }



}
