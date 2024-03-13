import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-view-ordered-products',
  templateUrl: './view-ordered-products.component.html',
  styleUrls: ['./view-ordered-products.component.css']
})
export class ViewOrderedProductsComponent implements OnInit {
  orderId:number=this.activatedRoute.snapshot.params["orderId"]
  orderedProductDetailsList=[]
  amount:any

  constructor(private activatedRoute:ActivatedRoute,
    private customerService:CustomerService) { }

  ngOnInit(): void {
     this.getOrderedProductDetailsByOrderId()
  }

  getOrderedProductDetailsByOrderId(){
    this.customerService.getOrderedProductsById(this.orderId).subscribe(res=>{
      res.productWrapperList.forEach(element=>{
        element.processedImage='data:image/jpeg;base64,'+element.byteImage
        this.orderedProductDetailsList.push(element)
      })
      this.amount=res.orderAmount
    })
  }

}
