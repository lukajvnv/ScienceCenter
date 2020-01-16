import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewEditorComponent } from './review-editor.component';

describe('ReviewEditorComponent', () => {
  let component: ReviewEditorComponent;
  let fixture: ComponentFixture<ReviewEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReviewEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
