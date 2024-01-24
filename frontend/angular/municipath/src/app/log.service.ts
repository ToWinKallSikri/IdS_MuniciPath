import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from '../environments/environment.development';
import { catchError } from 'rxjs/operators';
import { WebResponse } from './Response';

@Injectable({
  providedIn: 'root'
})
export class LogService {

  constructor(private HttpClient: HttpClient) { }

  public signin(username : string, password : string) : Observable<WebResponse> {
    let data = {
      username : username,
      password : password
    }
    return this.HttpClient.post<WebResponse>(environment.baseUrl+'/api/v1/signin', data
    ).pipe(catchError(error => throwError(() => error)));
  }

  public login(username : string, password : string) : Observable<WebResponse> {
    let data = {
      username : username,
      password : password
    }
    return this.HttpClient.post<WebResponse>(environment.baseUrl + '/api/v1/login', data)
    .pipe(catchError(error => throwError(() => error)));
  }

  public getToValidate(jwt : string) : Observable<string[]> {
    let header = new HttpHeaders().append('auth', jwt);
    return this.HttpClient.get<string[]>(environment.baseUrl + '/api/v1/manager/getUsersNotConvalidated', {headers: header});
  }

  public validate(jwt : string, username: string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', jwt);
    let param = new HttpParams().append('toValidate', username)
    return this.HttpClient.put<WebResponse>(environment.baseUrl + '/api/v1/manager/convalidate', "", {headers: header, params: param})
    .pipe(catchError(error => throwError(() => error)));
  }
}
