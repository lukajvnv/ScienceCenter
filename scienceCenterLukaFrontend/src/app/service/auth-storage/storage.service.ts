import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';

const TOKEN_KEY = 'AuthToken';
const USERNAME_KEY = 'AuthUsername';
const AUTHORITIES_KEY = 'AuthAuthorities';
const USER_KEY = 'AuthUser;';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  private roles: Array<string> = [];

  isLoggedUser: Subject<boolean> = new Subject<boolean>();
  role: Subject<string> = new Subject<string>();


  constructor() { }

  public signOut() {
    window.sessionStorage.clear();
    this.isLoggedUser.next(false);
    this.role.next(undefined);
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

    this.role.next(this.getRole());
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

  public getRole(): string {
    return this.getAuthorities()[0];
  }

  public hasRole(role: string): boolean{
    return this.getAuthorities().filter(a => a === role) ? true : false; 
  }

  public saveUser(user: number) {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(): string {
    return sessionStorage.getItem(USER_KEY);
  }

}
