import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  AccountOperation,
  DebitDTO,
  CreditDTO,
  TransferDTO,
  OperationFilter,
  OperationPagination
} from '../models';

/**
 * Operation Service
 * Handles all banking operation-related API calls
 */
@Injectable({
  providedIn: 'root'
})
export class OperationService {
  private readonly apiUrl = `${environment.apiUrl}/operations`;

  constructor(private http: HttpClient) {}

  /**
   * Perform debit operation (withdrawal)
   */
  debit(debitData: DebitDTO): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/debit`, debitData);
  }

  /**
   * Perform credit operation (deposit)
   */
  credit(creditData: CreditDTO): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/credit`, creditData);
  }

  /**
   * Perform transfer operation
   */
  transfer(transferData: TransferDTO): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/transfer`, transferData);
  }

  /**
   * Get operation history for an account
   */
  getOperationHistory(
    accountId: string,
    filter?: OperationFilter,
    pagination?: OperationPagination
  ): Observable<AccountOperation[]> {
    let params = new HttpParams();

    if (filter) {
      if (filter.type) {
        params = params.set('type', filter.type);
      }

      if (filter.startDate) {
        params = params.set('startDate', filter.startDate.toISOString());
      }

      if (filter.endDate) {
        params = params.set('endDate', filter.endDate.toISOString());
      }

      if (filter.minAmount !== undefined) {
        params = params.set('minAmount', filter.minAmount.toString());
      }

      if (filter.maxAmount !== undefined) {
        params = params.set('maxAmount', filter.maxAmount.toString());
      }

      if (filter.description) {
        params = params.set('description', filter.description);
      }
    }

    if (pagination) {
      params = params.set('page', pagination.page.toString());
      params = params.set('size', pagination.size.toString());

      if (pagination.sortBy) {
        params = params.set('sortBy', pagination.sortBy);
      }

      if (pagination.sortDirection) {
        params = params.set('sortDirection', pagination.sortDirection);
      }
    }

    return this.http.get<AccountOperation[]>(`${environment.apiUrl}/accounts/${accountId}/operations`, { params });
  }

  /**
   * Get recent operations across all accounts
   */
  getRecentOperations(limit: number = 10): Observable<AccountOperation[]> {
    const params = new HttpParams().set('limit', limit.toString());
    return this.http.get<AccountOperation[]>(`${this.apiUrl}/recent`, { params });
  }

  /**
   * Get operations by date range
   */
  getOperationsByDateRange(
    startDate: Date,
    endDate: Date,
    accountId?: string
  ): Observable<AccountOperation[]> {
    let params = new HttpParams()
      .set('startDate', startDate.toISOString())
      .set('endDate', endDate.toISOString());

    if (accountId) {
      params = params.set('accountId', accountId);
    }

    return this.http.get<AccountOperation[]>(`${this.apiUrl}/date-range`, { params });
  }

  /**
   * Get operation statistics
   */
  getOperationStatistics(accountId?: string): Observable<any> {
    let params = new HttpParams();

    if (accountId) {
      params = params.set('accountId', accountId);
    }

    return this.http.get<any>(`${this.apiUrl}/statistics`, { params });
  }
}
