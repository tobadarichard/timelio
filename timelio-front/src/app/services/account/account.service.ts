import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private httpClient: HttpClient) { }

  askResetMdp(email: string): Observable<any> {
    return this.httpClient.post(environment.url + "/account/reset-password", { email: email });
  }

  resetMdpByToken(token: string, mdp: string): Observable<string> {
    return this.httpClient
      .post(environment.url + '/account/reset-password/' + token, { mdp: mdp }, { responseType: 'text' });
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
}
