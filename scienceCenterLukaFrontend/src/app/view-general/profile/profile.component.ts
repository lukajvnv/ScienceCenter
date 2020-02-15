import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { StorageService } from 'src/app/service/auth-storage/storage.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private activateTab: string;

  private user;

  constructor(private router: Router, private tokenStorage: StorageService, private userService: UserService) { }

  ngOnInit() {
    this.activateTab = 'profile';

    this.userService.getUser().subscribe(data => {
      this.user = data;
    });

  }

}
