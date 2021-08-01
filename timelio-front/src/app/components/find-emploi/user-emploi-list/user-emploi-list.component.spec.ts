import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEmploiListComponent } from './user-emploi-list.component';

describe('UserEmploiListComponent', () => {
  let component: UserEmploiListComponent;
  let fixture: ComponentFixture<UserEmploiListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserEmploiListComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserEmploiListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
