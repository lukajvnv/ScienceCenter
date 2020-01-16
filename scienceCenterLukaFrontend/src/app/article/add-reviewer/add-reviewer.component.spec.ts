import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddReviewerComponent } from './add-reviewer.component';

describe('AddReviewerComponent', () => {
  let component: AddReviewerComponent;
  let fixture: ComponentFixture<AddReviewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddReviewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddReviewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
