import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-order-by-status',
  templateUrl: './order-by-status.component.html',
  styleUrls: ['./order-by-status.component.css']
})
export class OrderByStatusComponent implements OnInit {

  @Input() data:any
  
  constructor() { }

  ngOnInit(): void {
  }

}
