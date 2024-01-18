import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comune } from './Comune';
import { environment } from '../environments/environment.development';


@Injectable({
  providedIn: 'root'
})
export class ComuneService {
  
  constructor(private HttpClient: HttpClient) { }

public getComuni() : Observable<Comune[]> {
  return this.HttpClient.get<Comune[]>(environment.baseUrl + "/api/v1/home");
}

public getComune(id : number) : Observable<Comune>{
  return this.HttpClient.get<Comune>(environment.baseUrl + "/api/v1/home/" + id);
}
}
