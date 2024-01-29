import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ComuneComponent } from './comune/comune.component';
import { HomeComponent } from './home/home.component';
import { MapComponent } from './map/map.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatCardModule} from '@angular/material/card';
import { HttpClientModule } from  '@angular/common/http';
import {MatIconModule} from '@angular/material/icon';
import { CookieService } from 'ngx-cookie-service';
import { LoginComponent } from './login/login.component';
import { MakecityComponent } from './makecity/makecity.component';
import { MakepostComponent } from './makepost/makepost.component';
import { MakegroupComponent } from './makegroup/makegroup.component';
import { StaffComponent } from './staff/staff.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import {ReactiveFormsModule} from "@angular/forms"; 
import {MatMenuModule} from '@angular/material/menu';
import {MatExpansionModule} from '@angular/material/expansion';
import { ProfileComponent } from './profile/profile.component';
import { UpdatecityComponent } from './updatecity/updatecity.component';
import { RemovecityComponent } from './removecity/removecity.component';
import { UservalidationComponent } from './uservalidation/uservalidation.component';
import {MatDividerModule} from '@angular/material/divider';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {MatRadioModule} from '@angular/material/radio';
import {MatCheckboxModule} from '@angular/material/checkbox';
import { Validators } from '@angular/forms';
import { DeletepostComponent } from './deletepost/deletepost.component';

@NgModule({
  declarations: [
    AppComponent,
    ComuneComponent,
    HomeComponent,
    MapComponent,
    NotFoundComponent,
    LoginComponent,
    MakecityComponent,
    MakepostComponent,
    MakegroupComponent,
    StaffComponent,
    ProfileComponent,
    UpdatecityComponent,
    RemovecityComponent,
    UservalidationComponent,
    DeletepostComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatIconModule,
    MatButtonModule,
    MatToolbarModule,
    MatCardModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatInputModule,
    MatMenuModule,
    MatExpansionModule,
    MatDividerModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatRadioModule,
    MatCheckboxModule
  ],
  providers: [CookieService,
    MatDatepickerModule,
    MatNativeDateModule],
  bootstrap: [AppComponent]
})
export class AppModule { }
