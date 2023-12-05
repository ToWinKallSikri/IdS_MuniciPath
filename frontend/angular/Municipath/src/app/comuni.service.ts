import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comune } from './Comune';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root'
})
export class ComuniService {

  constructor(private HttpClient: HttpClient) { }

  public getComuni() : Observable<Comune[]> {
    return this.HttpClient.get<Comune[]>(environment.baseUrl + "/api/comuni");
  }
}
