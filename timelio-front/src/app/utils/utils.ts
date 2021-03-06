import { Evenement } from "../model/evenements/evenement";
import { EvenementForm } from "../model/evenements/evenement-form";
import { ParsedEvenement } from "../model/evenements/parsed-evenement";

import * as dayjs from "dayjs";
import * as duration from 'dayjs/plugin/duration';
import 'dayjs/locale/fr';
dayjs.extend(duration);
dayjs.locale('fr');

function toParsed(event: Evenement): ParsedEvenement {
    var duree = dayjs.duration(event.duree);
    var dateDebut = dayjs(event.dateDebut);

    return {
        id: event.id,
        dateDebut: dateDebut,
        dateFin: dateDebut.add(duree.asSeconds(), 'seconds'),
        duree: duree,
        description: event.description,
        couleur: event.couleur,
        periodique: event.periodique,
        periode: dayjs.duration(event.periode)
    };
}

function fromFormToParsed(form: EvenementForm): ParsedEvenement {
    if (form.description.trim() == '') {
        throw new Error('La description ne doit pas être vide')
    }
    if (!form.couleur.match('^#(?:[0-9a-fA-F]{3}){1,2}$')) {
        throw new Error('Couleur invalide')
    }
    let dateDebut = dayjs(form.dateDebut + "T" + form.heureDebut);
    let dateFin = dayjs(form.dateFin + "T" + form.heureFin);
    if (dateDebut.isAfter(dateFin) || dateDebut.isSame(dateFin)) {
        throw new Error('La date de fin doit être après la date de début');
    }
    
    let dureeDay = dateFin.diff(dateDebut, "days");
    let newDateFin = dateFin.subtract(dureeDay, "days");
    let dureeHours = newDateFin.diff(dateDebut, "hours");
    newDateFin = newDateFin.subtract(dureeHours, "hours");
    let dureeMinutes = newDateFin.diff(dateDebut, "minutes");
    let duree = dayjs.duration({days: dureeDay, 
                                hours: dureeHours,
                                minutes: dureeMinutes});

    let periode = dayjs.duration(0);
    if (form.periodique) {
        if (form.periodeJour <= 0) {
            throw new Error('Periode invalide');
        }
        else {
            periode = dayjs.duration({ days: form.periodeJour });
        }
    }

    return {
        id: form.id,
        dateDebut: dateDebut,
        dateFin: dateFin,
        duree: duree,
        description: form.description,
        couleur: form.couleur,
        periodique: form.periodique,
        periode: periode
    };
}

function fromParsedToForm(evenement: ParsedEvenement | null): EvenementForm {
    if (!evenement) {
        return getEventForm();
    }
    let periodeJour = evenement.periodique ? evenement.periode.asDays() : 1;
    return {
        id: evenement.id,
        dateDebut: evenement.dateDebut.format('YYYY-MM-DD'),
        heureDebut: evenement.dateDebut.format('HH:mm'),
        dateFin: evenement.dateFin.format('YYYY-MM-DD'),
        heureFin: evenement.dateFin.format('HH:mm'),
        description: evenement.description,
        couleur: evenement.couleur,
        periodique: evenement.periodique,
        periodeJour: periodeJour
    };
}

function getEventForm(): EvenementForm{
    return {
        id: 0,
        dateDebut: '',
        heureDebut: '',
        dateFin: '',
        heureFin: '',
        description: '',
        couleur: '#000000',
        periodique: false,
        periodeJour: 1
    }; 
}

export { toParsed, fromFormToParsed, fromParsedToForm , getEventForm};