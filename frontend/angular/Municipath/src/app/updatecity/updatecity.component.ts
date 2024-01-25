import { Component, AfterViewInit, ViewEncapsulation } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router';
import { Position } from '../Position';
import { SharedService } from '../shared.service';
import { ComuneService } from '../comune.service';
import { City } from '../City';
import * as L from 'leaflet';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-updatecity',
  templateUrl: './updatecity.component.html',
  styleUrl: './updatecity.component.scss',
  encapsulation: ViewEncapsulation.None
})
export class UpdatecityComponent implements AfterViewInit {
  myForm: FormGroup;
  pos: Position = {lat: 0, lng: 0};
  public comuni: City[] = [];
  private map: any;
  private marker: any;
  city = 'https://i.postimg.cc/GpP8xRfs/Authority.png';
  empty = 'https://i.postimg.cc/ZngYcZfq/immagine-2024-01-24-113850127-png.png';
  name = '';
  cap = '';
  curator = '';
  
  constructor( private cookieService: SharedService, private router: Router, 
    private route : ActivatedRoute, private comuneService : ComuneService) {
      this.myForm = new FormGroup({ 
        txtNomeDelComune: new FormControl(),
        txtCuratore: new FormControl(),
        txtCap : new FormControl()
      });
  }
  

  ngAfterViewInit(): void {
    this.route.params.subscribe((params) => {
      let id = params['id'];
      this.comuneService.getCity(id).subscribe((city) => {
        this.map = L.map('map').setView([city.pos.lat, city.pos.lng], 12);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
        this.map.setMaxZoom(20).setMinZoom(12).setMaxBounds([
          [47.0, 6.0],
          [35.0, 19.0]
        ])
        this.map.on('click', (event: any) => {
          this.pos.lat = event.latlng.lat;
          this.pos.lng = event.latlng.lng;
          this.addEmptyMarker(event.latlng.lat, event.latlng.lng);
        });
        this.addEmptyMarker(city.pos.lat, city.pos.lng);
        this.pos.lat = city.pos.lat;
        this.pos.lng = city.pos.lng;
        this.name = city.name;
        this.cap = ""+city.cap;
        this.curator = city.curator;
      })
    })
    
  }

  async modifica() {
    if(this.myForm.valid){
      let city = {
        cityName : this.myForm.value.txtNomeDelComune,
        curator : this.myForm.value.txtCuratore,
	      cap : this.myForm.value.txtCap,
        pos : this.pos
      };
      let cityId = (await firstValueFrom(this.route.params))['id'];
      this.comuneService.updateCity(this.cookieService.get('jwt'), city, cityId).subscribe({
        next: (result) => {
          alert('Comune Aggiornato.');
          this.router.navigateByUrl('/');
        },
        error: (error) => 
        alert('Dati inseriti non validi.')});
    } else alert('Compila tutti i campi.');
  }

  private addEmptyMarker(lat : number, lng: number){
    if(this.marker)
      this.map?.removeLayer(this.marker);
    var myIcon = L.icon({
      iconUrl: this.empty,
      iconSize: [28, 40],
      popupAnchor: [0, -26]
  });
    if (this.map) {
      this.marker = L.marker([lat, lng], {icon:myIcon}).addTo(this.map);
    }
  }

}


