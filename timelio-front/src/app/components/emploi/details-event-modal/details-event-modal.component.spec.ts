import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsEventModalComponent } from './details-event-modal.component';

describe('DetailsEventModalComponent', () => {
  let component: DetailsEventModalComponent;
  let fixture: ComponentFixture<DetailsEventModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailsEventModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailsEventModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
