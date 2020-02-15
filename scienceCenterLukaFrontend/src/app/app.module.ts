import { BrowserModule } from '@angular/platform-browser';
import { NgModule, InjectionToken } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './view-general/home/home.component';
import { RegisterComponent } from './view-general/register/register.component';
import { SignInComponent } from './view-general/sign-in/sign-in.component';
import { FailedComponent } from './view-general/failed/failed.component';
import { ErrorComponent } from './view-general/error/error.component';
import { RouterModule, ActivatedRouteSnapshot } from '@angular/router';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { HttpClientModule } from '@angular/common/http';
import {ReactiveFormsModule, FormsModule} from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NotFoundComponent } from './view-general/not-found/not-found.component';
import { NavbarComponent } from './view-general/navbar/navbar.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TestService } from './service/test.service';
import { UserService } from './service/user/user.service';
import { MagazineService } from './service/magazine/magazine.service';
import { StorageService } from './service/auth-storage/storage.service';
import { NewMagazineComponent } from './magazine/new-magazine/new-magazine.component';
import { TaskService } from './service/task/task.service';
import { ViewMagazinesComponent } from './magazine/view-magazines/view-magazines.component';
import { ViewTasksComponent } from './task/view-tasks/view-tasks.component';
import { UpdateMagazineComponent } from './magazine/update-magazine/update-magazine.component';
import { ViewMagazineComponent } from './magazine/view-magazine/view-magazine.component';
import { NewArticleComponent } from './article/new-article/new-article.component';
import { AnalizeArticleComponent } from './article/analize-article/analize-article.component';
import { UpdateArticleComponent } from './article/update-article/update-article.component';
import { AddReviewerComponent } from './article/add-reviewer/add-reviewer.component';
import { ReviewComponent } from './article/review/review.component';
import { AddAditionalReviewerComponent } from './article/add-aditional-reviewer/add-aditional-reviewer.component';
import { ReviewEditorComponent } from './article/review-editor/review-editor.component';
import { DefineTimeForReviewingComponent } from './article/define-time-for-reviewing/define-time-for-reviewing.component';
import { AddReviewerWhenErrorComponent } from './article/add-reviewer-when-error/add-reviewer-when-error.component';
import { ReviewAdditionalComponent } from './article/review-additional/review-additional.component';
import { UpdateArticleChangesComponent } from './article/update-article-changes/update-article-changes.component';
import { CheckNewMagazineDataComponent } from './admin/check-new-magazine-data/check-new-magazine-data.component';
import { ReviewerConfirmationComponent } from './admin/reviewer-confirmation/reviewer-confirmation.component';
import { NewEditorComponent } from './admin/new-editor/new-editor.component';
import { Notauthorized } from './path-guards/nonauthorized.guard';
import { AdminGuard } from './path-guards/admin.guard';
import { EditorGuard } from './path-guards/editor.guard';
import { Authorized } from './path-guards/authorized.guard';
import { NewArticleInitComponent } from './article/new-article-init/new-article-init.component';
import { AuthorPayComponent } from './shopping/author-pay/author-pay.component';
import { ViewMagazineEditionsComponent } from './magazine/view-magazine-editions/view-magazine-editions.component';
import { ViewArticleComponent } from './article/view-article/view-article.component';
import { CartComponent } from './shopping/cart/cart.component';
import { UserTxComponent } from './shopping/user-tx/user-tx.component';
import { SuccessComponent } from './shopping/success/success.component';
import { NewEditionComponent } from './magazine/new-edition/new-edition.component';
import { ProfileComponent } from './view-general/profile/profile.component';
import { AddToKpComponent } from './magazine/add-to-kp/add-to-kp.component';

const externalUrlProvider = new InjectionToken('externalUrlRedirectResolver');


