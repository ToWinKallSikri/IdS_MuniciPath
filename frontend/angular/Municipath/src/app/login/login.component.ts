import { Component,  } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { LogService } from '../log.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginForm: FormGroup; 
  signinForm: FormGroup; 
  isLogin = false;

  constructor(private route : ActivatedRoute, private cookieService: CookieService, private router: Router, private logService : LogService) {
    this.loginForm = new FormGroup({ 
      txtUsername: new FormControl(),
      txtPassword: new FormControl()
    });
    this.signinForm = new FormGroup({ 
      txtUsername: new FormControl(),
      txtPassword: new FormControl()
    });
    this.route.params.subscribe(params => {
      this.isLogin = params['id'] == 'login';
    });
  }


  login() {
    if(this.loginForm.valid){
    this.logService.login(this.loginForm.value.txtUsername, this.loginForm.value.txtPassword).subscribe({
      next: (lol) => {
        this.cookieService.set('jwt', lol.reponse);
        this.router.navigateByUrl('/');
      },
      error: (error) => alert('Dati inseriti non validi.')});
    } else alert('Compila tutti i campi.');
 }

 signin() {
  if(this.signinForm.valid){
  this.logService.signin(this.signinForm.value.txtUsername, this.signinForm.value.txtPassword).subscribe({
    next: (lol) => {
      alert('Account Creato.');
      this.router.navigateByUrl('/');
    },
    error: (error) => alert('Dati inseriti non validi.')});
  } else alert('Compila tutti i campi.');
}

}
