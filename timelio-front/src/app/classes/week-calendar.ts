import * as dayjs from "dayjs";
import * as duration from "dayjs/plugin/duration";
import { EvenementSummary } from "../model/evenements/evenement-summary";
import { ParsedEvenement } from "../model/evenements/parsed-evenement";
import { Calendar } from "./calendar";
import { CalendarType } from "./calendar-type";

dayjs.extend(duration);
dayjs.locale('fr');
export class WeekCalendar extends Calendar {
    type = CalendarType.WEEK;

    constructor() {
        super();
        this.startingDay = dayjs().startOf('week');
    }

    putCalendarEvents(): void {
        this.calendarPage = [];
        let day: dayjs.Dayjs;
        for (let i = 0; i < 12; i++) {
            this.calendarPage.push([]);
            for (let j = 0; j < 7; j++) {
                day = this.startingDay.add(j, 'day');
                this.calendarPage[i].push({
                    label: '',
                    classLabel: '',
                    date: day,
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

        if (event.dateDebut.format('YYYY-MM-DD') == event.dateFin.format('YYYY-MM-DD') &&
            Math.floor(event.dateDebut.hour() / 2) == Math.floor(event.dateFin.hour() / 2)) {
            this.addInPage(event.dateDebut, {
                id: event.id,
                description: event.description,
                couleur: event.couleur
            });
        }
        else {
            if (this.isInPage(event.dateDebut)) {
                this.addInPage(event.dateDebut, {
                    id: event.id,
                    description: 'DEBUT : ' + event.description,
                    couleur: event.couleur
                });
            }
            if (this.isInPage(event.dateFin)) {
                this.addInPage(event.dateFin, {
                    id: event.id,
                    description: 'FIN : ' + event.description,
                    couleur: event.couleur
                });
            }
        }
    }

    addInPage(date: dayjs.Dayjs, summary: EvenementSummary): void {
        let j = date.day();
        j = j == 0 ? 6 : j-1;
        let i = Math.floor(date.hour() / 2);
        this.calendarPage[i][j].evenements.push(summary);
    }

    changeCalendarPage(): void {
        this.endingDay = this.startingDay.add(6, 'day').set('hour', 23)
            .set('minute', 59).set('second', 59);

        this.currentSelection = this.startingDay.format('DD MMMM') + ' au '
            + this.endingDay.format('DD MMMM');
    }

    next(): void {
        this.startingDay = this.startingDay.add(1, 'week');
        this.updateCalendar();
    }

    previous(): void {
        this.startingDay = this.startingDay.subtract(1, 'week');
        this.updateCalendar();
    }

    changeDate(date: Date): void {
        this.startingDay = dayjs(date).startOf('week');
        this.updateCalendar();
    }

    getRepere(): Date{
        return this.startingDay.toDate();
    }
}