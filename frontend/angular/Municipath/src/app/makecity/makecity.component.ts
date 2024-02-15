import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router';
import { Position } from '../models/Position';
import { SharedService } from '../services/shared.service';
import { ComuneService } from '../services/comune.service';

@Component({
  selector: 'app-makecity',
  templateUrl: './makecity.component.html',
  styleUrl: './makecity.component.scss'
})
export class MakecityComponent {
  myForm: FormGroup;
  pos!: Position;
  
  constructor( private cookieService: SharedService, private router: Router, 
    private mkService : ComuneService, private route : ActivatedRoute) {
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
      this.mkService.createCity(this.cookieService.get('jwt'), city).subscribe({
        next: (result) => {
          alert('Comune Creato.');
          this.router.navigateByUrl('/');
        },
        error: (error) => 
        alert('Dati inseriti non validi.')});
    } else alert('Compila tutti i campi.');
  }

}
