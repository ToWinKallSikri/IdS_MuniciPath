import { Component } from '@angular/core';
import { LogService } from '../log.service';
import { SharedService } from '../shared.service';
import { firstValueFrom } from 'rxjs';
import { StaffService } from '../staff.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup } from '@angular/forms';


@Component({
  selector: 'app-staff',
  templateUrl: './staff.component.html',
  styleUrl: './staff.component.scss'
})

export class StaffComponent {
  cityId! : string;
  role! : string;
  toSet! : string;
  myForm: FormGroup;


  constructor(private staffService : StaffService, private sharedService : SharedService
    , private route : ActivatedRoute, private router: Router){
      this.myForm = new FormGroup({ 
        txtToSet: new FormControl(),
        txtRole: new FormControl()
      });
      this.route.url.subscribe((url)=> this.cityId = url[1].toString());
    console.log(this.cityId);
  }

  public setRole() {
    if(this.myForm.valid){
      this.staffService.setRole(this.toSet, this.role, this.cityId).subscribe({
        next: (result) => {
          alert('Autorizzazione aggiornata');
          this.router.navigateByUrl('/city/'+this.cityId+'/staff');
        },
        error: (error) => 
        alert('Dati inseriti non validi.')});
    } else alert('Compila tutti i campi.');
  }
  
}

