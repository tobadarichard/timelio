import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { EvenementForm } from 'src/app/model/evenements/evenement-form';
import { ParsedEvenement } from 'src/app/model/evenements/parsed-evenement';
import { EvenementService } from 'src/app/services/evenement/evenement.service';
import { fromFormToParsed, getEventForm, toParsed } from 'src/app/utils/utils';
import { ChangeOnEvent } from '../events/change-on-event';
import { EventAction } from '../events/event-action';

@Component({
  selector: 'app-create-event-modal',
  templateUrl: './create-event-modal.component.html',
  styleUrls: ['./create-event-modal.component.css']
})
export class CreateEventModalComponent implements OnInit {

  @Input()
  prefix: string = '';
  @Output()
  emitter = new EventEmitter<ChangeOnEvent>();
  event: EvenementForm;

  constructor(private snackbar: MatSnackBar, private eventService: EvenementService) {
    this.event = getEventForm();
  }

  ngOnInit(): void {
  }

  creerEvenement(form: NgForm): void {
    let parsedEvent: ParsedEvenement;
    try {
      parsedEvent = fromFormToParsed(this.event);
    } catch (e) {
      this.snackbar.open(e.message, 'OK');
      return;
    }
    this.eventService.createEvent(this.prefix, parsedEvent).subscribe(
      (evenement) => {
        form.resetForm();
        this.emitter.emit({
          type: EventAction.CREATED,
          value: toParsed(evenement)
        });
      }, () => { this.snackbar.open('Une erreur est survenue') }
    );
  }
}
