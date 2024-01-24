import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemovecityComponent } from './removecity.component';

describe('RemovecityComponent', () => {
  let component: RemovecityComponent;
  let fixture: ComponentFixture<RemovecityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RemovecityComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RemovecityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
