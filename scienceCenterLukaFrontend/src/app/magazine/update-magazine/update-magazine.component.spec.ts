import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateMagazineComponent } from './update-magazine.component';

describe('UpdateMagazineComponent', () => {
  let component: UpdateMagazineComponent;
  let fixture: ComponentFixture<UpdateMagazineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UpdateMagazineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateMagazineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
