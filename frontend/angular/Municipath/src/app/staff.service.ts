import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders  } from '@angular/common/http';
import { SharedService } from './shared.service';
import { WebResponse } from './Response';
import { environment } from '../environments/environment.development';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StaffService {

  constructor(private HttpClient : HttpClient, private cookieService : SharedService) {}

  public setRole(toSet : string, role : string, cityId : string) : Observable<WebResponse>{
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    let params = new HttpParams().append('toSet',toSet).append('role', role);
    return this.HttpClient.put<WebResponse>(environment.baseUrl+'/api/v1/city/'+cityId+'/staff/setRole', { headers: header, params: params })
    .pipe(catchError(error => throwError(() => error)));
  }
}
