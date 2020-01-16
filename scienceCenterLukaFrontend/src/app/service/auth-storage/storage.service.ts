import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

const TOKEN_KEY = 'AuthToken';
const USERNAME_KEY = 'AuthUsername';
const AUTHORITIES_KEY = 'AuthAuthorities';
const USER_KEY = 'AuthUser;';
const RESERVED = 'Reserved;';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private roles: Array<string> = [];

  isLoggedUser: Subject<boolean> = new Subject<boolean>();


  constructor() { }

  public signOut() {
    window.sessionStorage.clear();
    this.isLoggedUser.next(false);
  }

  public saveToken(token: string) {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
    this.isLoggedUser.next(true);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public isLogged(): boolean {
    return this.getToken() ? true : false;
  }

  public saveUsername(username: string) {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.setItem(USERNAME_KEY, username);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY);
  }

  public saveAuthorities(authorities: string[]) {
    window.sessionStorage.removeItem(AUTHORITIES_KEY);
    window.sessionStorage.setItem(AUTHORITIES_KEY, JSON.stringify(authorities));
  }

  public getAuthorities(): string[] {
    this.roles = [];

    if (sessionStorage.getItem(TOKEN_KEY)) {
      JSON.parse(sessionStorage.getItem(AUTHORITIES_KEY)).forEach(authority => {
        this.roles.push(authority.authority);
      });
    }

    return this.roles;
  }

  public saveUser(user: number) {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): string {
    return sessionStorage.getItem(USER_KEY);
  }

  // public saveReserved(num: number) {
  //   window.sessionStorage.removeItem(RESERVED);
  //   window.sessionStorage.setItem(RESERVED, JSON.stringify(num));
  // }

  // public getReserved(): string {
  //   return sessionStorage.getItem(RESERVED);
  // }
}
