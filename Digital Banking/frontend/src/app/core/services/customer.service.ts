import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Customer, CustomerDTO, CustomerFilter } from '../models';

/**
 * Customer Service
 * Handles all customer-related API operations
 */
@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private readonly apiUrl = `${environment.apiUrl}/customers`;

  constructor(private http: HttpClient) {}

  /**
   * Get all customers
   */
  getAllCustomers(): Observable<Customer[]> {
    return this.http.get<Customer[]>(this.apiUrl);
  }

  /**
   * Get customer by ID
   */
  getCustomerById(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new customer
   */
  createCustomer(customer: CustomerDTO): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrl, customer);
  }

  /**
   * Update existing customer
   */
  updateCustomer(id: number, customer: CustomerDTO): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${id}`, customer);
  }

  /**
   * Delete customer
   */
  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Search customers with filters
   */
  searchCustomers(filter: CustomerFilter): Observable<Customer[]> {
    let params = new HttpParams();

    if (filter.searchTerm) {
      params = params.set('search', filter.searchTerm);
    }

    if (filter.emailDomain) {
      params = params.set('emailDomain', filter.emailDomain);
    }

    return this.http.get<Customer[]>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Get customer accounts
   */
  getCustomerAccounts(customerId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${customerId}/accounts`);
  }
}
