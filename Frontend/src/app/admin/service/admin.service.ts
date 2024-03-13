import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserStorageService } from 'src/app/storage/user-storage.service';

const BACKEND_BASEURL="http://localhost:8081/"

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http:HttpClient) { }

  addCategory(categoryDto:any){
    return this.http.post(BACKEND_BASEURL+"api/admin/category",categoryDto,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  getAllCategories():Observable<any>{
    return this.http.get(BACKEND_BASEURL+"api/admin",{
      headers:this.createAuthorizationHeaders(),
    })
  }

  addProduct(productDto:any):Observable<any>{
    return this.http.post(BACKEND_BASEURL+"api/admin/product",productDto,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  updateProductById(productId:number,productDto:any):Observable<any>{
    return this.http.put(BACKEND_BASEURL+`api/admin/product/${productId}`,productDto,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  getAllProducts():Observable<any>{
    return this.http.get(BACKEND_BASEURL+"api/admin/product",{
      headers:this.createAuthorizationHeaders(),
    })
  }

  getAllProductsByName(name:any):Observable<any>{
    return this.http.get(BACKEND_BASEURL+`api/admin/search/${name}`,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  deleteProductById(productId:any):Observable<any>{
    return this.http.delete(BACKEND_BASEURL+`api/admin/product/${productId}`,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  createCoupon(couponDto:any):Observable<any>{
    return this.http.post(BACKEND_BASEURL+"api/admin/coupons",couponDto,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  getCoupons():Observable<any>{
    return this.http.get(BACKEND_BASEURL+"api/admin/coupons",{
      headers:this.createAuthorizationHeaders(),
    })
  }

  getPlacedOrders():Observable<any>{
    return this.http.get(BACKEND_BASEURL+"api/admin/placedOrders",{
      headers:this.createAuthorizationHeaders(),
    })
  }

  changeOrderStatus(orderId:number,status:string):Observable<any>{
    return this.http.get(BACKEND_BASEURL+`api/admin/order/${orderId}/${status}`,{
      headers:this.createAuthorizationHeaders()
    })
  }

  postProdutFaq(productId:number,faqDto:any):Observable<any>{
    return this.http.post(BACKEND_BASEURL+`api/admin/faq/${productId}`,faqDto,{
      headers:this.createAuthorizationHeaders()
    })
  }

  getProductById(productId:number):Observable<any>{
    return this.http.get(BACKEND_BASEURL+`api/admin/product/${productId}`,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  getAnalytics():Observable<any>{
    return this.http.get(BACKEND_BASEURL+"api/admin/order/analytics",{
      headers:this.createAuthorizationHeaders()
    })
  }

  private createAuthorizationHeaders():HttpHeaders{
        return new HttpHeaders().set(
          'Authorization','Bearer '+UserStorageService.getToken()
        )
  }
}
