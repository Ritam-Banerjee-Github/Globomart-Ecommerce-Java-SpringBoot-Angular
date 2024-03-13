import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../service/admin.service';

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.css']
})
export class AnalyticsComponent implements OnInit {
  data:any
  constructor(private adminService:AdminService) { }

  ngOnInit(): void {
    this.adminService.getAnalytics().subscribe(res=>{
      console.log(res)
      this.data=res
    })
  }

}
