import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  mailForm: FormGroup;
  envoye = false;
  constructor(public authService: AuthService, private fb: FormBuilder, private snackbar: MatSnackBar) {
    this.mailForm = fb.group(
      {
        'email': ['', [Validators.required, Validators.email]],
      }
    );
  }

  resetMdp(): void{
    this.envoye = false;
    this.authService.askResetMdp(this.mailForm.value.email).subscribe(
      () => { 
        this.envoye = true;
        this.mailForm.reset();
       },
      (error) => {
        this.mailForm.reset();
        if (error instanceof HttpErrorResponse) {
          switch (error.status) {
            case 400:
              this.snackbar.open('Formulaire incomplet');
              break;
            case 404:
              this.snackbar.open('Compte non trouvé');
              break;
            default:
              this.snackbar.open('Une erreur s\'est produite veuillez reessayer');
              break;
          }
        }
        else {
          this.snackbar.open('Erreur');
        }
      });

  }

  ngOnInit(): void {
  }

}
