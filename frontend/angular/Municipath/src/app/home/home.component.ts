import { Component } from '@angular/core';
import { ComuneService } from '../comune.service';
import { City } from '../City';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  public comuni: City[] = [];

  constructor(private route : ActivatedRoute, comuneService : ComuneService, private router : Router) {
    route.queryParams.subscribe(params => {
      let id = params['id'] ? params['id'] : '';
      comuneService.getCities(id).subscribe((comuniBE) => {
        console.log(comuniBE);
        this.comuni = comuniBE;
      });
    });
  }

}
