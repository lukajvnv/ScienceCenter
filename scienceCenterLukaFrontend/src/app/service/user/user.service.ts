import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SignInDto } from 'src/app/model/form-sign-in-submission';
import { JwtResponse } from 'src/app/model/jwt-response';
import { StorageService } from '../auth-storage/storage.service';


const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private USERAPI = "https://localhost:8085/user/";
  private USERAuthAPI = "https://localhost:8085/auth/";
  // private USERAuthAPI = "http://localhost:8085/auth/";



  constructor(private http: HttpClient, private tokenStorageService: StorageService) { }

  private genHeader(): HttpHeaders {
    return new HttpHeaders().set('Authorization', this.tokenStorageService.getToken());
  }

  startRegister(): Observable<any>{
    return this.http.get(this.USERAPI + 'register'); 
  }

  registerUser(user, taskId) : Observable<any>{
    return this.http.post(this.USERAPI + "register/".concat(taskId), user);
  }

  registerEditor(user) : Observable<any>{
    return this.http.post(this.USERAPI + "newEditor", user, {headers: this.genHeader()});
  }

  startReviewerConfirmation(taskId): Observable<any>{
    return this.http.get(this.USERAPI + 'reviewerConfirmationStart/'.concat(taskId), {headers: this.genHeader()}); 
  }

  postReviewerConfirmation(activateAsReviewer, taskId) : Observable<any>{
    return this.http.get(this.USERAPI + "reviewerConfirmationEnd/".concat(taskId, '?confirm=', activateAsReviewer), {headers: this.genHeader()});
  }

  signIn(signInDto: SignInDto): Observable<JwtResponse>{
    return this.http.post<JwtResponse>(this.USERAuthAPI + 'signin', signInDto); 
  }

  signOut(user, taskId) : Observable<any>{
    return this.http.post(this.USERAPI + "register/".concat(taskId), user);
  }
  
}
