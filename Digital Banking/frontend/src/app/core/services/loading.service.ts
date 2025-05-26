import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

/**
 * Loading Service
 * Manages global loading state across the application
 */
@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private loadingCount = 0;

  public loading$ = this.loadingSubject.asObservable();

  /**
   * Set loading state
   */
  setLoading(loading: boolean): void {
    if (loading) {
      this.loadingCount++;
    } else {
      this.loadingCount = Math.max(0, this.loadingCount - 1);
    }

    // Only emit false when all requests are complete
    this.loadingSubject.next(this.loadingCount > 0);
  }

  /**
   * Get current loading state
   */
  isLoading(): boolean {
    return this.loadingSubject.value;
  }

  /**
   * Force stop loading
   */
  stopLoading(): void {
    this.loadingCount = 0;
    this.loadingSubject.next(false);
  }
}
