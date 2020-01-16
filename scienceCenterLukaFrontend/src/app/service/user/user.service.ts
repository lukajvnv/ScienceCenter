import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private USERAPI = "http://localhost:8085/user/";


  constructor(private http: HttpClient) { }

  startRegister(): Observable<any>{
    return this.http.get(this.USERAPI + 'register'); 
  }

  registerUser(user, taskId) : Observable<any>{
    return this.http.post(this.USERAPI + "/register/".concat(taskId), user);
  }

  startReviewerConfirmation(taskId): Observable<any>{
    return this.http.get(this.USERAPI + 'reviewerConfirmationStart/'.concat(taskId)); 
  }

  postReviewerConfirmation(activateAsReviewer, taskId) : Observable<any>{
    return this.http.get(this.USERAPI + "reviewerConfirmationEnd/".concat(taskId, '?confirm=', activateAsReviewer));
  }
  
}
