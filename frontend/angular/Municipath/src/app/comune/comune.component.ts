import { Component } from '@angular/core';
import { Comune } from '../Comune';
import { ComuniService } from '../comuni.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-comune',
  templateUrl: './comune.component.html',
  styleUrls: ['./comune.component.scss']
})
export class ComuneComponent {

  public comune: Comune | undefined;

  constructor(private route : ActivatedRoute, private comuniService : ComuniService, private router : Router) {
    this.route.params.subscribe(params => {
      comuniService.getComune(params['id']).subscribe((comuniBE) => {
        console.log(comuniBE);
        this.comune = comuniBE;
        if(this.comune == undefined){
          router.navigateByUrl("/Error/404");
        }
      });
    });
    
  }
}
