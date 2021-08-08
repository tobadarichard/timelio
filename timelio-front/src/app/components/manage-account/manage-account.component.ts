import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AccountService } from 'src/app/services/account/account.service';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-manage-account',
  templateUrl: './manage-account.component.html',
  styleUrls: ['./manage-account.component.css']
})
export class ManageAccountComponent implements OnInit {
  passwordForm: FormGroup;
  pseudoForm: FormGroup;
  constructor(private fb: FormBuilder, private accountService: AccountService,
      private snackbar: MatSnackBar,private authService: AuthService) {
    this.passwordForm = fb.group({
      'password': ['', Validators.required],
      'newPassword': ['', Validators.required],
      'confNewPassword': ['', Validators.required]
    });

    this.pseudoForm = fb.group({
      'pseudo': ['', [Validators.required, Validators.pattern('\\w+')]]
    });
  }

  ngOnInit(): void {
    this.authService.getUserInfos()
      .subscribe((infos) => this.pseudoForm.get('pseudo')?.setValue(infos.pseudo));
    
  }

  changePseudo(): void {
    var pseudo = this.pseudoForm.value.pseudo;
    this.accountService.changePseudo(pseudo).subscribe(
      () => {this.snackbar.open("Votre pseudo a bien été modifié","OK",{duration: undefined})},
      () => {this.snackbar.open("Une erreur est survenue","OK",{duration: undefined})}
    )
  }

  changePassword(): void {
    var values = this.passwordForm.value;
    if (values.newPassword != values.confNewPassword){
      this.snackbar.open("Les mots de passe ne correspondent pas","OK",{duration: undefined});
      return;
    }
    this.accountService.changeMdp(values.password,values.newPassword).subscribe(() => {
      this.passwordForm.reset();
      this.snackbar.open("Votre mot de passe a bien été modifié","OK",{duration: undefined});
    }, () => {this.snackbar.open("Mot de passe incorrect","OK",{duration: undefined})});
  }

  deleteAccount(): void{
    this.accountService.deleteAccount().subscribe(() => {}, () => {
      this.snackbar.open("Une erreur est survenue","OK",{duration: undefined});
    });
  }
}
