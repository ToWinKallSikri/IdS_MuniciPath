import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedService } from './shared.service';
import { CheckService } from './check.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'MuniciPath';
  like:boolean= false;
  isManager:boolean=false;

  constructor(private router: Router, private cookieService: SharedService, private checkService : CheckService) {
    this.checkService.checkManager().subscribe((wr) => {
      this.isManager = wr.response == 'true';
    })
  }

  isHomePage(): boolean {
    return this.router.url === '/';
  }

  isLogged() : boolean{
    return this.cookieService.check('jwt');
  }

  logout(){
    this.cookieService.delete('jwt');
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate([this.router.url]);
  }

  share(){
    alert("Contenuto condiviso!")
  }
  check(){
    console.log(this.cookieService.check('jwt'));
  }

}
