import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Error Handling Interceptor
 * Provides centralized error handling for HTTP requests
 */
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(
    private snackBar: MatSnackBar,
    private router: Router,
    private authService: AuthService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unexpected error occurred';

        switch (error.status) {
          case 400:
            errorMessage = this.handleBadRequest(error);
            break;
          case 401:
            errorMessage = 'Authentication required';
            this.handleUnauthorized();
            break;
          case 403:
            errorMessage = 'Access forbidden';
            break;
          case 404:
            errorMessage = this.handleNotFound(error);
            break;
          case 409:
            errorMessage = 'Resource conflict';
            break;
          case 422:
            errorMessage = this.handleValidationError(error);
            break;
          case 500:
            errorMessage = 'Internal server error';
            break;
          case 503:
            errorMessage = 'Service temporarily unavailable';
            break;
          default:
            if (error.error && error.error.message) {
              errorMessage = error.error.message;
            }
            break;
        }

        // Show error message to user
        this.showErrorMessage(errorMessage);

        return throwError(() => error);
      })
    );
  }

  /**
   * Handle 400 Bad Request errors
   */
  private handleBadRequest(error: HttpErrorResponse): string {
    if (error.error && error.error.message) {
      return error.error.message;
    }
    return 'Invalid request';
  }

  /**
   * Handle 401 Unauthorized errors
   */
  private handleUnauthorized(): void {
    this.authService.logout();
    this.router.navigate(['/auth/login']);
  }

  /**
   * Handle 404 Not Found errors
   */
  private handleNotFound(error: HttpErrorResponse): string {
    if (error.url?.includes('/customers/')) {
      return 'Customer not found';
    } else if (error.url?.includes('/accounts/')) {
      return 'Account not found';
    }
    return 'Resource not found';
  }

  /**
   * Handle 422 Validation errors
   */
  private handleValidationError(error: HttpErrorResponse): string {
    if (error.error && error.error.validationErrors) {
      const validationErrors = error.error.validationErrors;
      return validationErrors.map((err: any) => err.message).join(', ');
    }
    return 'Validation error';
  }

  /**
   * Show error message to user
   */
  private showErrorMessage(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar'],
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }
}
