import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../../core/services/auth.service';
import { LoadingService } from '../../../core/services/loading.service';
import { LoginResponse, LoginRequest } from '../../../core/models';

/**
 * Login component providing secure authentication for the banking application
 * Includes form validation, error handling, and remember me functionality
 */
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatButtonModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ]
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm: FormGroup;
  hidePassword = true;
  errorMessage = '';
  returnUrl = '';

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly authService: AuthService,
    private readonly loadingService: LoadingService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.loginForm = this.createLoginForm();
  }

  ngOnInit(): void {
    // Get return URL from route parameters or default to dashboard
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] ?? '/dashboard';

    // Redirect to dashboard if already authenticated
    if (this.authService.isAuthenticated()) {
      this.router.navigate([this.returnUrl]);
    }

    // Clear any existing error messages when form values change
    this.loginForm.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => {
        if (this.errorMessage) {
          this.errorMessage = '';
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
  /**
   * Creates and configures the reactive login form with validation
   */
  private createLoginForm(): FormGroup {
    return this.formBuilder.group({
      email: ['', [
        Validators.required,
        // Validators.email, // <-- Remove email validation
        Validators.maxLength(100)
      ]],
      password: ['', [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(100)
      ]],
      rememberMe: [false]
    });
  }
  /**
   * Handles form submission and authentication
   */
  onSubmit(): void {
    if (this.loginForm.valid) {
      this.errorMessage = '';
      const loginRequest: LoginRequest = {
        email: this.loginForm.value.email,
        password: this.loginForm.value.password,
        rememberMe: this.loginForm.value.rememberMe
      };

      this.authService.login(loginRequest)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (_: LoginResponse) => {
            // Successful login - redirect to return URL
            this.router.navigate([this.returnUrl]);
          },
          error: (error: any) => {
            // Handle login errors
            this.handleLoginError(error);
          }
        });
    } else {
      // Mark all fields as touched to show validation errors
      this.markFormGroupTouched();
    }
  }

  /**
   * Handles various login error scenarios
   */
  private handleLoginError(error: any): void {
    if (error.status === 401) {
      this.errorMessage = 'Invalid username or password. Please try again.';
    } else if (error.status === 403) {
      this.errorMessage = 'Account is locked or suspended. Please contact support.';
    } else if (error.status === 404) {
      this.errorMessage = 'Login service not found or endpoint is incorrect. Please contact support.';
    } else if (error.status === 0) {
      this.errorMessage = 'Unable to connect to server. Please check your connection.';
    } else {
      this.errorMessage = 'Login failed. Please try again later.';
    }
  }

  /**
   * Marks all form controls as touched to trigger validation display
   */
  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach((key: string) => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  /**
   * Toggles password visibility
   */
  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
  /**
   * Gets specific field error message for display
   */
  getFieldErrorMessage(fieldName: string): string {
    const field = this.loginForm.get(fieldName);

    if (field?.errors && field.touched) {
      if (field.errors['required']) {
        return `${this.getFieldDisplayName(fieldName)} is required`;
      }
      // Remove email validation error
      // if (fieldName === 'email' && field.errors['email']) {
      //   return `Invalid email address`;
      // }
      if (fieldName === 'email' && field.errors['maxlength']) {
        const maxLength = field.errors['maxlength'].requiredLength;
        return `Email cannot exceed ${maxLength} characters`;
      }
      if (field.errors['minlength']) {
        const minLength = field.errors['minlength'].requiredLength;
        return `${this.getFieldDisplayName(fieldName)} must be at least ${minLength} characters`;
      }
      if (field.errors && field.errors['maxlength']) {
        const maxLength = field.errors['maxlength'].requiredLength;
        return `${this.getFieldDisplayName(fieldName)} cannot exceed ${maxLength} characters`;
      }
    }

    return '';
  }
  /**
   * Gets user-friendly field display name
   */
  private getFieldDisplayName(fieldName: string): string {
    const displayNames: { [key: string]: string } = {
      email: 'Email',
      password: 'Password'
    };
    return displayNames[fieldName] || fieldName;
  }

  /**
   * Checks if a specific field has errors and should show them
   */
  hasFieldError(fieldName: string): boolean {
    const field = this.loginForm.get(fieldName);
    return !!(field?.errors && field.touched);
  }

  /**
   * Gets loading state from loading service
   */
  get isLoading(): boolean {
    return this.loadingService.isLoading();
  }

  /**
   * Handles forgot password functionality
   */
  onForgotPassword(): void {
    // Navigate to forgot password component (to be implemented)
    this.router.navigate(['/auth/forgot-password']);
  }

  /**
   * Handles navigation to registration page
   */
  onRegister(): void {
    // Navigate to registration component (to be implemented)
    this.router.navigate(['/auth/register']);
  }
}
