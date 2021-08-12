import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { EmploiTemps } from 'src/app/model/emploi-temps';
import { AuthService } from 'src/app/services/auth/auth.service';
import { EmploiService } from 'src/app/services/emploi/emploi.service';

@Component({
  selector: 'app-create-emploi',
  templateUrl: './create-emploi.component.html',
  styleUrls: ['./create-emploi.component.css']
})
export class CreateEmploiComponent implements OnInit {

  emploiForm: FormGroup;
  constructor(public authService: AuthService,private fb: FormBuilder, private emploiService: EmploiService,
    private router: Router, private snackbar: MatSnackBar) {
    this.emploiForm = fb.group({
      'nom': ['', Validators.required],
      'publique': [false]
    });
  }

  ngOnInit(): void {
  }

  createEmploi(): void{
    var values = this.emploiForm.value;
    if (values.nom.trim() == ''){
      this.snackbar.open('Le nom ne peut pas être vide');
      return;
    }
    this.emploiForm.reset();
    var result: Observable<EmploiTemps>;
    if (this.authService.isLoggedIn()){
      result = this.emploiService.createUserEmploi(values.nom,values.publique);
    }
    else{
      result = this.emploiService.createEmploi(values.nom);
    }
    result.subscribe((emploi) => {
      this.snackbar.open('Emploi du temps crée');
      if (!this.authService.isLoggedIn() || values.publique){
        this.router.navigate(['/emplois/public',emploi.codeAcces]);
      }
      else{
        this.router.navigate(['/emplois/private',emploi.id]);
      }
    }, () => {this.snackbar.open('Une erreur est survenue')});
  }

}
