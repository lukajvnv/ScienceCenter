import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  // private SERVERAPI = "http://localhost:8085/start/";
  // private USERAPI = "http://localhost:8085/start/";


  // constructor(private http: HttpClient) { }

  // startProcess(): Observable<any>{
  //   return this.http.get(this.SERVERAPI + 'register'); 
  // }

  // registerUser(user, taskId) : Observable<any>{
  //   return this.http.post(this.USERAPI + "/post/".concat(taskId), user);
  // }
}
