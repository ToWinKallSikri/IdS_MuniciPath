import { Component } from '@angular/core';
import { ComuniService } from '../comuni.service';
import { Comune } from '../Comune';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  public comuni: Comune[] = [];

  constructor(private comuniService : ComuniService) {
    comuniService.getComuni().subscribe((comuniBE) => {
      console.log(comuniBE);
      this.comuni = comuniBE;
    });
  }
}
