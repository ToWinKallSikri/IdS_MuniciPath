import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedService } from './shared.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'MuniciPath';
  like:boolean= false;

  constructor(private router: Router, private cookieService: SharedService) {}

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
