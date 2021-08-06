import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EvenementForm } from 'src/app/model/evenements/evenement-form';
import { ParsedEvenement } from 'src/app/model/evenements/parsed-evenement';
import { EvenementService } from 'src/app/services/evenement/evenement.service';
import { fromFormToParsed, fromParsedToForm } from 'src/app/utils/utils';
import { ChangeOnEvent } from '../events/change-on-event';
import { EventAction } from '../events/event-action';

@Component({
  selector: 'app-details-event-modal',
  templateUrl: './details-event-modal.component.html',
  styleUrls: ['./details-event-modal.component.css']
})
export class DetailsEventModalComponent implements OnInit, OnChanges {

  @Input()
  evenement: ParsedEvenement | null = null;
  @Input()
  prefix: string = '';
  @Output()
  emitter = new EventEmitter<ChangeOnEvent>();
  enModif = false;
  modifiedEvent: EvenementForm;

  constructor(private snackbar: MatSnackBar, private eventService: EvenementService) {
    this.modifiedEvent = fromParsedToForm(this.evenement);
  }

  ngOnInit(): void {
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.annulerModif();
  }

  modifierEvenement(): void {
    let parsedEvent: ParsedEvenement;
    try {
      parsedEvent = fromFormToParsed(this.modifiedEvent);
    } catch (e) {
      this.snackbar.open(e.message, 'OK');
      return;
    }
    this.eventService.updateEvent(this.prefix, parsedEvent).subscribe(
      () => {
        this.emitter.emit({
          type: EventAction.UPDATED,
          value: parsedEvent
        });
        this.enModif = false;
      }, () => { this.snackbar.open('Une erreur est survenue') }
    );
  }

  supprimerEvenement(): void {
    if (!this.evenement){
      return;
    }
    let copy: ParsedEvenement = {...this.evenement};
    this.eventService.deleteEvent(this.prefix,this.evenement.id).subscribe(
      () => {
        this.emitter.emit({
          type: EventAction.DELETED,
          value: copy
        });
      },
      () => {this.snackbar.open('Une erreur est survenue')}
    );
  }

  annulerModif(): void {
    this.enModif = false;
    this.modifiedEvent = fromParsedToForm(this.evenement);
  }

  formatPeriode(): string {
    let jours = this.modifiedEvent.periodeJour;
    if (jours == 7){
      return 'Toutes les semaines';
    }
    return 'Tous les ' + (jours == 1 ? '' : jours) + ' jours';
  }

}
