import { Component, OnInit, ViewEncapsulation  } from '@angular/core';
declare let L: any;

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class MapComponent implements OnInit {
  private map: any;
  private markers: any[] = [];
  authority = 'https://i.postimg.cc/GpP8xRfs/Authority.png';
  event = 'https://i.postimg.cc/q7H6Kq1T/Event.png'
  health = 'https://i.postimg.cc/7Yc2xm7b/Health.png'
  social = 'https://i.postimg.cc/RFM3P8rp/Social.png'
  turistic = 'https://i.postimg.cc/QxV7GRFR/Turistic.png'

  ngOnInit(): void {
    this.map = L.map('map').setView([44, 13], 5.5);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
    this.map.setMaxZoom(12);
    this.map.setMinZoom(5.5);
    this.map.setMaxBounds([
      [47.0, 6.0],
      [35.0, 19.0]
    ]);
    
    this.map.on('click', (event: any) => {
      this.addMaker(event.latlng.lat, event.latlng.lng);
      console.log(event.latlng);
    });
  }

  private addMaker(lat: number, lng: number): void {
    var myIcon = L.icon({
      iconUrl: this.authority,
      iconSize: [28, 40],
      iconAnchor: [22, 94],
      popupAnchor: [-3, -76]
  });
    const marker = L.marker([lat, lng], {icon:myIcon}).addTo(this.map).on('click', (event: any) => {
      this.removeAllCircles();
    });
    marker.bindPopup('<p style="text-align: center"><b>Camerino<br>62032</b></p>',  {closeButton: false})
    .on('mouseover', (event: any) => {
      marker.openPopup();
    });
    marker.on('mouseout', (event : any) =>{
      marker.closePopup();
    });
    this.markers.push(marker);
  }

  removeAllCircles(): void {
    for (const marker of this.markers) {
      this.map.removeLayer(marker);
    }
    this.markers = [];
  }
}
