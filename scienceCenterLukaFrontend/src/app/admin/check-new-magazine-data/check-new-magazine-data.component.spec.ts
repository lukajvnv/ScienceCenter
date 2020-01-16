import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckNewMagazineDataComponent } from './check-new-magazine-data.component';

describe('CheckNewMagazineDataComponent', () => {
  let component: CheckNewMagazineDataComponent;
  let fixture: ComponentFixture<CheckNewMagazineDataComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CheckNewMagazineDataComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CheckNewMagazineDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
