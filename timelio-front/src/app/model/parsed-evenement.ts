import * as dayjs from 'dayjs';
import * as duration from 'dayjs/plugin/duration';
export interface ParsedEvenement{
    id: number;
    dateDebut: dayjs.Dayjs;
	duree: duration.Duration;
	description: string;
	couleur: string;
	periodique: boolean;
	periode: duration.Duration;
}