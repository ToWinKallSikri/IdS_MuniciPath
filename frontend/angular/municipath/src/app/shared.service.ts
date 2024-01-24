import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  constructor(private cookieService: CookieService) { }

  public get(name : string) : string{
    return this.cookieService.get(name);
  }

  public set(name : string, value : string) {
    this.cookieService.set(name, value);
  }

  public delete(name : string) {
    this.cookieService.delete(name);
  }

  public check(name : string) : boolean{
    return this.cookieService.check(name);
  }
}
