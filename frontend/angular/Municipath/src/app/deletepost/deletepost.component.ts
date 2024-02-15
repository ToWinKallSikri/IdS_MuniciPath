import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PointService } from '../services/point.service';

@Component({
  selector: 'app-deletepost',
  templateUrl: './deletepost.component.html',
  styleUrl: './deletepost.component.scss'
})
export class DeletepostComponent {
  postId! : string;
  cityId!: string;
  title : string = '';

  
  constructor(private router: Router, private pointService : PointService,
     private route : ActivatedRoute) {
    this.route.queryParams.subscribe(params => {
      this.postId = params['postId'];
      this.cityId = this.postId.split(".")[0];
      this.pointService.getPost (this.cityId, this.postId).subscribe({
        next: (post) => {this.title = post.title;},
        error: (error) => {this.router.navigateByUrl('/Error/404');}
      }
    )});
  }

  async delete() {
    this.pointService.deletePost(this.cityId, this.postId).subscribe({
      next: (result) => {
        alert('Post Eliminato.');
        this.router.navigateByUrl('/city/'+this.cityId);
      },
      error: (error) => 
      alert('Qualcosa è andato storto :/')
    });
  }
}
