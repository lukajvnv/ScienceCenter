import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewMagazineEditionsComponent } from './view-magazine-editions.component';

describe('ViewMagazineEditionsComponent', () => {
  let component: ViewMagazineEditionsComponent;
  let fixture: ComponentFixture<ViewMagazineEditionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewMagazineEditionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewMagazineEditionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
