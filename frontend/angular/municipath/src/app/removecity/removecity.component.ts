import { Component } from '@angular/core';
import { City } from '../models/City';
import { ActivatedRoute, Router } from '@angular/router';
import { ComuneService } from '../services/comune.service';
import { SharedService } from '../services/shared.service';


@Component({
  selector: 'app-removecity',
  templateUrl: './removecity.component.html',
  styleUrl: './removecity.component.scss'
})
export class RemovecityComponent {
  public comune?: City;

  constructor(private route : ActivatedRoute, private comuneService : ComuneService, private router : Router,
    private cookieService: SharedService) {
    this.route.params.subscribe(params => {
      comuneService.getCity(params['id']).subscribe((comuniBE) => {
        this.comune = this.comuneService.makeCity(comuniBE);
        if(this.comune == undefined){
          router.navigateByUrl("/Error/404");
        }
        console.log(this.comune);
      });
    });
  }

  public delete(){
    let id = this.comune ? this.comune.id : '';
    this.comuneService.deleteCity(this.cookieService.get('jwt'), id).subscribe({
      next: (result) => {
        alert('Comune Eliminato.');
        this.router.navigateByUrl('/');
      },
      error: (error) => 
      alert('Operazione non riuscita.')});
      this.router.navigateByUrl('/');
  }
}
