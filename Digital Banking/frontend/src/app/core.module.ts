import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuthService } from './core/services/auth.service';
import { LoadingService } from './core/services/loading.service';
import { CustomerService } from './core/services/customer.service';
import { AccountService } from './core/services/account.service';
import { OperationService } from './core/services/operation.service';

@NgModule({
  declarations: [],
  imports: [
    CommonModule
  ],
  providers: [
    AuthService,
    LoadingService,
    CustomerService,
    AccountService,
    OperationService
  ]
})
export class CoreModule { }
