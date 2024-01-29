import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SharedService } from './shared.service';
import { CheckService } from './check.service';
import { ChangeAccountService } from './change-account.service';
import { IsStaffService } from './is-staff.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'MuniciPath';
  like = false;
  isManager =false;
  isStaff = false;
  currentUrl : any = '';

  constructor(private router: Router, private cookieService: SharedService,
     private checkService : CheckService, private changeAccount : ChangeAccountService, 
     private isStaffService: IsStaffService) {
    this.changeManager();
    this.changeAccount.eventState.subscribe(data => {
      this.changeManager();
    });
    this.isStaffService.eventState.subscribe(data =>{
      this.currentUrl = data;
      this.checkStaff();
    })
  }

  private checkStaff(){
    this.checkService.getRole(this.currentUrl._value[1]).subscribe((role)=>{
      this.isStaff = role.response === 'CURATOR' || role.response === 'MODERATOR';
    })
  }

  changeManager(){
    this.checkService.checkManager().subscribe((wr) => {
      this.isManager = wr.response == 'true';
    })
  }

  isHomePage(): boolean {
    return this.router.url === '/' || this.router.url.startsWith('/?id=');
  }

  isCityPage(): boolean{
    return this.router.url.startsWith('/city/');
  }

  isValidation() : boolean {
    return this.router.url === '/accountValidation';
  }

  isLogged() : boolean{
    return this.cookieService.check('jwt');
  }

  logout(){
    this.cookieService.delete('jwt');
    this.isManager = false;
  }

  share(){
    alert("Contenuto condiviso!")
  }
  check(){
    console.log(this.cookieService.check('jwt'));
  }

  navigateToStaff() {
    this.router.navigate(['/city/' + this.currentUrl._value[1] + '/staff']);
  }

}
