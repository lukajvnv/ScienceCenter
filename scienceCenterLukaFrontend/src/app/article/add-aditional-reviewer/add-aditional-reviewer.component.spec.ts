import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddAditionalReviewerComponent } from './add-aditional-reviewer.component';

describe('AddAditionalReviewerComponent', () => {
  let component: AddAditionalReviewerComponent;
  let fixture: ComponentFixture<AddAditionalReviewerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddAditionalReviewerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddAditionalReviewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
