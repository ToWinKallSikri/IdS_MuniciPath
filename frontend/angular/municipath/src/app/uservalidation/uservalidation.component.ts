import { Component } from '@angular/core';
import { LogService } from '../services/log.service';
import { SharedService } from '../services/shared.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-uservalidation',
  templateUrl: './uservalidation.component.html',
  styleUrl: './uservalidation.component.scss'
})
export class UservalidationComponent {
  users : string[] = []

  constructor(private logService : LogService, private sharedService : SharedService){
    this.reload();
  }

  public validate(username : string){
    this.logService.validate(this.sharedService.get('jwt'), username).subscribe({
      next: (wr) =>{
        alert('Utente convalidato');
        this.reload();
      },
      error: (error)=> {alert('Operazione fallita');}})
  }

  private reload(){
    this.logService.getToValidate(this.sharedService.get('jwt')).subscribe({
      next: (list) =>{this.users = list;},
      error: (error)=> {}})
  }

}
