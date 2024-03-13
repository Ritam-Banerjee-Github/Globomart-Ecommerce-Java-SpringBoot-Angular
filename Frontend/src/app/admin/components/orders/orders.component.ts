import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AdminService } from '../../service/admin.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
 orders:any
 response:any

  constructor(private fb:FormBuilder,
    private router:Router,
    private snackBar:MatSnackBar,
    private adminService:AdminService) { }

  ngOnInit(): void {
    this.getPlacedOrders()
  }

  getPlacedOrders(){
    this.adminService.getPlacedOrders().subscribe(res=>{
      this.orders=res;
    })
  }

  changeOrderStatus(orderId:number,status:string){
    this.adminService.changeOrderStatus(orderId,status).subscribe(res=>{
      this.response=res
      this.getPlacedOrders()
    })

  }

}
