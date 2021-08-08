import * as dayjs from 'dayjs';
import { EvenementSummary } from './evenements/evenement-summary';

export interface CalendarCell{
    date: dayjs.Dayjs,
	evenements: EvenementSummary[]
}