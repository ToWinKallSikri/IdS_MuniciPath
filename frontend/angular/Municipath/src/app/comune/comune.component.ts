import { Component } from '@angular/core';
import { City } from '../City';
import { ActivatedRoute, Router } from '@angular/router';
import { ComuneService } from '../comune.service';

@Component({
  selector: 'app-comune',
  templateUrl: './comune.component.html',
  styleUrl: './comune.component.scss'
})
export class ComuneComponent {
  public comune?: City;

  constructor(private route : ActivatedRoute, private comuneService : ComuneService, private router : Router) {
    this.route.params.subscribe(params => {
      comuneService.getCity(params['id']).subscribe((comuniBE) => {
        this.comune = this.comuneService.makeCity(comuniBE);
        if(this.comune == undefined){
          router.navigateByUrl("/Error/404");
        }
      });
    });
    
  }
}
