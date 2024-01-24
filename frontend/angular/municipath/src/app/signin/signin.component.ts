import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 
import { Router } from '@angular/router';
import { LogService } from '../log.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss'
})
export class SigninComponent {
  myForm: FormGroup; 

  constructor(private router: Router, private logService : LogService) {
  this.myForm = new FormGroup({ 
    txtUsername: new FormControl(),
    txtPassword: new FormControl()
  });
}


signin() {
  if(this.myForm.valid){
  this.logService.signin(this.myForm.value.txtUsername, this.myForm.value.txtPassword).subscribe({
    next: (lol) => {
      alert('Account Creato.');
      this.router.navigateByUrl('/');
    },
    error: (error) => alert('Dati inseriti non validi.')});
  } else alert('Compila tutti i campi.');
}
}
