import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { LoadingService } from '../services/loading.service';

/**
 * Loading Interceptor
 * Tracks API calls and manages global loading state
 */
@Injectable()
export class LoadingInterceptor implements HttpInterceptor {

  constructor(private loadingService: LoadingService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Start loading
    this.loadingService.setLoading(true);

    return next.handle(req).pipe(
      finalize(() => {
        // Stop loading when request completes
        this.loadingService.setLoading(false);
      })
    );
  }
}
