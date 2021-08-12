import * as dayjs from "dayjs";
import * as duration from "dayjs/plugin/duration";
import { EvenementSummary } from "../model/evenements/evenement-summary";
import { ParsedEvenement } from "../model/evenements/parsed-evenement";
import { Calendar } from "./calendar";
import { CalendarType } from "./calendar-type";

dayjs.extend(duration);
dayjs.locale('fr');

export class MonthCalendar extends Calendar {
    type = CalendarType.MONTH;
    monthPosition: dayjs.Dayjs;

    constructor(){
        super();
        this.monthPosition = dayjs().startOf('month');
    }

    putCalendarEvents(): void {
        this.calendarPage = [
            [], [], [], [], [], []
        ];
        let day: dayjs.Dayjs;
        for (let i = 0; i < 6; i++) {
            for (let j = 0; j < 7; j++) {
                day = this.startingDay.add(i * 7 + j, 'day');
                this.calendarPage[i].push({
                    label: day.format('D MMMM'),
                    classLabel: day.month() != this.monthPosition.month() ? 'text-muted' : '',
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

    addInPage(date: dayjs.Dayjs, summary: EvenementSummary): void {
        let difference = dayjs.duration(date.diff(this.startingDay));
        if (difference.asSeconds() < 0) { return; }
        let i = Math.floor(difference.asDays() / 7);
        let j = difference.asDays() % 7;
        this.calendarPage[i][j].evenements.push(summary);
    }

    changeCalendarPage(): void {
        this.currentSelection = this.monthPosition.format('MMMM YYYY');
        this.startingDay = this.monthPosition.startOf('week');
        this.endingDay = this.startingDay.add(6, 'week').subtract(1, 'day');

        this.endingDay = this.endingDay
            .set('hour', 23).set('minute', 59).set('second', 59);
    }

    next(): void {
        this.monthPosition = this.monthPosition.add(1,'month');
        this.updateCalendar();
    }

    previous(): void {
        this.monthPosition = this.monthPosition.subtract(1,'month');
        this.updateCalendar();
    }

    changeDate(date: Date): void {
        this.monthPosition = dayjs(date).startOf('month');
        this.updateCalendar();
    }

    getRepere(): Date{
        return this.monthPosition.toDate();
    }
}