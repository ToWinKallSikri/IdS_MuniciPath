import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { MakecityService } from '../makecity.service';
import { Position } from '../Position';

@Component({
  selector: 'app-makecity',
  templateUrl: './makecity.component.html',
  styleUrl: './makecity.component.scss'
})
export class MakecityComponent {
  myForm: FormGroup;
  pos!: Position;
  
  constructor( private cookieService: CookieService, private router: Router, 
    private mkService : MakecityService, private route : ActivatedRoute) {
    this.myForm = new FormGroup({ 
      txtNomeDelComune: new FormControl(),
      txtCuratore: new FormControl(),
      txtCap : new FormControl(),
    });
    this.route.params.subscribe(params => {
      this.pos = {
        lat : params['lat'],
        lng : params['lng']
      }
    });
  }

  salva() {
    if(this.myForm.valid){
      let city = {
        cityName : this.myForm.value.txtNomeDelComune,
        curator : this.myForm.value.txtCuratore,
	      cap : this.myForm.value.txtCap,
        pos : this.pos
      };
      console.log(city);
      this.mkService.createCity(this.cookieService.get('jwt'), city).subscribe({
        next: (result) => {
          alert('Account Creato.');
          this.router.navigateByUrl('/');
        },
        error: (error) => 
        alert('Dati inseriti non validi.')});
    } else alert('Compila tutti i campi.');
  }

}
