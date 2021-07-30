import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { AccountService } from 'src/app/services/account/account.service';

@Component({
  selector: 'app-verify-account',
  templateUrl: './verify-account.component.html',
  styleUrls: ['./verify-account.component.css']
})
export class VerifyAccountComponent implements OnInit {
  private token: string;
  verificationResult: Observable<string> | null = null;
  askNewTokenResult: Observable<boolean> | null = null;
  constructor(private accountService: AccountService, private route: ActivatedRoute) {
    this.token = route.snapshot.paramMap.get('token') || '';
  }

  ngOnInit(): void {
    this.verificationResult = new Observable((subscriber) => {
      if (this.token == '' || !this.token.match('^\\w+$')) {
        subscriber.next('BAD');
        subscriber.complete();
        return;
      }
      this.accountService.verifyAccount(this.token).subscribe(
        () => {
          subscriber.next('OK');
          subscriber.complete();
        }, (error) => {
          if (error instanceof HttpErrorResponse && error.status == 400
            && (error.error as string).includes('expiré')) {
            subscriber.next('EXPIRED');
            subscriber.complete();
          }
          else {
            subscriber.next('BAD');
            subscriber.complete();
          }
        });
    });
  }

  askNewToken(): void {
    this.askNewTokenResult = new Observable<boolean>((subscriber) => {
      this.accountService.askNewVerification(this.token).subscribe(() => {
        subscriber.next(true);
        subscriber.complete();
      }, () => {
        subscriber.next(false);
        subscriber.complete();
      });
    });

  }

}
