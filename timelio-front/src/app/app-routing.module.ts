import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateEmploiComponent } from './components/create-emploi/create-emploi.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { ManageAccountComponent } from './components/manage-account/manage-account.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { VerifyAccountComponent } from './components/verify-account/verify-account.component';
import { AuthGuard } from './guards/auth/auth.guard';
import { VisitorGuard } from './guards/visitor/visitor.guard';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent, canActivate: [VisitorGuard]},
  {path: 'forgot-password', component: ForgotPasswordComponent, canActivate: [VisitorGuard]},
  {path: 'sign-up', component: SignUpComponent, canActivate: [VisitorGuard]},
  {path: 'account/verify/:token',component: VerifyAccountComponent},
  {path: 'account/reset-password/:token',component: ResetPasswordComponent},
  {path: 'account/infos', component: ManageAccountComponent, canActivate: [AuthGuard]},
  {path: 'create-emploi', component: CreateEmploiComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
