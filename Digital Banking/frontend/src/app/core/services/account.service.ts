import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  Account,
  CurrentAccount,
  SavingsAccount,
  CreateAccountDTO,
  AccountFilter
} from '../models';

/**
 * Account Service
 * Handles all bank account-related API operations
 */
@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private readonly apiUrl = `${environment.apiUrl}/accounts`;

  constructor(private http: HttpClient) {}

  /**
   * Get all bank accounts
   */
  getAllAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(this.apiUrl);
  }

  /**
   * Get account by ID
   */
  getAccountById(id: string): Observable<Account> {
    return this.http.get<Account>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create current account
   */
  createCurrentAccount(accountData: CreateAccountDTO): Observable<CurrentAccount> {
    const params = new HttpParams()
      .set('initialBalance', accountData.initialBalance.toString())
      .set('overDraft', accountData.overDraft?.toString() || '0')
      .set('customerId', accountData.customerId.toString());

    return this.http.post<CurrentAccount>(`${this.apiUrl}/current`, null, { params });
  }

  /**
   * Create savings account
   */
  createSavingsAccount(accountData: CreateAccountDTO): Observable<SavingsAccount> {
    const params = new HttpParams()
      .set('initialBalance', accountData.initialBalance.toString())
      .set('interestRate', accountData.interestRate?.toString() || '2.5')
      .set('customerId', accountData.customerId.toString());

    return this.http.post<SavingsAccount>(`${this.apiUrl}/saving`, null, { params });
  }

  /**
   * Get account operations/history
   */
  getAccountOperations(accountId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${accountId}/operations`);
  }

  /**
   * Filter accounts based on criteria
   */
  filterAccounts(filter: AccountFilter): Observable<Account[]> {
    let params = new HttpParams();

    if (filter.accountType) {
      params = params.set('type', filter.accountType);
    }

    if (filter.status) {
      params = params.set('status', filter.status);
    }

    if (filter.customerId) {
      params = params.set('customerId', filter.customerId.toString());
    }

    if (filter.minBalance !== undefined) {
      params = params.set('minBalance', filter.minBalance.toString());
    }

    if (filter.maxBalance !== undefined) {
      params = params.set('maxBalance', filter.maxBalance.toString());
    }

    return this.http.get<Account[]>(`${this.apiUrl}/filter`, { params });
  }

  /**
   * Get accounts by customer ID
   */
  getAccountsByCustomer(customerId: number): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.apiUrl}/customer/${customerId}`);
  }

  /**
   * Update account status
   */
  updateAccountStatus(accountId: string, status: string): Observable<Account> {
    return this.http.patch<Account>(`${this.apiUrl}/${accountId}/status`, { status });
  }

  /**
   * Close account
   */
  closeAccount(accountId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${accountId}`);
  }
}
