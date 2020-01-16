import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  flag: boolean;

  constructor() { }

  ngOnInit() {
  }

  signOut(){
    this.flag = true;
  }

}
