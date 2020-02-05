import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewArticleInitComponent } from './new-article-init.component';

describe('NewArticleInitComponent', () => {
  let component: NewArticleInitComponent;
  let fixture: ComponentFixture<NewArticleInitComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewArticleInitComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewArticleInitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
