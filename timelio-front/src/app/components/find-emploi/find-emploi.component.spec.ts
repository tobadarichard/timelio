import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FindEmploiComponent } from './find-emploi.component';

describe('FindEmploiComponent', () => {
  let component: FindEmploiComponent;
  let fixture: ComponentFixture<FindEmploiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FindEmploiComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FindEmploiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
