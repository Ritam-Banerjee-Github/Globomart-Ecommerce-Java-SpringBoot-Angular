import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../service/admin.service';

@Component({
  selector: 'app-coupon',
  templateUrl: './coupon.component.html',
  styleUrls: ['./coupon.component.css']
})
export class CouponComponent implements OnInit {
  coupons:any
  constructor(private adminService:AdminService) { }

  ngOnInit(): void {
    this.getCoupons()
  }

  getCoupons():any{
      this.adminService.getCoupons().subscribe(res=>{
        this.coupons=res
      })
  }
}
