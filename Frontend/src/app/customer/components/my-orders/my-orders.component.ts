import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrls: ['./my-orders.component.css']
})
export class MyOrdersComponent implements OnInit {
 myOrders:any

  constructor(private customerService: CustomerService,
    private fb:FormBuilder,
    private snackBar:MatSnackBar,
    private router:Router,
    private dialog:MatDialog) { }

  ngOnInit(): void {
    this.getMyPlacedOrders();
  }

  getMyPlacedOrders(){
    this.customerService.getMyOrders().subscribe(res=>{
      this.myOrders=res;
    })
  }

}
