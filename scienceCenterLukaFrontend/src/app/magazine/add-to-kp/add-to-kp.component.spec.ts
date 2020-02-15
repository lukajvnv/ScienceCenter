import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddToKpComponent } from './add-to-kp.component';

describe('AddToKpComponent', () => {
  let component: AddToKpComponent;
  let fixture: ComponentFixture<AddToKpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddToKpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddToKpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
