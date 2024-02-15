import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { City } from '../models/City';
import { environment } from '../../environments/environment.development';
import { catchError } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class ComuneService {
  
  constructor(private HttpClient: HttpClient) { }

  public getCities(id : string) : Observable<any[]> {
    let params = new HttpParams().append('cityName', id);
    return this.HttpClient.get<any[]>(environment.baseUrl+'/api/v1/cities', { params: params });
  }
  
  public getCity(id : string) : Observable<City>{
    let params = new HttpParams().append('cityId', id);
    return this.HttpClient.get<any>(environment.baseUrl + '/api/v1/city', { params: params });
  }

  public createCity(jwt : string, pc : any) : Observable<any> {
    let header = new HttpHeaders().append('auth', jwt);
    return this.HttpClient.post<any>(environment.baseUrl + '/api/v1/manager/createCity', pc, {headers : header})
    .pipe(catchError(error => throwError(() => error)));
  }

  public updateCity(jwt : string, pc : any, cityId : string) : Observable<any> {
    let header = new HttpHeaders().append('auth', jwt);
    let param = new HttpParams().append('cityId', cityId);
    return this.HttpClient.put<any>(environment.baseUrl + '/api/v1/manager/updateCity',pc , {headers : header, params: param})
    .pipe(catchError(error => throwError(() => error)));
  }

  public deleteCity(jwt : string, cityId : string) : Observable<any> {
    let header = new HttpHeaders().append('auth', jwt);
    let param = new HttpParams().append('cityId', cityId);
    return this.HttpClient.delete<any>(environment.baseUrl + '/api/v1/manager/deleteCity', {headers : header, params: param})
    .pipe(catchError(error => throwError(() => error)));
  }

  public makeCity(data : any) :City{
    return {
      name : data.name,
      curator : data.curator,
      pos : data.pos,
      id : data.id,
      cap : this.correctCap(data.cap)
    };
  }

  private correctCap(numero : number) : string{
    let stringa = numero.toString();
    while (stringa.length < 5) {
        stringa = '0' + stringa;
    }
    return stringa;
  }
  
}
