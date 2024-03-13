import { Injectable } from '@angular/core';

const TOKEN="ecom-token"
const USER="ecom-user"

@Injectable({
  providedIn: 'root'
})
export class UserStorageService {

  constructor() { }

  public saveToken(token:string): void{
    window.localStorage.removeItem(TOKEN)
    window.localStorage.setItem(TOKEN,token)
  }
 
  public saveUser(user: any): void{
    window.localStorage.removeItem(USER)
    window.localStorage.setItem(USER,JSON.stringify(user))
  }

  static getToken():string{
    return window.localStorage.getItem(TOKEN)
  }

  static getUser():any{
    return JSON.parse(window.localStorage.getItem(USER))
  }

  static getUserId():string{
    const user=this.getUser()
     if(user==null){
      return ''
     }
    return user.userId
  }
  
  static getUserRole():string{
    const user=this.getUser()
     if(user==null){
      return ''
     }
    return user.role
  }

  static isAdminLoggedIn():boolean{
    if(this.getToken()===null){
      return false;
    }

    const role:string=this.getUserRole();

    return role=='ADMIN'
  }

  static isCustomerLoggedIn():boolean{
    if(this.getToken()===null){
      return false;
    }

    const role:string=this.getUserRole();

    return role=='CUSTOMER'
  }

  static signOut():void{
    console.log("Before removing Token-> "+this.getToken())
    console.log("Before removing UserId -> "+this.getUser().userId)
    window.localStorage.removeItem(TOKEN)
    window.localStorage.removeItem(USER)
    console.log("After removing -> "+this.getToken())
  }

}
