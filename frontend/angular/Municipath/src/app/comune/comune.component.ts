import { Component, AfterViewInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ComuneService } from '../services/comune.service';
import { Point } from '../models/Point';
import { PointService } from '../services/point.service';
import * as L from 'leaflet';
import { Post } from '../models/Post';
import { formatDate } from '@angular/common';
import { CheckService } from '../services/check.service';
import { IsStaffService } from '../services/is-staff.service';
import { MediaService } from '../services/media.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-comune',
  templateUrl: './comune.component.html',
  styleUrl: './comune.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class ComuneComponent implements AfterViewInit {

  public comune: any;
  public points: any[] = [];
  public point: any;
  public post: any;
  private map: any;
  marker: any;
  inPost = false;
  canMake = false;
  canEdit = false;

  authority = 'https://i.postimg.cc/GpP8xRfs/Authority.png';
  event = 'https://i.postimg.cc/q7H6Kq1T/Event.png';
  health = 'https://i.postimg.cc/7Yc2xm7b/Health.png';
  social = 'https://i.postimg.cc/RFM3P8rp/Social.png';
  turistic = 'https://i.postimg.cc/QxV7GRFR/Turistic.png';
  empty = 'https://i.postimg.cc/ZngYcZfq/immagine-2024-01-24-113850127-png.png';
color: any;

  constructor(private route : ActivatedRoute, private comuneService : ComuneService, 
    private router : Router, private pointService : PointService,
     private checkService : CheckService, private isStaffService : IsStaffService) {
      this.isStaffService.publish(this.route.url);
    }

  ngAfterViewInit(): void {
    this.route.params.subscribe((params) => {
      this.comuneService.getCity(params['id']).subscribe({ 
        next: (comuniBE) => {
          this.comune = this.comuneService.makeCity(comuniBE);
          this.getAllPoint(params['id']);
        }, error: (error) => this.router.navigateByUrl("/Error/404")});
    });
  }

  private getAllPoint(id: string){
    this.pointService.getPoints(id).subscribe((poi) => {
      this.points = poi;
      this.map = L.map('map').setView([this.comune.pos.lat, this.comune.pos.lng], 14);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
      let dis = 0.11;
      this.map.setMaxZoom(20).setMinZoom(12).setMaxBounds([
        [this.comune.pos.lat + dis, this.comune.pos.lng + dis],
        [this.comune.pos.lat - dis, this.comune.pos.lng - dis]
      ]);
      this.map.on('click', (event: any) => {
        this.addEmptyMarker(event.latlng.lat, event.latlng.lng);
      });
      let prime : any;
      this.points.forEach(p => {
        if(this.comune.pos.lat === p.pos.lat && this.comune.pos.lng === p.pos.lng) {
          this.addPrimeMarker(p);
          prime = p;
        } else this.addMarker(p);
      });
      this.findPoint(prime, id);
    });
  }

  private findPoint(prime : any, id : string){
    this.route.queryParams.subscribe({next:(qParams) => {
      if(qParams['point']){
        let point = this.points.find(p => p.pointId === qParams['point']);
        if(point){
          this.point = this.sortPosts(point);
        } else this.router.navigateByUrl("/Error/404");
        if(qParams['post']){
          this.pointService.getPost(id, qParams['post']).subscribe({
            next: (post) => {
            this.post = post;
            this.inPost = true;
            this.checkService.havePowerWithIt(post.id).subscribe((wr)=> this.canEdit = wr.response === 'true');
          }, error: (error) => this.router.navigateByUrl("/Error/404")});
        } else this.inPost = false;
      } else this.point = prime;
      this.checkRole();
    }, error: (error) => this.router.navigateByUrl("/Error/404")});
  }

  private sortPosts(point : Point) : Point {
    point.posts = point.posts.sort((a, b) => new Date(b.publicationTime).getTime()-new Date(a.publicationTime).getTime());
    return point;
  }

  private checkRole(){
    this.checkService.getRole(this.comune.id).subscribe(wr => {
      let role = wr.response;
      this.canMake = role != 'LIMITED' && role != 'TOURIST';
    })
  }

  private addPrimeMarker(point : Point): void {
    var myIcon = L.icon({
      iconUrl: this.authority,
      iconSize: [42, 60],
      popupAnchor: [0, -26]
  });
    const marker = L.marker([point.pos.lat, point.pos.lng], {icon:myIcon}).addTo(this.map);
    marker.on('click', (event: any) => {
      this.router.navigateByUrl(`/city/${point.cityId}?point=${point.pointId}`);
    });
  }


  private addMarker(point : Point): void {
    var myIcon = L.icon({
      iconUrl: this.getMarker(point),
      iconSize: [28, 40],
      popupAnchor: [0, -26]
  });
    const marker = L.marker([point.pos.lat, point.pos.lng], {icon:myIcon}).addTo(this.map);
    
    marker.on('click', (event: any) => {
      this.router.navigateByUrl(`/city/${point.cityId}?point=${point.pointId}`);
    });
  }

  private getMarker(point : Point) : string {
    let list = [0, 0, 0, 0, 0];
    for(let post of point.posts){
      list[this.getTypeIndex(post)] += 1;
    }
    let result = 0;
    let count = 0;
    for(let i = 0; i < 5; i++){
      if(list[i] > count){
        count = list[i];
        result = i;
      }
    }
    switch (result) {
      case 0: return this.authority;
      case 1: return this.social;
      case 2: return this.turistic;
      case 3: return this.health;
      default: return this.event;
    }
  }

  getTypeIndex(post : Post) : number{
    switch (post.type) {
     case 'INSTITUTIONAL': return 0;
     case 'SOCIAL': return 1;
     case 'TOURISTIC': return 2;
     case 'HEALTHandWELLNESS': return 3 ;
     default: return 4;
   }
 }

  private addEmptyMarker(lat : number, lng: number){
    if(this.marker)
      this.map.removeLayer(this.marker);
    var myIcon = L.icon({
      iconUrl: this.empty,
      iconSize: [28, 40],
      popupAnchor: [0, -26]
    });
    this.marker = L.marker([lat, lng], {icon:myIcon}).addTo(this.map);
    if(this.canMake) {
      this.marker.bindPopup(`<button onClick="location.href='/makepost/${this.comune.id}/${lat}/${lng}'">Crea Post</button>`,  {closeButton: false})
      .on('click', (event: any) => {
        this.marker.openPopup();
      });
    }
  }

  getColor(post : Post) : string{
     switch (post.type) {
      case 'INSTITUTIONAL': return 'deepskyblue';
      case 'SOCIAL': return 'yellow';
      case 'TOURISTIC': return 'orange';
      case 'HEALTHandWELLNESS': return 'green' ;
      default: return 'red';
    }
  }

  getItalianName(post : Post) : string{
    switch (post.type) {
     case 'INSTITUTIONAL': return 'Istituzione';
     case 'SOCIAL': return 'Sociale';
     case 'TOURISTIC': return 'Turismo';
     case 'HEALTHandWELLNESS': return 'Salute e Benessere' ;
     case 'EVENT': return 'Evento' ;
     default: return 'Contest';
   }
 }

  rightFormatDate(date: Date) {
    return formatDate(date, 'HH:mm - dd/MM/yyyy', 'it-IT', 'Europe/Rome');
  }

}