import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { environment } from '../environments/environment.development';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UpdatecityService {

  constructor(private HttpClient: HttpClient) { }

  public updateCity(jwt : string, pc : any, cityId : string) : Observable<any> {
    let header = new HttpHeaders().append('auth', jwt);
    return this.HttpClient.post<any>(environment.baseUrl + '/api/v1/manager/updateCity', {headers : header}, cityId, pc)
    .pipe(catchError(error => throwError(() => error)));
  }
}
