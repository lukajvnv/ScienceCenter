import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private REVIEWAPI = "http://localhost:8085/review/";


  constructor(private http: HttpClient) { }

  startReview(magazineId: string): Observable<any>{
    return this.http.get(this.REVIEWAPI + 'start/'.concat(magazineId)); 
  }

  postReview(review, taskId: string) : Observable<any>{
    return this.http.post(this.REVIEWAPI + "reviewPost/".concat(taskId), review);
  }

  startReviewAdditional(magazineId: string): Observable<any>{
    return this.http.get(this.REVIEWAPI + 'startReviewingAdditional/'.concat(magazineId)); 
  }

  postReviewAdditional(review, taskId: string) : Observable<any>{
    return this.http.post(this.REVIEWAPI + "startReviewingAdditional/".concat(taskId), review);
  }

  startReviewEditor(magazineId: string): Observable<any>{
    return this.http.get(this.REVIEWAPI + 'editorReview/'.concat(magazineId)); 
  }

  postReviewEditor(review, taskId: string) : Observable<any>{
    return this.http.post(this.REVIEWAPI + "editorReview/".concat(taskId), review);
  }

  startAddingReviewers(taskId: string) : Observable<any> {
    return this.http.get(this.REVIEWAPI + 'addReviewer/'.concat(taskId));
  }

  postAddingReviewers(taskId: string, obj: any): Observable<any> {
    return this.http.post(this.REVIEWAPI + 'addReviewer/'.concat(taskId), obj);
  }

  filterReviewers(taskId: string, obj: any): Observable<any> {
    return this.http.post(this.REVIEWAPI + 'filterReviewer/'.concat(taskId), obj);
  }

  startAddingReviewerWhenError(taskId: string) : Observable<any> {
    return this.http.get(this.REVIEWAPI + 'addReviewerWhenError/'.concat(taskId));
  }

  postAddingReviewerWhenError(taskId: string, obj: any): Observable<any> {
    return this.http.post(this.REVIEWAPI + 'addReviewerWhenError/'.concat(taskId), obj);
  }

  postAddingReviewerAdditional(taskId: string, obj: any): Observable<any> {
    return this.http.post(this.REVIEWAPI + 'addAdditionalReviewer/'.concat(taskId), obj);
  }

  defineTimeForReview(taskId: string, time:string) : Observable<any> {
    return this.http.get(this.REVIEWAPI + 'defineTimeForReview/'.concat(taskId,'/',time));
  }

  defineTimeForReviewStart(taskId: string) : Observable<any> {
    return this.http.get(this.REVIEWAPI + 'defineTimeForReview/'.concat(taskId));
  }

  defineTimeForReviewPost(taskId: string, body) : Observable<any> {
    return this.http.post(this.REVIEWAPI + 'defineTimeForReview/'.concat(taskId), body);
  }

  
}
