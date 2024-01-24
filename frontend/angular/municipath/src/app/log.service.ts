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
    return this.HttpClient.post<WebResponse>(environment.baseUrl+'/api/v1/signin', data).pipe(
      catchError(error => throwError(() => error)));
  }

  public login(username : string, password : string) : Observable<WebResponse> {
    let data = {
      username : username,
      password : password
    }
    console.log(data);
    return this.HttpClient.post<WebResponse>(environment.baseUrl + '/api/v1/login', data).pipe(
      catchError(error => throwError(() => error)));
  }
}
