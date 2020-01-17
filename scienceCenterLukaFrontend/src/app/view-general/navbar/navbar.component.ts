import { Component, OnInit } from '@angular/core';
import { StorageService } from 'src/app/service/auth-storage/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  isUserLogged: boolean;
  role: String;

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

    this.role = this.tokenStorage.getRole();
    this.tokenStorage.role.subscribe(value => {
      this.role = value;
    });
  }

  signOut() {
    this.tokenStorage.signOut();
    this.router.navigate(['home']);
  }

}
