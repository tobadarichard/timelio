import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { Page } from 'src/app/model/page';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmploiService {

  constructor(private httpClient: HttpClient) {}

  getUserEmploiPage(page: number): Observable<Page<EmploiTemps>>{
    return this.httpClient.get<Page<EmploiTemps>>(environment.url+'/user/emplois',{params: {'page': page,
  'size': 10,'sort':'nom'}});
  }

  createEmploi(nom: string): Observable<EmploiTemps>{
    return this.httpClient.post<EmploiTemps>(environment.url+'/emplois',{nom: nom});
  }

  createEmploiUser(nom: string, publique: boolean): Observable<EmploiTemps>{
    return this.httpClient.post<EmploiTemps>(environment.url+'/user/emplois',{nom: nom, publique: publique});
  }
}
