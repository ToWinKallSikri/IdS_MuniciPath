import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class MediaService {

  private pathUrl = '/api/v1/path';

  constructor(private http: HttpClient) { }

  
  getPath(files: File[]): Observable<string[]> {
    const formData: FormData = new FormData();
    Array.from(files).forEach((file) => {
      formData.append('files', file, file.name);
    });
    return this.http.post<string[]>(this.pathUrl, formData);
  }

}
