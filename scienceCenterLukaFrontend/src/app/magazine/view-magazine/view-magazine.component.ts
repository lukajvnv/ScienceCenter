import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { Magazine } from 'src/app/model/magazine';

@Component({
  selector: 'app-view-magazine',
  templateUrl: './view-magazine.component.html',
  styleUrls: ['./view-magazine.component.css']
})
export class ViewMagazineComponent implements OnInit {

  private magazine: Magazine;


  constructor(private activatedRoute: ActivatedRoute, private magazineService: MagazineService, private router: Router) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const magazineId = data.get("magazineId");
      
      let x = this.magazineService.getMagazine(magazineId);

      x.subscribe(
        res => {
          console.log(res);
          this.magazine = res;

        
        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  newText(){
    this.router.navigate(['new-article', this.magazine.magazineId])
  }

  viewEditions(){

  }

  subscribe(){

  }

}
