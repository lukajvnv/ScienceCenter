import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // private loginUrl = 'http://localhost:' + AppConfigService.settings.backend.serverPort + '/agent/api/auth/signin';
  // private signupUrl = 'http://localhost:' + AppConfigService.settings.backend.serverPort + '/agent/api/auth/signup';

  // constructor(private http: HttpClient) {
  // }



  // attemptAuth(credentials: AuthLoginInfo): Observable<JwtResponse> {
  //   return this.http.post<JwtResponse>(this.loginUrl, credentials, httpOptions);
  // }

  // signUp(info: SignUpInfo): Observable<any> {
  //   return this.http.post<string>(this.signupUrl, info, httpOptions);
  // }
}
