import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { EmploiService } from 'src/app/services/emploi/emploi.service';


@Component({
  selector: 'app-update-emploi-modal',
  templateUrl: './update-emploi-modal.component.html',
  styleUrls: ['./update-emploi-modal.component.css']
})
export class UpdateEmploiModalComponent implements OnInit,OnChanges {

  @Input()
  emploi!: EmploiTemps;
  @Input()
  isUserEmploi!: boolean;
  emploiForm: FormGroup;
  @Output()
  updateEmploiEvent = new EventEmitter<EmploiTemps>();

  constructor(private emploiService: EmploiService,private router: Router,
    private snackbar: MatSnackBar,private fb: FormBuilder) {
      this.emploiForm = fb.group({
        'nom': ['', Validators.required],
        'publique': [false]
      });
     }
  ngOnChanges(changes: SimpleChanges): void {
    this.emploiForm.get('nom')?.setValue(this.emploi.nom);
    this.emploiForm.get('publique')?.setValue(this.emploi.publique);
  }

  ngOnInit(): void {
    this.emploiForm.get('nom')?.setValue(this.emploi.nom);
    this.emploiForm.get('publique')?.setValue(this.emploi.publique);
  }

  updateEmploi(): void{
    var values = this.emploiForm.value;
    if (values.nom.trim() == ''){
      this.snackbar.open('Le nom ne peut pas être vide');
      return;
    }
    this.emploiForm.reset();
    var result: Observable<EmploiTemps>;
    if (this.isUserEmploi){
      result = this.emploiService.updateUserEmploi(this.emploi.id,values.nom,values.publique);
    }
    else{
      result = this.emploiService.updateEmploi(this.emploi.codeAcces,values.nom);
    }
    result.subscribe((emploi) => {
      this.emploi = emploi;
      this.updateEmploiEvent.emit(emploi);
    }, () => {this.snackbar.open('Une erreur est survenue')});
  }

}
