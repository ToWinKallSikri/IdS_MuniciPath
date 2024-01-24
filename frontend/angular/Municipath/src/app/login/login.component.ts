import { Component,  } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LogService } from '../log.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  myForm: FormGroup; 
  
  constructor( private cookieService: CookieService, private router: Router, private logService : LogService) {
    this.myForm = new FormGroup({ 
      txtUsername: new FormControl(),
      txtPassword: new FormControl()
    });
  }


  login() {
    if(this.myForm.valid){
    this.logService.login(this.myForm.value.txtUsername, this.myForm.value.txtPassword).subscribe({
      next: (lol) => {
        this.cookieService.set('jwt', lol.reponse);
        this.router.navigateByUrl('/');
      },
      error: (error) => alert('Dati inseriti non validi.')});
    } else alert('Compila tutti i campi.');
 }

}
