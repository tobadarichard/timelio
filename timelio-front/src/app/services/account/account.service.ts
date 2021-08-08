import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { AuthService } from '../auth/auth.service';
import { LoginResponse } from '../auth/login-response';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private httpClient: HttpClient, private authService: AuthService) { }

  askResetMdp(email: string): Observable<any> {
    return this.httpClient.post(environment.url + "/account/reset-password", { email: email });
  }

  resetMdpByToken(token: string, mdp: string): Observable<string> {
    return this.httpClient
      .post(environment.url + '/account/reset-password/' + token, { mdp: mdp }, { responseType: 'text' });
  }

  changeMdp(oldMdp: string, newMdp: string): Observable<LoginResponse> {
    return this.httpClient.put<LoginResponse>(environment.url + '/user/account/password',
      { oldMdp: oldMdp, newMdp: newMdp }).pipe(tap((response) => {
        localStorage.setItem("RT", response.refreshToken);
        localStorage.setItem("UI", JSON.stringify(response.userInfos));
      }));
  }

  signUp(email: string, password: string, pseudo: string): Observable<string> {
    return this.httpClient.post(environment.url + '/account/register', {
      email: email, mdp: password, pseudo: pseudo
    }, { responseType: 'text' });
  }

  verifyAccount(token: string): Observable<string> {
    return this.httpClient.get(environment.url + '/account/verify/' + token, { responseType: 'text' });
  }

  askNewVerification(token: string): Observable<string> {
    return this.httpClient.get(environment.url + '/account/verify/' + token + '/resend', { responseType: 'text' });
  }

  changePseudo(pseudo: string): Observable<string> {
    return this.httpClient.put(environment.url + '/user/account',
      { pseudo: pseudo }, { responseType: 'text' }).pipe(tap(() => {
        this.authService.updateUserInfos(pseudo);
      }));
  }

  deleteAccount(): Observable<string> {
    return this.httpClient.delete(environment.url + '/user/account', { responseType: 'text' }).pipe(tap(() => {
      this.authService.logout();
    }));
  }
}
