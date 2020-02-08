import { Component, OnInit } from '@angular/core';
import { StorageService } from 'src/app/service/auth-storage/storage.service';
import { Router } from '@angular/router';

const editor_id = 'editor';
const admin_id = 'camunda-admin';
const reviewer_id = 'reviewer';
const guest_id = 'guest';
const author_id = 'author';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  isUserLogged: boolean;
  role: String;

  groups: string[] = [];

  rev :boolean = false;
  editor :boolean = false;
  admin: boolean = false;

  constructor(private tokenStorage: StorageService, private router: Router) { }

  ngOnInit() {
    // if (this.tokenStorage.getToken()) {
    //   this.isUserLogged = true;
    // } else {
    //   this.isUserLogged = false;
    // }
    this.isUserLogged = this.tokenStorage.isLogged();
    this.tokenStorage.isLoggedUser.subscribe(value => {
      this.isUserLogged = value;
    });

    // this.role = this.tokenStorage.getRole();
    // this.tokenStorage.role.subscribe(value => {
    //   this.role = value;
    // });

    this.groups = this.tokenStorage.getGroups();
    this.rev = this.isReviewer();
      this.editor = this.isEditor();
      this.admin = this.isAdmin();
    this.tokenStorage.groups.subscribe(value => {
      this.groups = value;
      this.rev = this.isReviewer();
      this.editor = this.isEditor();
      this.admin = this.isAdmin();
    })
  }

  signOut() {
    this.tokenStorage.signOut();
    this.router.navigate(['home']);
  }

  public isAdmin(): boolean {
    return this.groups.includes(admin_id);
  }

  public isEditor(): boolean {
    return this.groups.includes(editor_id);
  }

  public isReviewer(): boolean {
    return this.groups.includes(reviewer_id);
  }

}
