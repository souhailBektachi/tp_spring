import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthRoutingModule } from './auth-routing.module';
import { SharedModule } from '../../shared.module';
import { LoginComponent } from './login/login.component';


@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    SharedModule,
    AuthRoutingModule,
    LoginComponent
  ]
})
export class AuthModule { }
