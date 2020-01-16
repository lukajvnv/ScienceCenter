import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReviewerWhenErrorComponent } from './add-reviewer-when-error.component';

describe('AddReviewerWhenErrorComponent', () => {
  let component: AddReviewerWhenErrorComponent;
  let fixture: ComponentFixture<AddReviewerWhenErrorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddReviewerWhenErrorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddReviewerWhenErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
