import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateEmploiModalComponent } from './update-emploi-modal.component';

describe('UpdateEmploiModalComponent', () => {
  let component: UpdateEmploiModalComponent;
  let fixture: ComponentFixture<UpdateEmploiModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateEmploiModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateEmploiModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
