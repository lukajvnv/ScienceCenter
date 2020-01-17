import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SignInDto } from 'src/app/model/form-sign-in-submission';
import { JwtResponse } from 'src/app/model/jwt-response';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private USERAPI = "http://localhost:8085/user/";
  private USERAuthAPI = "http://localhost:8085/auth/";



  constructor(private http: HttpClient) { }

  startRegister(): Observable<any>{
    return this.http.get(this.USERAPI + 'register'); 
  }

  registerUser(user, taskId) : Observable<any>{
    return this.http.post(this.USERAPI + "register/".concat(taskId), user);
  }

  startReviewerConfirmation(taskId): Observable<any>{
    return this.http.get(this.USERAPI + 'reviewerConfirmationStart/'.concat(taskId)); 
  }

  postReviewerConfirmation(activateAsReviewer, taskId) : Observable<any>{
    return this.http.get(this.USERAPI + "reviewerConfirmationEnd/".concat(taskId, '?confirm=', activateAsReviewer));
  }

  signIn(signInDto: SignInDto): Observable<JwtResponse>{
    return this.http.post<JwtResponse>(this.USERAuthAPI + 'signin', signInDto); 
  }

  signOut(user, taskId) : Observable<any>{
    return this.http.post(this.USERAPI + "register/".concat(taskId), user);
  }
  
}
