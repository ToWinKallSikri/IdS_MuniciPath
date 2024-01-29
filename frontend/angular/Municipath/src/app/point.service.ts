import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Point } from './Point';
import { Post } from './Post';
import { environment } from '../environments/environment.development';
import { catchError } from 'rxjs/operators';
import { SharedService } from './shared.service';
import { WebResponse } from './Response';
import { Position } from './Position';


@Injectable({
  providedIn: 'root'
})
export class PointService {

  constructor(private HttpClient: HttpClient, private cookieService : SharedService) {}

  public getPoints(cityId : string) : Observable<Point[]> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    return this.HttpClient.get<Point[]>(environment.baseUrl+'/api/v1/city/'+cityId+'/points', { headers: header })
    .pipe(catchError(error => throwError(() => error)));
  }

  public getPoint(cityId : string, pointId : string) : Observable<Point> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    let param = new HttpParams().append('pointId', pointId);
    return this.HttpClient.get<Point>(environment.baseUrl+'/api/v1/city/'+cityId+'/point', { headers: header, params: param })
    .pipe(catchError(error => throwError(() => error)));
  }

  public getPost(cityId : string, postId : string) : Observable<Post> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    let param = new HttpParams().append('postId', postId);
    return this.HttpClient.get<Post>(environment.baseUrl+'/api/v1/city/'+cityId+'/post', { headers: header, params: param })
    .pipe(catchError(error => throwError(() => error)));
  }

  public createPost(cityId : string, pos : Position, postData : any) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    let param = new HttpParams().append('lat', pos.lat).append('lng', pos.lng);
    return this.HttpClient.post<WebResponse>(environment.baseUrl+'/api/v1/city/'+cityId+'/posts', postData, { headers: header, params: param })
    .pipe(catchError(error => throwError(() => error)));
  }

  public deletePost (cityId : string, postId : string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    let param = new HttpParams().append('postId', postId);
    return this.HttpClient.delete<WebResponse>(environment.baseUrl+'/api/v1/city/'+cityId+'/posts', { headers: header, params: param })
    .pipe(catchError(error => throwError(() => error)));
  }

  public updatePost(cityId : string, postId : string, postData : any) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.cookieService.get('jwt'));
    let param = new HttpParams().append('postId', postId);
    return this.HttpClient.put<WebResponse>(environment.baseUrl+'/api/v1/city/'+cityId+'/posts', postData, { headers: header, params: param })
    .pipe(catchError(error => throwError(() => error)));
  }
}
