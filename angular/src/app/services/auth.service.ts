import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders,HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Observable, tap, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/authenticate';

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<any> {
    const credentials = btoa(`${username}:${password}`);
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Basic ${credentials}`   // Add Basic Auth header
    });
	const params = new HttpParams().set('username', username);
    return this.http.post(this.baseUrl, { }, { headers,params }).pipe(
      tap((response: any) => {
	  console.log("response",response)
        localStorage.setItem('access_token', response.access_token);
        this.router.navigate(['home']);
      })
    ).pipe(
      catchError(this.handleError) // Handle errors properly
    );;
  }
  
  logout(): void {
    localStorage.removeItem('access_token');
    this.router.navigate(['login']);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('access_token');
  }
  
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';

    if (error.status === 401) {
      errorMessage = 'Invalid username or password. Please try again.';
    } else if (error.status === 500) {
      errorMessage = 'Server error. Please try later.';
    } else if (error.status === 0) {
      errorMessage = 'Unable to connect to the server. Check your connection.';
    }

    return throwError(() => new Error(errorMessage));
  }

}
