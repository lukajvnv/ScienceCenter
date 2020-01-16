import { Component, OnInit } from '@angular/core';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-view-magazines',
  templateUrl: './view-magazines.component.html',
  styleUrls: ['./view-magazines.component.css']
})
export class ViewMagazinesComponent implements OnInit {

  private magazines;

  constructor(private magazineService: MagazineService, private router: Router) { }

  ngOnInit() {
    let x = this.magazineService.getAllMagazines();

    x.subscribe(res => {
      this.magazines = res;
    }, err => {

    });
  }

  detail(id: number){
    this.router.navigate(['view-magazine', id]);
  }

}
