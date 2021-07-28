import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor() { }

  isLoggedIn() : boolean {
    return localStorage.getItem("RT") != null;
  }

  unexpectedLogout() {
    //TODO: snackbar + naviguer vers /login
    localStorage.clear();
    console.log("Unexpected logout");
  }
  logout(){
    localStorage.clear();
  }

  getAccesToken() : string | null {
    return localStorage.getItem("AT");
  }
  saveAccesToken(token: string): void {
    localStorage.setItem("AT",token);
  }
  getRefreshToken() : string | null {
    return localStorage.getItem("RT");
  }
  saveRefreshTolen(token: string): void{
    localStorage.setItem("RT",token);
  }

}
