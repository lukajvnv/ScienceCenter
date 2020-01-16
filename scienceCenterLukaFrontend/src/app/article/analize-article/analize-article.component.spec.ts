import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalizeArticleComponent } from './analize-article.component';

describe('AnalizeArticleComponent', () => {
  let component: AnalizeArticleComponent;
  let fixture: ComponentFixture<AnalizeArticleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AnalizeArticleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AnalizeArticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
