import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewMagazinesComponent } from './view-magazines.component';

describe('ViewMagazinesComponent', () => {
  let component: ViewMagazinesComponent;
  let fixture: ComponentFixture<ViewMagazinesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewMagazinesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewMagazinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
