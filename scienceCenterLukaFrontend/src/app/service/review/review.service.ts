import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { StorageService } from '../auth-storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private REVIEWAPI = "http://localhost:8085/review/";


  constructor(private http: HttpClient, private tokenStorageService: StorageService) { }

  private genHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', this.tokenStorageService.getToken());
  }

  startReview(magazineId: string): Observable<any>{
    return this.http.get(this.REVIEWAPI + 'start/'.concat(magazineId), {headers: this.genHeader()}); 
  }

  postReview(review, taskId: string) : Observable<any>{
    return this.http.post(this.REVIEWAPI + "reviewPost/".concat(taskId), review, {headers: this.genHeader()});
  }

  startReviewAdditional(magazineId: string): Observable<any>{
    return this.http.get(this.REVIEWAPI + 'startReviewingAdditional/'.concat(magazineId), {headers: this.genHeader()}); 
  }

  postReviewAdditional(review, taskId: string) : Observable<any>{
    return this.http.post(this.REVIEWAPI + "startReviewingAdditional/".concat(taskId), review, {headers: this.genHeader()});
  }

  startReviewEditor(magazineId: string): Observable<any>{
    return this.http.get(this.REVIEWAPI + 'editorReview/'.concat(magazineId), {headers: this.genHeader()}); 
  }

  postReviewEditor(review, taskId: string) : Observable<any>{
    return this.http.post(this.REVIEWAPI + "editorReview/".concat(taskId), review, {headers: this.genHeader()});
  }

  startAddingReviewers(taskId: string) : Observable<any> {
    return this.http.get(this.REVIEWAPI + 'addReviewer/'.concat(taskId), {headers: this.genHeader()});
  }

  postAddingReviewers(taskId: string, obj: any): Observable<any> {
    return this.http.post(this.REVIEWAPI + 'addReviewer/'.concat(taskId), obj, {headers: this.genHeader()});
  }

  filterReviewers(taskId: string, obj: any): Observable<any> {
    return this.http.post(this.REVIEWAPI + 'filterReviewer/'.concat(taskId), obj, {headers: this.genHeader()});
  }

  startAddingReviewerWhenError(taskId: string) : Observable<any> {
    return this.http.get(this.REVIEWAPI + 'addReviewerWhenError/'.concat(taskId), {headers: this.genHeader()});
  }

  postAddingReviewerWhenError(taskId: string, obj: any): Observable<any> {
    return this.http.post(this.REVIEWAPI + 'addReviewerWhenError/'.concat(taskId), obj, {headers: this.genHeader()});
  }

  postAddingReviewerAdditional(taskId: string, obj: any): Observable<any> {
    return this.http.post(this.REVIEWAPI + 'addAdditionalReviewer/'.concat(taskId), obj, {headers: this.genHeader()});
  }

  defineTimeForReview(taskId: string, time:string) : Observable<any> {
    return this.http.get(this.REVIEWAPI + 'defineTimeForReview/'.concat(taskId,'/',time), {headers: this.genHeader()});
  }

  defineTimeForReviewStart(taskId: string) : Observable<any> {
    return this.http.get(this.REVIEWAPI + 'defineTimeForReview/'.concat(taskId), {headers: this.genHeader()});
  }

  defineTimeForReviewPost(taskId: string, body) : Observable<any> {
    return this.http.post(this.REVIEWAPI + 'defineTimeForReview/'.concat(taskId), body, {headers: this.genHeader()});
  }

  
}
