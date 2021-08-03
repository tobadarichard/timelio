import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteEmploiModalComponent } from './delete-emploi-modal.component';

describe('DeleteEmploiModalComponent', () => {
  let component: DeleteEmploiModalComponent;
  let fixture: ComponentFixture<DeleteEmploiModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteEmploiModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteEmploiModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
