import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedService } from './shared.service';
import { CheckService } from './check.service';
import { ChangeAccountService } from './change-account.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'MuniciPath';
  like:boolean= false;
  isManager:boolean=false;

  constructor(private router: Router, private cookieService: SharedService,
     private checkService : CheckService, private changeAccount : ChangeAccountService) {
    this.changeManager();
    this.changeAccount.eventState.subscribe(data => {
      this.changeManager();
    });
  }

  changeManager(){
    this.checkService.checkManager().subscribe((wr) => {
      this.isManager = wr.response == 'true';
    })
  }

  isHomePage(): boolean {
    return this.router.url === '/' || this.router.url.startsWith('/?id=');
  }

  isLogged() : boolean{
    return this.cookieService.check('jwt');
  }

  logout(){
    this.cookieService.delete('jwt');
    this.changeManager();
  }

  share(){
    alert("Contenuto condiviso!")
  }
  check(){
    console.log(this.cookieService.check('jwt'));
  }

}
