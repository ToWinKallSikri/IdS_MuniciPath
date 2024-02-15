import { Component } from '@angular/core';
import { LogService } from '../services/log.service';
import { SharedService } from '../services/shared.service';
import { firstValueFrom } from 'rxjs';
import { StaffService } from '../services/staff.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup } from '@angular/forms';


@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrl: './staff.component.scss'
})

export class StaffComponent {
  cityId! : string;
  myForm: FormGroup;


  constructor(private staffService : StaffService, private route : ActivatedRoute){
      this.myForm = new FormGroup({ 
        txtToSet: new FormControl(),
        txtRole: new FormControl()
      });
      this.route.url.subscribe((url)=> this.cityId = url[1].toString());
  }

  public setRole() {
    if(this.myForm.valid){
      console.log(this.myForm.value.txtToSet);
      console.log(this.myForm.value.txtRole);
      console.log(this.cityId);
      this.staffService.setRole(this.myForm.value.txtToSet, this.myForm.value.txtRole, this.cityId).subscribe({
        next: (result) => {
          alert('Autorizzazione aggiornata');
        },
        error: (error) => 
        alert('Dati inseriti non validi.')});
    } else alert('Compila tutti i campi.');
  }
  
}

