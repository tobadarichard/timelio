import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { VerifyAccountComponent } from './components/verify-account/verify-account.component';
import { VisitorGuard } from './guards/visitor/visitor.guard';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent, canActivate: [VisitorGuard]},
  {path: 'forgot-password', component: ForgotPasswordComponent, canActivate: [VisitorGuard]},
  {path: 'sign-up', component: SignUpComponent, canActivate: [VisitorGuard]},
  {path: 'account/verify/:token',component: VerifyAccountComponent},
  {path: 'account/reset-password/:token',component: ResetPasswordComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
