import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, ActivatedRoute } from '@angular/router';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-view-wishlist',
  templateUrl: './view-wishlist.component.html',
  styleUrls: ['./view-wishlist.component.css']
})
export class ViewWishlistComponent implements OnInit {
  products:any[]=[]

  constructor(private fb:FormBuilder,
    private router:Router,
    private snackBar:MatSnackBar,
    private customerService:CustomerService,
    private activatedRoute:ActivatedRoute) { }

  ngOnInit(): void {
    this.getWishlistByUSerId()
  }

  getWishlistByUSerId(){
    this.customerService.getWshlistByUserId().subscribe(res=>{
      res.forEach(element=>{
        element.processedImg='data:image/jpeg;base64,'+element.returnedImg
        this.products.push(element)
      })
    })
  }

}
