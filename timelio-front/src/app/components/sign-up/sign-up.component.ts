import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AccountService } from 'src/app/services/account/account.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {
  signUpForm: FormGroup;
  passwordsMatch = true;
  envoye = false;
  mailAvailable = true;
  constructor(private accountService: AccountService, private fb: FormBuilder,
    private snackbar: MatSnackBar) {
    this.signUpForm = fb.group({
      'email': ['', [Validators.required, Validators.email]],
      'password': ['', Validators.required],
      'confPassword': ['', Validators.required],
      'pseudo': ['', [Validators.required,Validators.pattern('\\w+')]]
    });
  }

  signUp(): void {
    this.envoye = false;
    this.mailAvailable = true;
    var values = this.signUpForm.value;
    this.passwordsMatch = values.confPassword == values.password;
    this.signUpForm.markAsPristine();
    if (!this.passwordsMatch) {
      return;
    }
    this.accountService.signUp(values.email, values.password, values.pseudo).subscribe(
      () => { 
        this.envoye = true;
        this.signUpForm.reset();
      },
      (error) => {
        if (!(error instanceof HttpErrorResponse)) {
          this.snackbar.open('Erreur');
          return;
        }
        if (error.status == 409) {
          this.mailAvailable = false;
        }
        else if (error.status == 400) {
          this.snackbar.open('Formulaire incomplet ou invalide');
        }
        else {
          this.snackbar.open('Une erreur s\'est produite veuillez reessayer');
        }
      });
  }

  ngOnInit(): void {
  }

}
