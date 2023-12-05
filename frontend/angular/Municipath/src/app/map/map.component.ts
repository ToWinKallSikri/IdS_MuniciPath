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

  ngOnInit(): void {
    this.map = L.map('map').setView([43.13748102156574, 13.070254325866701], 15);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
    this.map.on('click', (event: any) => {
      this.addMaker(event.latlng.lat, event.latlng.lng);
    });
  }

  private addMaker(lat: number, lng: number): void {
    const marker = L.marker([lat, lng]).addTo(this.map).on('click', (event: any) => {
      this.removeAllCircles();
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
