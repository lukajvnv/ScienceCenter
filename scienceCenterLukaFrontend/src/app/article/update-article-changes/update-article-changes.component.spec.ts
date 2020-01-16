import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateArticleChangesComponent } from './update-article-changes.component';

describe('UpdateArticleChangesComponent', () => {
  let component: UpdateArticleChangesComponent;
  let fixture: ComponentFixture<UpdateArticleChangesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UpdateArticleChangesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateArticleChangesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
