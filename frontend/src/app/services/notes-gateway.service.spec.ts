import { TestBed } from '@angular/core/testing';

import { NotesGatewayService } from './notes-gateway.service';

describe('NotesGatewayService', () => {
  let service: NotesGatewayService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NotesGatewayService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
