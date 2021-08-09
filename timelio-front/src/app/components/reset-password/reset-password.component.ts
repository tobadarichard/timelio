import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from 'src/app/services/account/account.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  passwordForm: FormGroup;
  passwordsMatch = false;
  success = false;
  sent = false;
  token: string;
  constructor(private accountService: AccountService, private fb: FormBuilder,
    private snackbar: MatSnackBar, private route: ActivatedRoute) {
    this.passwordForm = fb.group({
      'password': ['', Validators.required],
      'confPassword': ['', Validators.required]
    });
    var token = route.snapshot.paramMap.get('token') || '';
    this.token = token.match('^\\w+$') ? token : '';
  }

  ngOnInit(): void {
  }

  changePassword(): void {
    this.success = false;
    var values = this.passwordForm.value;
    this.passwordsMatch = values.confPassword == values.password;
    this.passwordForm.markAsPristine();

    if (!this.passwordsMatch) {
      return;
    }
    this.accountService.resetMdpByToken(this.token, values.password).subscribe(
      () => { 
        this.success = true;
        this.sent = true;
      }, () => { 
        this.success = false;
        this.sent = true;
      });
  }

}
