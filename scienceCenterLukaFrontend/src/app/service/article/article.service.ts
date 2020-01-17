import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StorageService } from '../auth-storage/storage.service';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  private ARTICLEAPI = "http://localhost:8085/article/";


  constructor(private http: HttpClient, private tokenStorageService: StorageService) { }
  
  private genHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', this.tokenStorageService.getToken());
  }

  startNewArticle(magazineId: string): Observable<any>{
    //return this.http.get(this.ARTICLEAPI + 'start/'.concat(magazineId)); 
    return this.http.get(this.ARTICLEAPI + 'start/'.concat(magazineId), {headers: this.genHeader()}); 
  }

  postNewArticle(article, taskId) : Observable<any>{
    return this.http.post(this.ARTICLEAPI + "postArticle/".concat(taskId), article);
  }

  analizeArticle(taskId: string){
    return this.http.get(this.ARTICLEAPI + 'analizeBasic/'.concat(taskId));
  }

  analizeBasicResult(taskId: string, topicOk: boolean): Observable<any> {
    let topic: string = 'true';
    if (!topicOk) {
      topic = 'false';
    }
    return this.http.get(this.ARTICLEAPI + 'analizeBasicResult/'.concat(taskId, '?topicOk=', topic));
  }

  analizeTextResult(taskId: string, comment: string, textOk: boolean): Observable<any> {
    let text: string = 'true';
    if (!textOk) {
      text = 'false';
    }
    return this.http.post(this.ARTICLEAPI + 'analizeTextResult/'.concat(taskId, '?textOk=', text), comment);
  }

  startUpdateArticle(taskId: string): Observable<any>{
    return this.http.get(this.ARTICLEAPI + 'updateArticleStart/'.concat(taskId)); 
  }

  updateArticle(article, taskId) : Observable<any>{
    return this.http.put(this.ARTICLEAPI + "updateArticle/".concat(taskId), article);
  }

  startUpdateChangesArticle(taskId: string): Observable<any>{
    return this.http.get(this.ARTICLEAPI + 'updateArticleChangesStart/'.concat(taskId)); 
  }

  updateChangesArticle(article, taskId) : Observable<any>{
    return this.http.put(this.ARTICLEAPI + "updateArticleChangesPut/".concat(taskId), article);
  }

 
}
