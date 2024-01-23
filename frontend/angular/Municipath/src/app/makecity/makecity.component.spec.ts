import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakecityComponent } from './makecity.component';

describe('MakecityComponent', () => {
  let component: MakecityComponent;
  let fixture: ComponentFixture<MakecityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MakecityComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MakecityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
