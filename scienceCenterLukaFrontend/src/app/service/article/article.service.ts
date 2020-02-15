import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from '../auth-storage/storage.service';
import { GenericResponse } from 'src/app/model/GenericResponse';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  private ARTICLEAPI = "https://localhost:8085/article/";


  constructor(private http: HttpClient, private tokenStorageService: StorageService) { }
  
  private genHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', this.tokenStorageService.getToken());
  }

  startNewArticle(magazineId: string): Observable<any>{
    return this.http.get(this.ARTICLEAPI + 'start/'.concat(magazineId), {headers: this.genHeader()}); 
  }

  startNewArticleInitialiaztion(magazineId: string): Observable<any>{
    return this.http.get(this.ARTICLEAPI + 'startArticleInit/'.concat(magazineId), {headers: this.genHeader()}); 
  }

  postNewArticle(article, taskId) : Observable<any>{
    return this.http.post(this.ARTICLEAPI + "postArticle/".concat(taskId), article, {headers: this.genHeader()});
  }

  analizeArticle(taskId: string){
    return this.http.get(this.ARTICLEAPI + 'analizeBasic/'.concat(taskId), {headers: this.genHeader()});
  }

  analizeBasicResult(taskId: string, topicOk: boolean): Observable<any> {
    let topic: string = 'true';
    if (!topicOk) {
      topic = 'false';
    }
    return this.http.get(this.ARTICLEAPI + 'analizeBasicResult/'.concat(taskId, '?topicOk=', topic), {headers: this.genHeader()});
  }

  analizeBasicResultPost(taskId: string, body): Observable<any> {
    return this.http.post(this.ARTICLEAPI + 'analizeBasicResult/'.concat(taskId), body, {headers: this.genHeader()});
  }

  analizeTextResult(taskId: string, comment: string, textOk: boolean): Observable<any> {
    let text: string = 'true';
    if (!textOk) {
      text = 'false';
    }
    return this.http.post(this.ARTICLEAPI + 'analizeTextResult/'.concat(taskId, '?textOk=', text), comment, {headers: this.genHeader()});
  }

  analizeTextResultPost(taskId: string, body): Observable<any> {
    return this.http.post(this.ARTICLEAPI + 'analizeTextResult/'.concat(taskId), body, {headers: this.genHeader()});
  }

  startUpdateArticle(taskId: string): Observable<any>{
    return this.http.get(this.ARTICLEAPI + 'updateArticleStart/'.concat(taskId), {headers: this.genHeader()}); 
  }

  updateArticle(article, taskId) : Observable<any>{
    return this.http.put(this.ARTICLEAPI + "updateArticle/".concat(taskId), article, {headers: this.genHeader()});
  }

  startUpdateChangesArticle(taskId: string): Observable<any>{
    return this.http.get(this.ARTICLEAPI + 'updateArticleChangesStart/'.concat(taskId), {headers: this.genHeader()}); 
  }

  updateChangesArticle(article, taskId) : Observable<any>{
    return this.http.put(this.ARTICLEAPI + "updateArticleChangesPut/".concat(taskId), article, {headers: this.genHeader()});
  }

  uploadFile(taskId, file): Observable<any> {
    return this.http.post(this.ARTICLEAPI + "upload/".concat(taskId), file);
  }

  downloadFile(articleId): Observable<Blob>{
    const headers = new HttpHeaders({ responseType : 'blob'});

		return this.http.get<Blob>(this.ARTICLEAPI + 'download/'.concat(articleId), {headers: headers, responseType: 'blob' as 'json'});
  }

  downloadFileByUser(articleId): Observable<Blob>{
    // const headers = new HttpHeaders({ responseType : 'blob'});

    let header = this.genHeader().set('responseType', 'blog');

		return this.http.get<Blob>(this.ARTICLEAPI + 'downloadFile/'.concat(articleId), {headers: header, responseType: 'blob' as 'json'});
  }

  viewArticle(articleId: string): Observable<any>{
    return this.http.get(this.ARTICLEAPI + 'viewArticle/'.concat(articleId), {headers: this.genHeader()}); 
  }

  viewArticles(articleId: string): Observable<any>{
    return this.http.get(this.ARTICLEAPI + 'getArticles/'.concat(articleId), {headers: this.genHeader()}); 
  }

  // downloadFiles(): Observable<HttpResponse<Blob>>{
  //   const headers = new HttpHeaders({ responseType : 'blob'});

	// 	return this.http.get<HttpResponse<Blob>>(this.ARTICLEAPI + 'download', {headers: headers, responseType: 'blob' as 'json'});
  // }

  // downloadFiles() : Observable<HttpResponse<Object>>{
  //   //kako videti http header response
  //   return this.http.get<HttpResponse<Blob>>(this.ARTICLEAPI + 'download', {observe: 'response'}).pipe(
  //     tap(resp => console.log('heaeder', resp.headers.get('ReturnStatus')))
  //   );
    
  // }

  executeAuthorPayment(taskId): Observable<any> {
    return this.http.get(this.ARTICLEAPI + "executeAuthorPayment/".concat(taskId), {headers: this.genHeader()});
  }

 
}
