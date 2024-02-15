import { Component} from '@angular/core';
import { MediaService } from '../services/media.service';
import { ActivatedRoute } from '@angular/router';
import { PointService } from '../services/point.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-pictures',
  templateUrl: './pictures.component.html',
  styleUrl: './pictures.component.scss'
})
export class PicturesComponent {
  index = 0;
  media : string[] = [];
  isImage = true;
  play = false;
  fileUrl : SafeUrl = '';

  constructor(private mediaService : MediaService, private route : ActivatedRoute,
     private poiService : PointService, private sanitizer : DomSanitizer){
    this.route.queryParams.subscribe((qparam) => {
      if(qparam['post'] !== undefined) {
        this.poiService.getPost(qparam['post'].split('.')[0], qparam['post']).subscribe(post => {
          if(post && post.multimediaData.length != 0) {
            this.media = post.multimediaData;
            this.setFile(this.media[0]);
          }
        });
      }
    });
  }

  private async setFile(path: string) {
    let file = await firstValueFrom(this.mediaService.getFile(path));
    if(file){
      let type = file.type.split('/')[0];
      if (type !== 'image') {
        this.isImage = false;
      }
      const url = window.URL.createObjectURL(file);
      this.fileUrl = this.sanitizer.bypassSecurityTrustUrl(url);
    }
  }

  getBack(){
    if(this.index > 0){
      this.setFile(this.media[--this.index]);
    }
  }

  getNext(){
    if(this.index < this.media.length -1){
      this.setFile(this.media[++this.index]);
    }
  }

}
