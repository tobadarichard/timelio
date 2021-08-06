import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Evenement } from 'src/app/model/evenements/evenement';
import { ParsedEvenement } from 'src/app/model/evenements/parsed-evenement';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EvenementService {

  constructor(private httpClient: HttpClient) { }

  updateEvent(prefix: string, event: ParsedEvenement): Observable<Evenement> {
    return this.httpClient.put<Evenement>(environment.url + prefix + '/events/' + event.id,
      {
        id: event.id,
        dateDebut: event.dateDebut.toISOString(),
        duree: event.duree.toISOString(),
        description: event.description,
        couleur: event.couleur,
        periodique: event.periodique,
        periode: event.periode.toISOString()
      });
  }

  deleteEvent(prefix: string, id: number): Observable<string> {
    return this.httpClient.delete(environment.url + prefix + '/events/' + id,
      { responseType: 'text' });
  }
}
