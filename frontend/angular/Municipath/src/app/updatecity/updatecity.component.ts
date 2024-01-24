import { Component } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms'; 
import { ActivatedRoute, Router } from '@angular/router';
import { MakecityService } from '../makecity.service';
import { Position } from '../Position';
import { SharedService } from '../shared.service';
import { ComuneService } from '../comune.service';
import { City } from '../City';
import * as L from 'leaflet';

@Component({
  selector: 'app-updatecity',
  templateUrl: './updatecity.component.html',
  styleUrl: './updatecity.component.scss'
})
export class UpdatecityComponent {
  myForm: FormGroup;
  pos!: Position;
  public comuni: City[] = [];
  private map: any;
  private marker: any;
  city = 'https://i.postimg.cc/GpP8xRfs/Authority.png';
  empty = 'https://i.postimg.cc/ZngYcZfq/immagine-2024-01-24-113850127-png.png';
  
  constructor( private cookieService: SharedService, private router: Router, 
    private mkService : MakecityService, private route : ActivatedRoute, private comuneService : ComuneService) {
    this.myForm = new FormGroup({ 
      txtNomeDelComune: new FormControl(),
      txtCuratore: new FormControl(),
      txtCap : new FormControl(),
    });
  }

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
        this.map.on('click', (event: any) => {
          this.addEmptyMarker(event.latlng.lat, event.latlng.lng);
          console.log(event.latlng);
        });
      });
    });
  }

  modifica() {
    if(this.myForm.valid){
      let city = {
        cityName : this.myForm.value.txtNomeDelComune,
        curator : this.myForm.value.txtCuratore,
	      cap : this.myForm.value.txtCap,
        pos : this.pos
      };
      this.mkService.createCity(this.cookieService.get('jwt'), city).subscribe({
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
      this.map.removeLayer(this.marker);
    var myIcon = L.icon({
      iconUrl: this.empty,
      iconSize: [28, 40],
      popupAnchor: [0, -26]
  });
    this.marker = L.marker([lat, lng], {icon:myIcon}).addTo(this.map)
    .bindPopup(`<button onClick="location.href='/makecity/${lat}/${lng}'">Crea Comune</button>`,  {closeButton: false})
    .on('click', (event: any) => {
      this.marker.openPopup();
    });
  }

  

  private addMarker(city: City): void {
    var myIcon = L.icon({
      iconUrl: this.city,
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
  }

}


