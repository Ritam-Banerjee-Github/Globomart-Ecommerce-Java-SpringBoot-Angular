import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {map} from 'rxjs/operators'
import { UserStorageService } from '../storage/user-storage.service';


const BACKEND_BASEURL="http://localhost:8081/"

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  constructor(private http:HttpClient,
    private userStorageService:UserStorageService) { }

  register(signupRequest:any): Observable<any>{
    return this.http.post(BACKEND_BASEURL+"sign-up",signupRequest)
  }

  login(username: any, password: any) {
    const headers=new HttpHeaders().set('Content-type','application/json')
    const body={username,password}

    return this.http.post(BACKEND_BASEURL+'authenticate',body,{headers, observe:'response'}).pipe(
      map((res) =>{
        const token=res.headers.get('authorization').substring(7);
        const user=res.body
        console.log("Response Returned")
        if(token && user){
            this.userStorageService.saveToken(token)
            this.userStorageService.saveUser(user)
            return true;
        }
       return false;
      })
    ) 
  }

  getOrderByTrackingId(trackinId:number):Observable<any>{
    return this.http.get(BACKEND_BASEURL+`order/${trackinId}`)
  }
}
