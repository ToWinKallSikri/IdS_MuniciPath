import { Component } from '@angular/core';
import { ComuneService } from '../comune.service';
import { City } from '../City';
import { ActivatedRoute, Router } from '@angular/router';
import { AfterViewInit, ViewEncapsulation   } from '@angular/core';
import * as L from 'leaflet';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements AfterViewInit  {
  public comuni: City[] = [];

  constructor(private route : ActivatedRoute, private comuneService : ComuneService, private router : Router) {
     }

  private map: any;
  private markers: any[] = [];
  authority = 'https://i.postimg.cc/GpP8xRfs/Authority.png';

  ngAfterViewInit(): void {
    this.route.queryParams.subscribe(params => {
      let id = params['id'] ? params['id'] : '';
      this.comuneService.getCities(id).subscribe((comuniBE) => {
        console.log(comuniBE);
        this.comuni = comuniBE;
        this.map = L.map('map').setView([44, 13], 5.5);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
        this.map.setMaxZoom(12);
        this.map.setMinZoom(5.5);
        this.map.setMaxBounds([
          [47.0, 6.0],
          [35.0, 19.0]
        ]);
        this.comuni.forEach(c =>  this.addMarker(c));
      });
    });
    
  }

  private getMarker() : string {
    return this.authority;
  }

  private addMarker(city: City): void {
    var myIcon = L.icon({
      iconUrl: this.getMarker(),
      iconSize: [28, 40],
      popupAnchor: [0, -26]
  });
    const marker = L.marker([city.pos.lat, city.pos.lng], {icon:myIcon}).addTo(this.map).on('click', (event: any) => {
      this.router.navigateByUrl("/"+city.id) ;

    });
    marker.bindPopup(`<p style="text-align: center"><b>${city.name}<br>${city.cap}</b></p>`,  {closeButton: false})
    .on('mouseover', (event: any) => {
      marker.openPopup();
    });
    marker.on('mouseout', (event : any) =>{
      marker.closePopup();
    });
    this.markers.push(marker);
  }

}
