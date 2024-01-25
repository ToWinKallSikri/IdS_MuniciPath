import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Point } from './Point';
import { Post } from './Post';
import { environment } from '../environments/environment.development';
import { catchError } from 'rxjs/operators';
import { SharedService } from './shared.service';


@Injectable({
  providedIn: 'root'
})
export class PointService {

  constructor(private HttpClient: HttpClient, private cookieService : SharedService) {}

  public getPoints(cityId : string) : Observable<Point[]> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    return this.HttpClient.get<Point[]>(environment.baseUrl+'/api/v1/city/'+cityId+'/points', { headers: header });
  }

  public getPoint(cityId : string, pointId : string) : Observable<Point> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    let param = new HttpParams().append('pointId', pointId);
    return this.HttpClient.get<Point>(environment.baseUrl+'/api/v1/city/'+cityId+'/point', { headers: header, params: param });
  }

  public getPost(cityId : string, postId : string) : Observable<Post> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    let param = new HttpParams().append('postId', postId);
    return this.HttpClient.get<Post>(environment.baseUrl+'/api/v1/city/'+cityId+'/post', { headers: header, params: param });
  }
  
}
