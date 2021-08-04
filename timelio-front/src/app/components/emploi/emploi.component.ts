import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { Evenement } from 'src/app/model/evenement';
import { ParsedEvenement } from 'src/app/model/parsed-evenement';
import { EmploiService } from 'src/app/services/emploi/emploi.service';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { CalendarCell } from 'src/app/model/calendar-cell';
import { EvenementSummary } from 'src/app/model/evenement-summary';
import * as dayjs from 'dayjs';
import * as duration from 'dayjs/plugin/duration';
import 'dayjs/locale/fr';


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
  endingDay = dayjs();
  calendarPage: CalendarCell[][] = [[]];
  type = 'month';
  currentSelection = '';
  currentMonth = 1;

  constructor(private route: ActivatedRoute, private emploiService: EmploiService) {

  }

  ngOnInit(): void {
    var code = this.route.snapshot.paramMap.get('code') || '';
    var id = Number(this.route.snapshot.paramMap.get('id')) || 0;
    this.isUserEmploi = code == '';

    (this.isUserEmploi ? this.emploiService.getUserEmploi(id) : this.emploiService.getEmploi(code))
      .subscribe((emploi) => {
        this.evenements = emploi.evenements.map((event) => this.toParsed(event));
        this.updateCalendar();
        this.emploi = emploi;
      }, () => { this.hasError = true; });
  }

  putCalendarEvents(): void {
    this.calendarPage = [
      [], [], [], [], [], []
    ];

    for (let i = 0; i < 6; i++) {
      for (let j = 0; j < 7; j++) {
        this.calendarPage[i].push({
          date: this.startingDay.add(i * 7 + j, 'day'),
          evenements: []
        });
      }
    }
    let eventsPeriodique = this.evenements.filter((e) => e.periodique);
    let eventsInRange = this.evenements.filter((e) => {
      return e.dateDebut.isBefore(this.endingDay) && e.dateFin.isAfter(this.startingDay) && !e.periodique;
    });

    eventsInRange.forEach((event) => this.addEvent(event));
    eventsPeriodique.forEach((event) => this.addEventPeriodique(event));
  }

  addEvent(event: ParsedEvenement): void {
    let dateDebut = dayjs(event.dateDebut.format('YYYY-MM-DD'));
    let dateFin = dayjs(event.dateFin.format('YYYY-MM-DD'));

    if (dateDebut.format('YYYY-MM-DD') == dateFin.format('YYYY-MM-DD')) {
      this.addInPage(dateDebut, {
        id: event.id,
        description: event.description,
        couleur: event.couleur
      });
    }
    else {
      if (this.isInPage(dateDebut)) {
        this.addInPage(dateDebut, {
          id: event.id,
          description: 'DEBUT : ' + event.description,
          couleur: event.couleur
        });
      }
      if (this.isInPage(dateFin)) {
        this.addInPage(dateFin, {
          id: event.id,
          description: 'FIN : ' + event.description,
          couleur: event.couleur
        });
      }
    }
  }

  addEventPeriodique(event: ParsedEvenement) {
    let dateDebut = dayjs(event.dateDebut.format('YYYY-MM-DD'));
    let dateFin: dayjs.Dayjs;
    let periode = event.periode.asSeconds();

    let distance = this.startingDay.diff(dateDebut) / 1000;
    if (distance > 0) {
      let mult = Math.floor(distance / periode);
      dateDebut = dateDebut.add(mult * periode,'second');
    }
    dateFin = dateDebut.add(event.duree.asSeconds(), 'seconds');

    while (dateDebut.isBefore(this.endingDay)) {
      this.addEvent({
        id: event.id,
        dateDebut: dateDebut,
        dateFin: dateFin,
        duree: event.duree,
        description: event.description,
        couleur: event.couleur,
        periodique: event.periodique,
        periode: event.periode
      });
      dateDebut = dateDebut.add(periode,'second');
      dateFin = dateFin.add(periode,'second');
    }
  }

  isInPage(date: dayjs.Dayjs): boolean {
    return date.isAfter(this.startingDay) && date.isBefore(this.endingDay);
  }

  addInPage(date: dayjs.Dayjs, summary: EvenementSummary): void {
    let difference = dayjs.duration(date.diff(this.startingDay));
    let i = Math.floor(difference.asDays() / 7);
    let j = difference.asDays() % 7;
    this.calendarPage[i][j].evenements.push(summary);
  }

  updateCalendar(): void {
    this.changeCalendarPage();
    this.putCalendarEvents();
  }

  toParsed(event: Evenement): ParsedEvenement {
    var duree = dayjs.duration(event.duree);
    var dateDebut = dayjs(event.dateDebut);

    return {
      id: event.id,
      dateDebut: dateDebut,
      dateFin: dateDebut.add(duree.asSeconds(), 'seconds'),
      duree: duree,
      description: event.description,
      couleur: event.couleur,
      periodique: event.periodique,
      periode: dayjs.duration(event.periode)
    };
  }

  changeCalendarPage(): void {
    this.startingDay = this.startingDay.startOf('month');
    this.currentSelection = this.startingDay.format('MMMM YYYY');
    this.currentMonth = this.startingDay.month();

    if (this.startingDay.day() == 0) {
      this.startingDay = this.startingDay.subtract(6, 'day');
    }
    else {
      this.startingDay = this.startingDay.startOf('week').add(1, 'day');
    }

    this.endingDay = this.startingDay.add(6, 'week').subtract(1, 'day');

    this.startingDay = dayjs(this.startingDay.format('YYYY-MM-DD'));
    this.endingDay = this.endingDay
      .set('hour', 23).set('minute', 59).set('second', 59);
  }

  changeDate(event: MatDatepickerInputEvent<Date>): void {
    this.startingDay = dayjs(event.value);
    this.updateCalendar();
  }

  next(): void {
    if (this.currentMonth == 11) {
      this.startingDay = this.startingDay.add(1, 'year').set('month', 0).set('date', 1);
    }
    else {
      this.startingDay = this.startingDay.set('month', this.currentMonth + 1).set('date', 1);
    }
    this.updateCalendar();
  }

  previous(): void {
    if (this.currentMonth == 0) {
      this.startingDay = this.startingDay.subtract(1, 'year').set('month', 11).set('date', 1);
    }
    else {
      this.startingDay = this.startingDay.set('month', this.currentMonth - 1).set('date', 1);
    }
    this.updateCalendar();
  }
}
