import { Component, Input } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { MediaService } from '../media.service';

@Component({
  selector: 'app-pictures',
  templateUrl: './pictures.component.html',
  styleUrl: './pictures.component.scss'
})
export class PicturesComponent {
  @Input() pictures: string[] = [];
  index = 0;
  media : File[] = [];

  constructor(private mediaService : MediaService){
    this.mediaService.getFiles(this.pictures).subscribe((pics) =>{
      this.media = pics;
      this.media.forEach(f => console.log(f.webkitRelativePath));
    });
  }
}