const routes = [
    {
      path: '', redirectTo: '/home', pathMatch: 'full'
    },
    {
      path: 'home', component: HomeComponent,
    },
    {
      path: 'register', component: RegisterComponent, canActivate: [Notauthorized]
    },
    {
      path: 'sign-in', component: SignInComponent, canActivate: [Notauthorized]
    },
    {
      path: 'new-editor', component: NewEditorComponent, canActivate: [AdminGuard]
    },
    {
      path: 'new-magazine', component: NewMagazineComponent,
    },
    {
      path: 'view-magazines', component: ViewMagazinesComponent,
    },
    {
      path: 'view-magazine/:magazineId', component: ViewMagazineComponent,
    },
    {
      path: 'update-magazine/:taskId', component: UpdateMagazineComponent,
    },
    {
      path: 'add-magazine-to-kp/:taskId', component: AddToKpComponent,
    },
    {
      path: 'new-article/:taskId', component: NewArticleComponent,
    },
    {
      path: 'new-article-init/:magazineId', component: NewArticleInitComponent,
    },
    {
      path: 'update-article/:taskId', component: UpdateArticleComponent,
    },
    {
      path: 'update-article-changes/:taskId', component: UpdateArticleChangesComponent,
    },
    {
      path: 'analize-article/:taskId', component: AnalizeArticleComponent,
    },
    {
      path: 'view-article/:articleId', component: ViewArticleComponent,
    },
    {
      path: 'add-review/:taskId', component: AddReviewerComponent,
    },
    {
      path: 'add-review-when-error/:taskId', component: AddReviewerWhenErrorComponent,
    },
    {
      path: 'define_time_to_respond/:taskId', component: DefineTimeForReviewingComponent
    },
    {
      path: 'review-article/:taskId', component: ReviewComponent,
    },
    {
      path: 'review-additional/:taskId', component: ReviewAdditionalComponent,
    },
    {
      path: 'editor-review/:taskId', component: ReviewEditorComponent,
    },
    {
      path: 'tasks', component: ViewTasksComponent, canActivate: [Authorized]
    },
    {
      path: 'reviewer-confirmation/:taskId', component: ReviewerConfirmationComponent,
    },
    {
      path: 'check-magazine-data/:taskId', component: CheckNewMagazineDataComponent,
    },
    {
      path: 'author-pay/:taskId', component: AuthorPayComponent,
    },
    {
      path: 'view-edition/:editionId', component: ViewMagazineEditionsComponent,
    },
    {
      path: 'new-edition/:magazineId', component: NewEditionComponent,
    },
    {
      path: 'cart', component: CartComponent
  
    },
    {
      path: 'profile', component: ProfileComponent
    },
    {
      path: 'user-tx', component: UserTxComponent
  
    },
    {
      path: 'success', component: SuccessComponent
    },
    {
      path: 'externalRedirect',
      resolve: {
          url: externalUrlProvider,
      },
      // We need a component here because we cannot define the route otherwise
      component: AuthorPayComponent,
    },
    {
      path: '**', component: NotFoundComponent
    }
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    // FontAwesomeModule,
    RegisterComponent,
    SignInComponent,
    FailedComponent,
    ErrorComponent,
    NotFoundComponent,
    NavbarComponent,
    NewMagazineComponent,
    ViewMagazinesComponent,
    ViewTasksComponent,
    UpdateMagazineComponent,
    ViewMagazineComponent,
    NewArticleComponent,
    UpdateArticleComponent,
    AnalizeArticleComponent,
    AddReviewerComponent,
    ReviewComponent,
    AddAditionalReviewerComponent,
    ReviewEditorComponent,
    DefineTimeForReviewingComponent,
    AddReviewerWhenErrorComponent,
    ReviewAdditionalComponent,
    UpdateArticleChangesComponent,
    CheckNewMagazineDataComponent,
    ReviewerConfirmationComponent,
    NewEditorComponent,
    NewArticleInitComponent,
    AuthorPayComponent,
    ViewMagazineEditionsComponent,
    ViewArticleComponent,
    CartComponent,
    UserTxComponent,
    SuccessComponent,
    NewEditionComponent,
    ProfileComponent,
    AddToKpComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      timeOut: 4000,
      closeButton: true,
      positionClass: 'toast-top-right',
    }),
    RouterModule.forRoot(routes, {enableTracing: true}) // <-- debugging purposes only
  ],
  providers: [ToastrService, TestService, UserService, MagazineService, StorageService, TaskService,
    Notauthorized, AdminGuard, EditorGuard, Authorized,
    {
      provide: externalUrlProvider,
      useValue: (route: ActivatedRouteSnapshot) => {
          const externalUrl = route.paramMap.get('externalUrl');
          window.open(externalUrl, '_self');
      }
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
