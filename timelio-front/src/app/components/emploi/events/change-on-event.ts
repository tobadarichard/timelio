import { ParsedEvenement } from "src/app/model/evenements/parsed-evenement";
import { EventAction } from "./event-action";

export interface ChangeOnEvent{
    type: EventAction,
    value: ParsedEvenement
}