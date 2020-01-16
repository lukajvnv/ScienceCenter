import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DefineTimeForReviewingComponent } from './define-time-for-reviewing.component';

describe('DefineTimeForReviewingComponent', () => {
  let component: DefineTimeForReviewingComponent;
  let fixture: ComponentFixture<DefineTimeForReviewingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DefineTimeForReviewingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DefineTimeForReviewingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
