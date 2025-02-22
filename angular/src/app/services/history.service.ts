import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable,throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {
  private baseUrl = 'http://localhost:8080/api/v1/coin/history';

  constructor(private http: HttpClient) {}

   getHistory(code: string, fromDate: string, toDate: string): Observable<any> {
    return this.http.get(`${this.baseUrl}?code=${code}&from=${fromDate}&to=${toDate}`).pipe(
      catchError(this.handleError)
    );
  }
  
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';

    if (error.status === 400) {
      errorMessage = 'Invalid date range or missing parameters.';
    } else if (error.status === 500) {
      errorMessage = 'Server error. Please try again later.';
    } else if (error.status === 0) {
      errorMessage = 'Unable to connect to the server. Check your internet connection.';
    }

    return throwError(() => new Error(errorMessage));
  }
}
