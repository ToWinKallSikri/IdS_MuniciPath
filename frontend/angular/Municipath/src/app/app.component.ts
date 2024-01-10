import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Municipath';

  like:boolean= false;

  constructor(private router: Router) {}

  isHomePage(): boolean {
    return this.router.url === '/';
  }

  liked(){
    this.like = !this.like;
  }

  share(){
    alert("Contenuto condiviso!")
  }
}
