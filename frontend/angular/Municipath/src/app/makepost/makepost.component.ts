import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router';
import { Position } from '../Position';
import { PointService } from '../point.service';
import { Validators } from '@angular/forms';
import { MediaService } from '../media.service';
import { firstValueFrom } from 'rxjs';


@Component({
  selector: 'app-makepost',
  templateUrl: './makepost.component.html',
  styleUrl: './makepost.component.scss'
})
export class MakepostComponent {

  postForm: FormGroup;
  pos!: Position;
  cityId!: string;
  persist: FormControl<any>;
  filesToUpload: File[] = [];
  
  constructor(private router: Router, private pointService : PointService,
     private route : ActivatedRoute, private mediaService : MediaService) {
    this.persist = new FormControl();
    this.postForm = new FormGroup({ 
      txtTitle: new FormControl(),
      txtType: new FormControl(),
      txtText : new FormControl(),
      multimediaData : new FormControl(),
      startDate : new FormControl(),
      endDate : new FormControl(),
      persist : this.persist
    });
    this.route.params.subscribe(params => {
      this.cityId = params['cityId'];
      this.pos = {
        lat : params['lat'],
        lng : params['lng']
      }
    });
  }

  onFileChange(event: any): void {
    this.filesToUpload = event.target.files;
  }

  clearFile(){
    this.filesToUpload = [];
  }

  showEnd() : boolean{
    let type = this.postForm.value.txtType;
    if(type) return type == 'EVENT' || type == 'CONTEST';
    return false;
  }

  showStart(){
    let type = this.postForm.value.txtType;
    if(type) return type == 'EVENT';
    return false;
  }

  toggle() {
    if (this.showStart()) {
      this.postForm.controls['startDate'].setValidators([Validators.required]);
    } else {
      this.postForm.controls['startDate'].setValidators(null);
    }
    if (this.showEnd()) {
      this.postForm.controls['endDate'].setValidators([Validators.required]);
    } else {
      this.postForm.controls['endDate'].setValidators(null);
    }
    this.postForm.controls['startDate'].updateValueAndValidity();
    this.postForm.controls['endDate'].updateValueAndValidity();
  }

  async save() {
    console.log(this.filesToUpload)
    if(this.postForm.valid){
      let list = this.filesToUpload.length == 0 ? [] : await firstValueFrom(this.mediaService.getPath(this.filesToUpload));
      let data = {
        title: this.postForm.value.txtTitle,
        type: this.postForm.value.txtType,
        text: this.postForm.value.txtText,
        multimediaData: list,
        startTime: this.showStart() ? this.postForm.value.startDate : null,
        endTime: this.showEnd() ? this.postForm.value.endDate : null,
        persistence: this.showStart() ? this.postForm.value.persist : true,
      }
      this.pointService.createPost(this.cityId, this.pos, data).subscribe({
        next: (result) => {
          alert('Post Creato.');
          this.router.navigateByUrl('/city/'+this.cityId);
        },
        error: (error) => 
        alert('Dati inseriti non validi.')
      });
    } else alert('Compila tutti i campi.');
  }
}
