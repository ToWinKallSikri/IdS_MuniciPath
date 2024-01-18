import { TestBed } from '@angular/core/testing';

import { ComuneService } from './comune.service';

describe('ComuneService', () => {
  let service: ComuneService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComuneService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
