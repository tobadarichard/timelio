import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { Page } from 'src/app/model/page';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmploiService {

  constructor(private httpClient: HttpClient) { }

  getUserEmploiPage(page: number): Observable<Page<EmploiTemps>> {
    return this.httpClient.get<Page<EmploiTemps>>(environment.url + '/user/emplois', {
      params: {
        'page': page,
        'size': 10, 'sort': 'nom'
      }
    });
  }

  createEmploi(nom: string): Observable<EmploiTemps> {
    return this.httpClient.post<EmploiTemps>(environment.url + '/emplois', { nom: nom });
  }

  createUserEmploi(nom: string, publique: boolean): Observable<EmploiTemps> {
    return this.httpClient.post<EmploiTemps>(environment.url + '/user/emplois', { nom: nom, publique: publique });
  }

  getEmploi(code: String): Observable<EmploiTemps> {
    return this.httpClient.get<EmploiTemps>(environment.url + '/emplois/' + code);
  }

  getUserEmploi(id: number): Observable<EmploiTemps> {
    return this.httpClient.get<EmploiTemps>(environment.url + '/user/emplois/' + id);
  }

  deleteEmploi(code: String): Observable<string> {
    return this.httpClient.delete(environment.url + '/emplois/' + code, { responseType: 'text' });
  }

  deleteUserEmploi(id: number): Observable<string> {
    return this.httpClient.delete(environment.url + '/user/emplois/' + id, { responseType: 'text' });
  }

  updateEmploi(code: String, nom: string): Observable<EmploiTemps> {
    return this.httpClient.put<EmploiTemps>(environment.url + '/emplois/' + code, { nom: nom });
  }

  updateUserEmploi(id: number, nom: string, publique: boolean): Observable<EmploiTemps> {
    return this.httpClient.put<EmploiTemps>(environment.url + '/user/emplois/' + id,
      { nom: nom, publique: publique });
  }

  downloadUserEmploi(code: String): Observable<HttpResponse<Blob>> {
    return this.httpClient.get(environment.url + '/emplois/' + code + '/download',
    { responseType: 'blob',observe: 'response' });
  }

  downloadEmploi(id: number): Observable<HttpResponse<Blob>> {
    return this.httpClient.get(environment.url + '/user/emplois/' + id + 'download',
      { responseType: 'blob',observe: 'response' });
  }


}
