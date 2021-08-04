import * as dayjs from 'dayjs';
import { EvenementSummary } from './evenement-summary';
export interface CalendarCell{
    date: dayjs.Dayjs,
	evenements: EvenementSummary[]
}