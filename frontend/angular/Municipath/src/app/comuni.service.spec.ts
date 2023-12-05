import { TestBed } from '@angular/core/testing';

import { ComuniService } from './comuni.service';

describe('ComuniService', () => {
  let service: ComuniService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComuniService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
