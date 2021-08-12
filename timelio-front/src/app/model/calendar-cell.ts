import * as dayjs from 'dayjs';
import { EvenementSummary } from './evenements/evenement-summary';

export interface CalendarCell{
    date: dayjs.Dayjs,
    label: string,
    classLabel: string,
	evenements: EvenementSummary[]
}