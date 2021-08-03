import { Component, OnInit } from '@angular/core';
import { ActivatedRoute} from '@angular/router';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { Evenement } from 'src/app/model/evenement';
import { ParsedEvenement } from 'src/app/model/parsed-evenement';
import { EmploiService } from 'src/app/services/emploi/emploi.service';
import * as dayjs from 'dayjs';
import * as duration from 'dayjs/plugin/duration';
import 'dayjs/locale/fr';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
dayjs.extend(duration);
dayjs.locale('fr');

@Component({
  selector: 'app-emploi',
  templateUrl: './emploi.component.html',
  styleUrls: ['./emploi.component.css']
})
export class EmploiComponent implements OnInit {
  isUserEmploi = false;
  hasError = false;
  emploi: EmploiTemps | null = null;
  evenements: ParsedEvenement[] = [];

  days = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche'];
  startingDay = dayjs();
  weeks: dayjs.Dayjs[][] = [[]];
  type = 'month';
  currentSelection = '';
  currentMonth = 1;

  constructor(private route: ActivatedRoute, private emploiService: EmploiService) {
      this.changeCalendar();
     }

  ngOnInit(): void {
    var code = this.route.snapshot.paramMap.get('code') || '';
    var id = Number(this.route.snapshot.paramMap.get('id')) || 0;
    this.isUserEmploi = code == '';

    (this.isUserEmploi ? this.emploiService.getUserEmploi(id) : this.emploiService.getEmploi(code))
      .subscribe((emploi) => {
        this.emploi = emploi;
        this.evenements = emploi.evenements.map((event) => this.toParsed(event));

      }, () => { this.hasError = true; });
  }

  toParsed(event: Evenement): ParsedEvenement {
    return {
      id: event.id,
      dateDebut: dayjs(event.dateDebut),
      duree: dayjs.duration(event.duree),
      description: event.description,
      couleur: event.couleur,
      periodique: event.periodique,
      periode: dayjs.duration(event.periode)
    };
  }

  changeCalendar(): void{
    this.weeks = [
      [],[],[],[],[],[]
    ];

    this.startingDay = this.startingDay.startOf('month');
    this.currentSelection = this.startingDay.format('MMMM YYYY');
    this.currentMonth = this.startingDay.month();

    if (this.startingDay.day() == 0){
      this.startingDay = this.startingDay.subtract(6,'day');
    } 
    else{
      this.startingDay = this.startingDay.startOf('week').add(1,'day');
    }
    for (let i = 0; i <6; i++){
      for (let j = 0; j<7; j++){
        this.weeks[i].push(this.startingDay.add(i*7+j,'day'));
      }
    }
  }

  changeDate(event: MatDatepickerInputEvent<Date>): void{
    this.startingDay = dayjs(event.value);
    this.changeCalendar();
  }

  next(): void{
    if (this.currentMonth == 11){
      this.startingDay = this.startingDay.add(1,'year').set('month',0).set('date',1);
    }
    else{
      this.startingDay = this.startingDay.set('month',this.currentMonth+1).set('date',1);
    }
    this.changeCalendar();
  }

  previous(): void{
    if (this.currentMonth == 0){
      this.startingDay = this.startingDay.subtract(1,'year').set('month',11).set('date',1);
    }
    else{
      this.startingDay = this.startingDay.set('month',this.currentMonth-1).set('date',1);
    }
    this.changeCalendar();
  }
}
