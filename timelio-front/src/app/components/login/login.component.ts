import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  incorrectLogin = false;
  error: String | null;
  constructor(public authService: AuthService, private fb: FormBuilder, private router: Router,
    private snackbar: MatSnackBar,private route: ActivatedRoute) {
    this.loginForm = fb.group(
      {
        'email': ['', [Validators.required, Validators.email]],
        'password': ['', Validators.required]
      }
    );
    this.error = route.snapshot.queryParamMap.get('error')
  }

  ngOnInit(): void {
  }

  login(): void {
    var credentials = this.loginForm.value;
    this.incorrectLogin = false;
    this.loginForm.markAsPristine();
    this.authService.login(credentials.email, credentials.password).subscribe(
      () => { this.router.navigate(['/home']); },
      (error) => {
        this.incorrectLogin = true;
        if (error instanceof HttpErrorResponse) {
          switch (error.status) {
            case 400:
              if ((error.error as string).includes('non vérifié')){
                this.snackbar.open('Ce compte n\'est pas encore vérifié')
              }
              else{
                this.snackbar.open('Formulaire incomplet');
              }
              break;
            case 404:
              this.snackbar.open('Mot de passe ou email incorrect');
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

}
