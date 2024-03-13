import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserStorageService } from 'src/app/storage/user-storage.service';

const BACKEND_BASEURL="http://localhost:8081/"

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http:HttpClient) { }

  getAllProducts():Observable<any>{
    return this.http.get(BACKEND_BASEURL+"api/customer/product",{
      headers:this.createAuthorizationHeaders(),
    })
  }

  getAllProductsByName(name:any):Observable<any>{
    return this.http.get(BACKEND_BASEURL+`api/customer/search/${name}`,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  addToCart(productId:any):Observable<any>{
    const cartDto={
      productId: productId,
      userId:UserStorageService.getUserId()
    }

    console.log(cartDto)
    return this.http.post(BACKEND_BASEURL+"api/customer/cart",cartDto,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  getCartByUserId():Observable<any>{
    const userId=UserStorageService.getUserId()
    return this.http.get(BACKEND_BASEURL+`api/customer/cart/${userId}`,{
      headers:this.createAuthorizationHeaders(),
    }) 
  }

  applyCoupon(code:any):Observable<any>{
    const userId=UserStorageService.getUserId()
    return this.http.get(BACKEND_BASEURL+`api/customer/coupon/${userId}/${code}`,{
      headers:this.createAuthorizationHeaders(),
    }) 
  }

  increaseQuantity(productId:any):Observable<any>{
    const cartDto={
      productId: productId,
      userId:UserStorageService.getUserId()
    }

    console.log(cartDto)
    return this.http.post(BACKEND_BASEURL+"api/customer/addition",cartDto,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  decreaseQuantity(productId:any):Observable<any>{
    const cartDto={
      productId: productId,
      userId:UserStorageService.getUserId()
    }

    console.log(cartDto)
    return this.http.post(BACKEND_BASEURL+"api/customer/deduction",cartDto,{
      headers:this.createAuthorizationHeaders(),
    })
  }

  placeOrder(placeOrderDto:any):Observable<any>{
    placeOrderDto.userId=UserStorageService.getUserId()

    return this.http.post(BACKEND_BASEURL+"api/customer/placeOrder",placeOrderDto,{
      headers:this.createAuthorizationHeaders(),
    }) 
  }

  getMyOrders():Observable<any>{
    const userId=UserStorageService.getUserId()
    return this.http.get(BACKEND_BASEURL+`api/customer/myOrders/${userId}`,{
      headers:this.createAuthorizationHeaders(),
    }) 
  }

  getOrderedProductsById(orderId:number):Observable<any>{
    return this.http.get(BACKEND_BASEURL+`api/customer/ordered-products/${orderId}`,{
      headers:this.createAuthorizationHeaders()
    })
  }

  giveReview(reviewDto:any):Observable<any>{
  return this.http.post(BACKEND_BASEURL+"api/customer/review",reviewDto,{
    headers:this.createAuthorizationHeaders()
  })
  }

  getProductDetailsById(productId:number):Observable<any>{
    return this.http.get(BACKEND_BASEURL+`api/customer/product/${productId}`,{
      headers:this.createAuthorizationHeaders()
    })
  }

  addProductToWishlist(wishlistDto:any):Observable<any>{
    return this.http.post(BACKEND_BASEURL+"api/customer/wishlist",wishlistDto,{
      headers:this.createAuthorizationHeaders()
    })
  }

  getWshlistByUserId():Observable<any>{
    const userId=UserStorageService.getUserId()
    return this.http.get(BACKEND_BASEURL+`api/customer/wishlist/${userId}`,{
      headers:this.createAuthorizationHeaders()
    })
  }

  private createAuthorizationHeaders():HttpHeaders{
    return new HttpHeaders().set(
      'Authorization','Bearer '+UserStorageService.getToken()
    )
}
}

