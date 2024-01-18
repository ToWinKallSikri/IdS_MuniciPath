import { Component } from '@angular/core';
import { ComuneService } from '../comune.service';
import { Comune } from '../Comune';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  public comuni: Comune[] = [];

  constructor(private comuneService : ComuneService) {
    comuneService.getComuni().subscribe((comuniBE) => {
      console.log(comuniBE);
      this.comuni = comuniBE;
    });
  }
}
