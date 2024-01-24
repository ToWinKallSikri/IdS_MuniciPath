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
  like:boolean= false;

  constructor(private router: Router, private cookieService: CookieService) {}

  isHomePage(): boolean {
    return this.router.url === '/';
  }

  isLogged() : boolean{
    return this.cookieService.check('jwt');
  }

  logout(){
    this.cookieService.delete('jwt');
    this.router.navigateByUrl('/');
  }

  share(){
    alert("Contenuto condiviso!")
  }
  check(){
    console.log(this.cookieService.check('jwt'));
  }
}
