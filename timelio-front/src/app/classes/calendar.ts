import { MatDatepickerInputEvent } from "@angular/material/datepicker";
import * as dayjs from "dayjs";
import * as duration from "dayjs/plugin/duration";
import { CalendarCell } from "../model/calendar-cell";
import { EvenementSummary } from "../model/evenements/evenement-summary";
import { ParsedEvenement } from "../model/evenements/parsed-evenement";
import { CalendarType } from "./calendar-type";

dayjs.extend(duration);
dayjs.locale('fr');

export abstract class Calendar {
    evenements: ParsedEvenement[] = [];
    startingDay = dayjs();
    endingDay = dayjs();
    calendarPage: CalendarCell[][] = [[]];
    abstract type: CalendarType;
    currentSelection = '';

    selectedEvent: ParsedEvenement | null = null;

    abstract putCalendarEvents(): void;

    abstract addEvent(event: ParsedEvenement): void;

    abstract addInPage(date: dayjs.Dayjs, summary: EvenementSummary): void;

    abstract changeCalendarPage(): void;

    abstract next(): void;

    abstract previous(): void;

    abstract changeDate(date: Date): void;

    abstract getRepere(): Date;

    addEventPeriodique(event: ParsedEvenement): void {
        let dateDebut = event.dateDebut;
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

    updateCalendar(): void {
        this.changeCalendarPage();
        this.putCalendarEvents();
    }

    showEventDetails(id: number) {
        this.selectedEvent = this.evenements.find((e) => e.id == id) || null;
    }
}