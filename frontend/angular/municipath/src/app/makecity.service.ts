import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from '../environments/environment.development';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MakecityService {

  constructor(private HttpClient: HttpClient) { }

  public createCity(jwt : string, city : any) : Observable<any> {
    let header = new HttpHeaders().append('auth', jwt);
    return this.HttpClient.post<any>(environment.baseUrl + '/api/v1/manager/createCity', {headers : header}, city)
    .pipe(catchError(error => throwError(() => error)));
  }
}
