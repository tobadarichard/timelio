import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Observable, Subscriber } from 'rxjs';
import {tap} from 'rxjs/operators';
import { UserInfos } from 'src/app/model/user-infos';
import { environment } from 'src/environments/environment';
import { LoginResponse } from './login-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userInfos: Observable<UserInfos> | null = null;
  constructor(private httpClient: HttpClient,private snackBar: MatSnackBar,
    private router: Router) { }

  isLoggedIn() : boolean {
    return localStorage.getItem("RT") != null;
  }

  unexpectedLogout() {
    localStorage.clear();
    this.snackBar.open('Erreur : vous avez été déconnecté')
    this.router.navigate(['/login'],{queryParams: {error: 'logout'}});
    this.userInfos = null;
  }
  logout(){
    localStorage.clear();
    this.snackBar.open("Au revoir !");
    this.router.navigate(['']);
    this.userInfos = null;
  }

  login(email: string, password: string): Observable<LoginResponse>{
    return this.httpClient.post<LoginResponse>(environment.url+"/account/login",
      {email: email,mdp: password}).pipe(tap((response) => {
        localStorage.setItem("RT",response.refreshToken);
        localStorage.setItem("UI",JSON.stringify(response.userInfos));
      }));
  }

  askResetMdp(email: string): Observable<any>{
    return this.httpClient.post(environment.url+"/account/reset-password",{email: email});
  }

  signUp(email: string, password: string, pseudo: string): Observable<string>{
    return this.httpClient.post(environment.url+'/account/register',{
      email: email, mdp: password, pseudo: pseudo
    },{responseType: 'text'});
  }

  getUserInfos(): Observable<UserInfos>{
    if (this.userInfos != null){ return this.userInfos;}
    this.userInfos = new Observable<UserInfos>((subscriber) => {
      if (!this.isLoggedIn()){
        subscriber.error("Pas connecté");
        subscriber.complete();
        return;
      }
      var infos = localStorage.getItem("UI");
      if (infos != null){
        subscriber.next(JSON.parse(infos) as UserInfos);
        subscriber.complete();
      }
      else{
        this.fetchUserInfos().subscribe((response) => {
          subscriber.next(response);
          subscriber.complete();
        }, () => {
          subscriber.error("Une erreur est survenue");
          subscriber.complete();
        });
      }
    });
    return this.userInfos;
  }

  private fetchUserInfos(): Observable<UserInfos>{
    return this.httpClient.get<UserInfos>(environment.url+"/user/account")
      .pipe(tap((response) => {
        localStorage.setItem("UI",JSON.stringify(response));
      }));
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

