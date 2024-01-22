import { Injectable } from '@angular/core';
import { HttpClient, HttpParams  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { City } from './City';
import { environment } from '../environments/environment.development';


@Injectable({
  providedIn: 'root'
})
export class ComuneService {
  
  constructor(private HttpClient: HttpClient) { }

  public getCities(id : string) : Observable<City[]> {
    let params = new HttpParams().append('cityName', id);
    return this.HttpClient.get<City[]>(environment.baseUrl+'/api/v1/cities', { params: params });
  }

  public getCity(id : string) : Observable<City>{
    let params = new HttpParams().append('cityId', id);
    return this.HttpClient.get<City>(environment.baseUrl + '/api/v1/city', { params: params });
  }
  
}
