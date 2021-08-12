import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { EmploiService } from 'src/app/services/emploi/emploi.service';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { CalendarCell } from 'src/app/model/calendar-cell';
import { toParsed } from 'src/app/utils/utils';
import * as dayjs from 'dayjs';
import * as duration from 'dayjs/plugin/duration';
import 'dayjs/locale/fr';
import { ChangeOnEvent } from './events/change-on-event';
import { EventAction } from './events/event-action';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Calendar } from 'src/app/classes/calendar';
import { CalendarType } from 'src/app/classes/calendar-type';
import { MonthCalendar } from 'src/app/classes/month-calendar';
import { WeekCalendar } from 'src/app/classes/week-calendar';
import { ParsedEvenement } from 'src/app/model/evenements/parsed-evenement';
import { EvenementService } from 'src/app/services/evenement/evenement.service';
import { saveAs } from 'file-saver';

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
  selectedCell: CalendarCell | null = null;

  days = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche'];
  hours = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];
  calendar: Calendar = new MonthCalendar();

  searchResult: ParsedEvenement[] | null = null;
  searchForm = {
    dateDebut: '',
    dateFin: '',
    description: ''
  };

  constructor(private route: ActivatedRoute, private emploiService: EmploiService,
    private snackbar: MatSnackBar, private eventService: EvenementService) { }

  ngOnInit(): void {
    var code = this.route.snapshot.paramMap.get('code') || '';
    var id = Number(this.route.snapshot.paramMap.get('id')) || 0;
    this.isUserEmploi = code == '';

    (this.isUserEmploi ? this.emploiService.getUserEmploi(id) : this.emploiService.getEmploi(code))
      .subscribe((emploi) => {
        this.calendar.evenements = emploi.evenements.map((event) => toParsed(event));
        this.calendar.updateCalendar();
        this.emploi = emploi;
      }, () => { this.hasError = true; });
  }

  changeDate(event: MatDatepickerInputEvent<Date>): void {
    if (event.value != null) {
      this.calendar.changeDate(event.value);
    }
  }

  downloadEmploi(): void {
    if (!this.emploi) {
      return;
    }
    let nom = this.emploi.nom + '.ics';
    (this.isUserEmploi ?
      this.emploiService.downloadEmploi(this.emploi.id)
      : this.emploiService.downloadUserEmploi(this.emploi.codeAcces)).subscribe(
        (response) => {
          if (response.body) {
            saveAs(response.body, nom);
          }
          else {
            this.snackbar.open('Une erreur est survenue');
          }
        }, () => { this.snackbar.open('Une erreur est survenue'); }
      )
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
        this.calendar.evenements.push(change.value);
        this.snackbar.open('Evenement crée');
        break;
      case EventAction.DELETED:
        this.calendar.evenements = this.calendar.evenements.filter((e) => e.id != change.value.id);
        this.snackbar.open('Evenement supprimé');
        break;
      case EventAction.UPDATED:
        let index = this.calendar.evenements.findIndex((e) => e.id == change.value.id);
        this.calendar.evenements[index] = change.value;
        this.snackbar.open('Evenement modifié');
        break;
    }
    this.calendar.putCalendarEvents();
  }

  next() {
    this.calendar.next();
  }

  previous() {
    this.calendar.previous();
  }

  onCalendarTypeChange(event: Event) {
    let element = event.target as HTMLInputElement;
    let value = element.value || '0';
    if (value == '0' && this.calendar.type != CalendarType.MONTH) {
      let newCalendanr = new MonthCalendar();
      newCalendanr.monthPosition = this.calendar.startingDay.startOf('month');
      newCalendanr.evenements = this.calendar.evenements;

      newCalendanr.updateCalendar();
      this.calendar = newCalendanr;
    }
    else if (value == '1' && this.calendar.type != CalendarType.WEEK) {
      let newCalendanr = new WeekCalendar();
      newCalendanr.evenements = this.calendar.evenements;
      newCalendanr.startingDay = this.calendar.startingDay.startOf('week');

      newCalendanr.updateCalendar();
      this.calendar = newCalendanr;
    }
  }

  isMonthCalendar(): boolean {
    return this.calendar.type == CalendarType.MONTH;
  }

  isWeekCalendar(): boolean {
    return this.calendar.type == CalendarType.WEEK;
  }

  searchEvenements(): void {
    let dateDebut = dayjs(this.searchForm.dateDebut);
    let dateFin = dayjs(this.searchForm.dateFin).set('hour', 23).set('minute', 59);
    if (dateDebut.isAfter(dateFin)) {
      this.snackbar.open('Dates incorrectes');
      return;
    }
    this.eventService.searchEvents(this.getPrefix(), dateDebut, dateFin, this.searchForm.description)
      .subscribe(
        (events) => {
          this.searchResult = events.map((e) => toParsed(e));
          let listId = this.calendar.evenements.map((e) => e.id);
          this.searchResult.forEach((e) => {
            if (!listId.includes(e.id)) {
              this.calendar.evenements.push(e);
            }
          });
        },
        () => { this.snackbar.open('Une erreur est survenue'); }
      );
  }

  cleanSearch(): void {
    this.searchForm = {
      dateDebut: '',
      dateFin: '',
      description: ''
    };
    this.searchResult = null;
  }
}
