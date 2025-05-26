import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../../environments/environment';

/**
 * Logging Interceptor
 * Logs HTTP requests and responses for development
 */
@Injectable()
export class LoggingInterceptor implements HttpInterceptor {

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Only log in development mode
    if (!environment.production) {
      const startTime = Date.now();

      console.group(`🌐 HTTP ${req.method} ${req.url}`);
      console.log('Request:', req);

      return next.handle(req).pipe(
        tap({
          next: (event) => {
            if (event.type === 4) { // HttpEventType.Response
              const endTime = Date.now();
              const duration = endTime - startTime;
              console.log(`✅ Response (${duration}ms):`, event);
              console.groupEnd();
            }
          },
          error: (error) => {
            const endTime = Date.now();
            const duration = endTime - startTime;
            console.log(`❌ Error (${duration}ms):`, error);
            console.groupEnd();
          }
        })
      );
    }

    return next.handle(req);
  }
}
