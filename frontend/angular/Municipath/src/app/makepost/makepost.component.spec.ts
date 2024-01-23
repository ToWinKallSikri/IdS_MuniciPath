import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakepostComponent } from './makepost.component';

describe('MakepostComponent', () => {
  let component: MakepostComponent;
  let fixture: ComponentFixture<MakepostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MakepostComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MakepostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
