import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewAdditionalComponent } from './review-additional.component';

describe('ReviewAdditionalComponent', () => {
  let component: ReviewAdditionalComponent;
  let fixture: ComponentFixture<ReviewAdditionalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewAdditionalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewAdditionalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
