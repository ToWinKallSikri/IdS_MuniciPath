import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IsStaffService {
  private eventSubject = new Subject<any>();

  public eventState = this.eventSubject.asObservable();

  publish(data: any) {
    this.eventSubject.next(data);
  }
}
