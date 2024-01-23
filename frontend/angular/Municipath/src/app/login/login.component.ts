import { Component,  } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  myForm: FormGroup; 
  constructor() {
    this.myForm = new FormGroup({ 
      txtUsername: new FormControl(),
      txtPassword: new FormControl()
    });
  }

  visualizzaArticolo() {
    console.log(this.myForm.value);
 }

}
