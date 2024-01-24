import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { NotFoundComponent } from './not-found/not-found.component';
import { HomeComponent } from './home/home.component';
import { MapComponent } from './map/map.component';
import { ComuneComponent } from './comune/comune.component';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { MakecityComponent } from './makecity/makecity.component';
import { UpdatecityComponent } from './updatecity/updatecity.component';
import { RemovecityComponent } from './removecity/removecity.component';

const routes: Routes = [{
  path: "",
  component: HomeComponent
},
{
  path: "map",
  component: MapComponent
},
{
  path: "log/:id",
  component: LoginComponent
},
{
  path: "accountValidation",
  component : UserActivation
},
{
  path: "profile",
  component: ProfileComponent
},
{
  path: "makecity/:lat/:lng",
  component: MakecityComponent
},
{
  path: "updatecity/:id",
  component: UpdatecityComponent
},
{
  path: "deletecity/:id",
  component: RemovecityComponent
},
{
  path: "city/:id",
  component: ComuneComponent
},
{
  path: "**",
  component: NotFoundComponent
}];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
