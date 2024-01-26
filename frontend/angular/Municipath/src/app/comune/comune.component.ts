import { Component, AfterViewInit, ViewEncapsulation } from '@angular/core';
import { City } from '../City';
import { ActivatedRoute, Router } from '@angular/router';
import { ComuneService } from '../comune.service';
import { Point } from '../Point';
import { PointService } from '../point.service';
import * as L from 'leaflet';

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
  private map: any;
  marker: any;

  authority = 'https://i.postimg.cc/GpP8xRfs/Authority.png';
  event = 'https://i.postimg.cc/q7H6Kq1T/Event.png';
  health = 'https://i.postimg.cc/7Yc2xm7b/Health.png';
  social = 'https://i.postimg.cc/RFM3P8rp/Social.png';
  turistic = 'https://i.postimg.cc/QxV7GRFR/Turistic.png';
  empty = 'https://i.postimg.cc/ZngYcZfq/immagine-2024-01-24-113850127-png.png';

  constructor(private route : ActivatedRoute, private comuneService : ComuneService, 
    private router : Router, private pointService : PointService) {}

  ngAfterViewInit(): void {
    this.route.params.subscribe((params) => {
      this.comuneService.getCity(params['id']).subscribe((comuniBE) => {
        this.comune = this.comuneService.makeCity(comuniBE);
        if(this.comune == undefined){
          this.router.navigateByUrl("/Error/404");
        }
        this.pointService.getPoints(params['id']).subscribe((poi) => {
          this.points = poi;
          this.map = L.map('map').setView([this.comune.pos.lat, this.comune.pos.lng], 12);
          L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
          this.map.setMaxZoom(20).setMinZoom(12).setMaxBounds([
            [47.0, 6.0],
            [35.0, 19.0]
          ]);
          this.map.on('click', (event: any) => {
            this.addEmptyMarker(event.latlng.lat, event.latlng.lng);
          });
          this.points.forEach(p => {
            if(this.comune.pos.lat === p.pos.lat && this.comune.pos.lng === p.pos.lng) 
              this.addPrimeMarker(this.comune.pos.lat, this.comune.pos.lng);
            else this.addMarker(p.pos.lat, p.pos.lng);
            console.log(this.comune.pos);
            console.log(p.pos);
          });
        });
      });
    })
  }

  private addPrimeMarker(lat: number, lng: number): void {
    var myIcon = L.icon({
      iconUrl: this.authority,
      iconSize: [42, 60],
      popupAnchor: [0, -26]
  });
    const marker = L.marker([lat, lng], {icon:myIcon}).addTo(this.map);
    marker.bindPopup(`<p> ciao </p>`,  {closeButton: false})
    
    marker.on('click', (event: any) => {
      marker.openPopup();
    });
  }


  private addMarker(lat: number, lng: number): void {
    var myIcon = L.icon({
      iconUrl: this.getMarker(),
      iconSize: [28, 40],
      popupAnchor: [0, -26]
  });
    const marker = L.marker([lat, lng], {icon:myIcon}).addTo(this.map);
    marker.bindPopup(`<p> ciao </p>`,  {closeButton: false})
    
    marker.on('click', (event: any) => {
      marker.openPopup();
    });
  }

  private getMarker() : string {
    switch (Math.round(Math.random()*5)) {
      case 0: return this.authority;
      case 1: return this.event;
      case 2: return this.health;
      case 3: return this.social;
      default: return this.turistic;
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
  }

}
