import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewerConfirmationComponent } from './reviewer-confirmation.component';

describe('ReviewerConfirmationComponent', () => {
  let component: ReviewerConfirmationComponent;
  let fixture: ComponentFixture<ReviewerConfirmationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewerConfirmationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewerConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
