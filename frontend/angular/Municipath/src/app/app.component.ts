import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'MuniciPath';
  jwt : string = '';
  like:boolean= false;

  constructor(private router: Router, private cookieService: CookieService) {
    this.jwt = cookieService.get('jwt');
  }

  isHomePage(): boolean {
    return this.router.url === '/';
  }

  liked(){
    this.like = !this.like;
  }

  share(){
    alert("Contenuto condiviso!")
  }
}
