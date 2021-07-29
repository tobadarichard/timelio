import {
  HttpClient, HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from '../services/auth/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService : AuthService, private httpClient : HttpClient) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (!this.authService.isLoggedIn() || !request.url.startsWith(environment.url+'/user/')){
      return next.handle(request);
    }

    var accesToken = this.authService.getAccesToken();
    if (accesToken != null){
      return this.sendRequest(request,next,accesToken,false);
    }
    else{
      return this.getTokenAndSendRequest(request,next);
    }
  }

  getTokenAndSendRequest(request: HttpRequest<unknown>,next: HttpHandler): Observable<HttpEvent<unknown>>{  
    return new Observable<HttpEvent<unknown>>((subscriber) => {
      this.httpClient.post(environment.url+'/account/access-token',
        {token: this.authService.getRefreshToken()},
        {responseType: "text"})
        .subscribe((token) => {
          this.authService.saveAccesToken(token);
          this.sendRequest(request,next,token,true).subscribe(
            (event) => subscriber.next(event),
            (error) => subscriber.error(error));
        },(error) => {
          this.authService.unexpectedLogout();
          subscriber.error(error);
        })
    });
  }

  sendRequest(request: HttpRequest<unknown>,next: HttpHandler, accesToken: string,
    isNewToken: boolean): Observable<HttpEvent<unknown>> {
    var authRequest = request.clone(
      {headers: request.headers.set('Authorization','Bearer '+accesToken)}
      );
    return this.handleResponse(next.handle(authRequest),isNewToken,request,next);
  }

  handleResponse(obs: Observable<HttpEvent<unknown>>,isNewToken: boolean,request: HttpRequest<unknown>, next: HttpHandler)
    : Observable<HttpEvent<unknown>> {
      return new Observable<HttpEvent<unknown>>((subscriber) => {
        obs.subscribe((event) => subscriber.next(event),(error) => {
          if (error instanceof HttpErrorResponse && error.status == 401){
            if (isNewToken){
              subscriber.error(error);
            }
            else {
              this.getTokenAndSendRequest(request,next).subscribe((event) => subscriber.next(event),
                (error) => subscriber.error(error));
            }
          }
        });
      });
  }
}


