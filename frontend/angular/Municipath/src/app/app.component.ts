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
  jwt : string = '?';
  like:boolean= false;

  constructor(private router: Router, private cookieService: CookieService) {
    this.jwt = this.cookieService.check('jwt') ?  cookieService.get('jwt') : '?';
  }

  isHomePage(): boolean {
    return this.router.url === '/';
  }

  isLogged() : boolean{
    return this.jwt != '?';
  }

  liked(){
    this.like = !this.like;
  }

  share(){
    alert("Contenuto condiviso!")
  }
  check(){
    console.log(this.cookieService.check('jwt'));
  }
}
