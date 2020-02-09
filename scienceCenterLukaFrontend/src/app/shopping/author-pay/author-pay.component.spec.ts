import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorPayComponent } from './author-pay.component';

describe('AuthorPayComponent', () => {
  let component: AuthorPayComponent;
  let fixture: ComponentFixture<AuthorPayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorPayComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorPayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
