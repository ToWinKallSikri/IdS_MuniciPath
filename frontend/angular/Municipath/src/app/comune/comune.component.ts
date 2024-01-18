import { Component } from '@angular/core';
import { Comune } from '../Comune';
import { ActivatedRoute, Router } from '@angular/router';
import { ComuneService } from '../comune.service';

@Component({
  selector: 'app-comune',
  templateUrl: './comune.component.html',
  styleUrl: './comune.component.scss'
})
export class ComuneComponent {
  public comune: Comune | undefined;

  constructor(private route : ActivatedRoute, private comuneService : ComuneService, private router : Router) {
    this.route.params.subscribe(params => {
      comuneService.getComune(params['id']).subscribe((comuniBE) => {
        console.log(comuniBE);
        this.comune = comuniBE;
        if(this.comune == undefined){
          router.navigateByUrl("/Error/404");
        }
      });
    });
    
  }
}
