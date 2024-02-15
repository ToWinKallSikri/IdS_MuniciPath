import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment.development';
import { SharedService } from './shared.service';
import { WebResponse } from '../models/Response';

@Injectable({
  providedIn: 'root'
})
export class CheckService {

  constructor(private HttpClient: HttpClient, private sharedService : SharedService) {}

  public checkManager() : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/isManager', {headers : header});
  }

  public isAuthor(contentId : string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    let param = new HttpParams().append('contentId', contentId);
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/isAuthor', {headers : header, params : param});
  }

  public havePowerWithIt(contentId : string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    let param = new HttpParams().append('contentId', contentId);
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/havePowerWithIt', {headers : header, params : param});
  }

  public usernameExist() : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/usernameExists', {headers : header});
  }

  public isNotLimited(cityId : string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    let param = new HttpParams().append('cityId', cityId);
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/isNotLimited', {headers : header, params : param});
  }

  public alreadyFollowingContent(contentId : string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    let param = new HttpParams().append('contentId', contentId);
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/follow/content', {headers : header, params : param});
  }

  public alreadyFollowingCity(cityId : string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    let param = new HttpParams().append('cityId', cityId);
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/follow/city', {headers : header, params : param});
  }

  public alreadyFollowingContributor(contributor : string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    let param = new HttpParams().append('contributor', contributor);
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/follow/contributor', {headers : header, params : param});
  }

  public getRole(cityId : string) : Observable<WebResponse> {
    let header = new HttpHeaders().append('auth', this.sharedService.get('jwt'));
    let param = new HttpParams().append('cityId', cityId);
    return this.HttpClient.get<WebResponse>(environment.baseUrl+'/api/v1/check/role', {headers : header, params : param});
  }


}
/**
 * 
	
	
	@GetMapping(value="/api/v1/city/{cityId}/check/follow/content")
	
	@GetMapping(value="/api/v1/city/{cityId}/check/follow/city")
	
	@GetMapping(value="/api/v1/city/{cityId}/check/follow/contributor")
 */
