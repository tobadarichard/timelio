import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { EmploiService } from 'src/app/services/emploi/emploi.service';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { CalendarCell } from 'src/app/model/calendar-cell';
import { EvenementSummary } from 'src/app/model/evenements/evenement-summary';
import { ParsedEvenement } from 'src/app/model/evenements/parsed-evenement';
import { toParsed } from 'src/app/utils/utils';
import * as dayjs from 'dayjs';
import * as duration from 'dayjs/plugin/duration';
import 'dayjs/locale/fr';
import { ChangeOnEvent } from './events/change-on-event';
import { EventAction } from './events/event-action';
import { MatSnackBar } from '@angular/material/snack-bar';


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
  selectedEvent: ParsedEvenement | null = null;
  selectedCell: CalendarCell | null = null;

  days = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche'];
  startingDay = dayjs();
  endingDay = dayjs();
  calendarPage: CalendarCell[][] = [[]];
  type = 'month';
  currentSelection = '';
  currentMonth = 1;

  constructor(private route: ActivatedRoute, private emploiService: EmploiService,
    private snackbar: MatSnackBar) { }

  ngOnInit(): void {
    var code = this.route.snapshot.paramMap.get('code') || '';
    var id = Number(this.route.snapshot.paramMap.get('id')) || 0;
    this.isUserEmploi = code == '';

    (this.isUserEmploi ? this.emploiService.getUserEmploi(id) : this.emploiService.getEmploi(code))
      .subscribe((emploi) => {
        this.evenements = emploi.evenements.map((event) => toParsed(event));
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
      dateDebut = dateDebut.add(mult * periode, 'second');
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
      dateDebut = dateDebut.add(periode, 'second');
      dateFin = dateFin.add(periode, 'second');
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

  showEventDetails(id: number) {
    this.selectedEvent = this.evenements.find((e) => e.id == id) || null;
  }

  getPrefix(): string {
    if (!this.emploi) {
      return '';
    }
    return this.emploi.publique ? '/emplois/' + this.emploi.codeAcces : '/user/emplois/' + this.emploi.id;
  }

  changeOnEvent(change: ChangeOnEvent) {
    switch (change.type) {
      case EventAction.CREATED:
        this.evenements.push(change.value);
        this.snackbar.open('Evenement crée');
        break;
      case EventAction.DELETED:
        this.evenements = this.evenements.filter((e) => e.id != change.value.id);
        this.snackbar.open('Evenement supprimé');
        break;
      case EventAction.UPDATED:
        let index = this.evenements.findIndex((e) => e.id == change.value.id);
        this.evenements[index] = change.value;
        this.snackbar.open('Evenement modifié');
        break;
    }
    this.putCalendarEvents();
  }
}
