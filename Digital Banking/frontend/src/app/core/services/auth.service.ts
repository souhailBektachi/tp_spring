import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, timer } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

import { environment } from '../../../environments/environment';
import {
  LoginRequest,
  LoginResponse,
  User,
  JwtPayload,
  PasswordResetRequest,
  ChangePasswordRequest
} from '../models';

/**
 * Authentication service handling JWT token management
 * and user session state
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'banking_access_token';
  private readonly REFRESH_TOKEN_KEY = 'banking_refresh_token';
  private readonly USER_KEY = 'banking_user';
  private readonly REMEMBER_ME_KEY = 'banking_remember_me';

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  private sessionTimeoutTimer: any;

  public currentUser$ = this.currentUserSubject.asObservable();
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    this.initializeAuth();
  }

  /**
   * Initialize authentication state from stored tokens
   */
  private initializeAuth(): void {
    const token = this.getStoredToken();
    const user = this.getStoredUser();

    if (token && user && this.isTokenValid(token)) {
      this.currentUserSubject.next(user);
      this.isAuthenticatedSubject.next(true);
      this.setupSessionTimeout(token);
    } else {
      this.logout();
    }
  }

  /**
   * Login user with username and password
   */
  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    // loginRequest should have { username, password }
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/authenticate`, loginRequest)
      .pipe(
        tap(response => {
          this.handleSuccessfulLogin(response, loginRequest.rememberMe);
        }),
        catchError(error => {
          throw error;
        })
      );
  }

  /**
   * Logout current user
   */
  logout(): void {
    this.clearStoredData();
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
    this.clearSessionTimeout();
    this.router.navigate(['/auth/login']);
  }

  /**
   * Get current user
   */
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  /**
   * Get stored JWT token
   */
  getToken(): string | null {
    return this.getStoredToken();
  }

  /**
   * Request password reset
   */
  requestPasswordReset(request: PasswordResetRequest): Observable<any> {
    return this.http.post(`${environment.apiUrl}/auth/password-reset`, request);
  }

  /**
   * Change user password
   */
  changePassword(request: ChangePasswordRequest): Observable<any> {
    return this.http.post(`${environment.apiUrl}/auth/change-password`, request);
  }

  /**
   * Refresh authentication token
   */
  refreshToken(): Observable<LoginResponse> {
    const refreshToken = localStorage.getItem(this.REFRESH_TOKEN_KEY);

    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/refresh`, { refreshToken })
      .pipe(
        tap(response => {
          this.updateStoredTokens(response);
          this.setupSessionTimeout(response.accessToken);
        }),
        catchError(error => {
          this.logout();
          throw error;
        })
      );
  }

  /**
   * Handle successful login response
   */
  private handleSuccessfulLogin(response: LoginResponse, rememberMe?: boolean): void {
    this.storeAuthData(response, rememberMe);
    this.currentUserSubject.next(response.user);
    this.isAuthenticatedSubject.next(true);
    this.setupSessionTimeout(response.accessToken);
  }

  /**
   * Store authentication data
   */
  private storeAuthData(response: LoginResponse, rememberMe?: boolean): void {
    const storage = rememberMe ? localStorage : sessionStorage;

    storage.setItem(this.TOKEN_KEY, response.accessToken);
    storage.setItem(this.USER_KEY, JSON.stringify(response.user));

    if (rememberMe) {
      localStorage.setItem(this.REMEMBER_ME_KEY, 'true');
    }
  }

  /**
   * Update stored tokens after refresh
   */
  private updateStoredTokens(response: LoginResponse): void {
    const storage = localStorage.getItem(this.REMEMBER_ME_KEY) ? localStorage : sessionStorage;
    storage.setItem(this.TOKEN_KEY, response.accessToken);
  }

  /**
   * Get stored token from storage
   */
  private getStoredToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY) || sessionStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Get stored user from storage
   */
  private getStoredUser(): User | null {
    const userStr = localStorage.getItem(this.USER_KEY) || sessionStorage.getItem(this.USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
  }

  /**
   * Clear all stored authentication data
   */
  private clearStoredData(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    localStorage.removeItem(this.REMEMBER_ME_KEY);

    sessionStorage.removeItem(this.TOKEN_KEY);
    sessionStorage.removeItem(this.USER_KEY);
  }

  /**
   * Check if token is valid and not expired
   */
  private isTokenValid(token: string): boolean {
    try {
      const payload = this.decodeToken(token);
      const currentTime = Date.now() / 1000;
      return payload.exp > currentTime;
    } catch {
      return false;
    }
  }

  /**
   * Decode JWT token payload
   */
  private decodeToken(token: string): JwtPayload {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c =>
      '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    ).join(''));

    return JSON.parse(jsonPayload);
  }

  /**
   * Setup session timeout based on token expiration
   */
  private setupSessionTimeout(token: string): void {
    this.clearSessionTimeout();

    try {
      const payload = this.decodeToken(token);
      const expirationTime = payload.exp * 1000;
      const currentTime = Date.now();
      const timeToExpiry = expirationTime - currentTime;

      // Set timeout for 5 minutes before expiration to attempt refresh
      const refreshTime = Math.max(timeToExpiry - (5 * 60 * 1000), 60000);

      this.sessionTimeoutTimer = timer(refreshTime).subscribe(() => {
        this.handleSessionTimeout();
      });
    } catch (error) {
      console.error('Error setting up session timeout:', error);
      this.logout();
    }
  }

  /**
   * Handle session timeout
   */
  private handleSessionTimeout(): void {
    const refreshToken = localStorage.getItem(this.REFRESH_TOKEN_KEY);

    if (refreshToken) {
      this.refreshToken().subscribe({
        next: () => {
          console.log('Token refreshed successfully');
        },
        error: () => {
          console.log('Token refresh failed, logging out');
          this.logout();
        }
      });
    } else {
      this.logout();
    }
  }

  /**
   * Clear session timeout timer
   */
  private clearSessionTimeout(): void {
    if (this.sessionTimeoutTimer) {
      this.sessionTimeoutTimer.unsubscribe();
      this.sessionTimeoutTimer = null;
    }
  }
}
