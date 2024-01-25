import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChangeAccountService {

  private eventSubject = new Subject<any>();

  // Observable string streams
  public eventState = this.eventSubject.asObservable();

  // Service message commands
  publish(data: any) {
    this.eventSubject.next(data);
  }
}
