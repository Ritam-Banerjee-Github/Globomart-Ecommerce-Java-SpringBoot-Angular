import { Component } from '@angular/core';
import { UserStorageService } from './storage/user-storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'GloboMart';

  isCustomerLoggedIn:boolean=UserStorageService.isCustomerLoggedIn()
  isAdminLoggedIn:boolean=UserStorageService.isAdminLoggedIn()

  constructor(private router:Router){

  }

  ngOnInit():void{
     this.router.events.subscribe(event =>{
      this.isAdminLoggedIn=UserStorageService.isAdminLoggedIn()
      this.isCustomerLoggedIn=UserStorageService.isCustomerLoggedIn()
     })
  }

  logout():void{
    console.log("Inside Logout")
    UserStorageService.signOut()
    this.router.navigateByUrl('/login')
  }
}
