import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { Magazine } from 'src/app/model/magazine';
import { StorageService } from 'src/app/service/auth-storage/storage.service';

@Component({
  selector: 'app-view-magazine',
  templateUrl: './view-magazine.component.html',
  styleUrls: ['./view-magazine.component.css']
})
export class ViewMagazineComponent implements OnInit {

  private magazine: Magazine;
  private isLogged: boolean;

  private magazineId: string;

  private editions: any[];

  constructor(private activatedRoute: ActivatedRoute, private magazineService: MagazineService, private router: Router,
    private tokenStorageService: StorageService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const magazineId = data.get("magazineId");
      this.magazineId = magazineId;
      
      let x = this.magazineService.getMagazine(magazineId);
      this.isLogged = this.tokenStorageService.isLogged();

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
    this.router.navigate(['new-article-init', this.magazine.magazineId])
  }

  viewEditions(){
    let x = this.magazineService.getEditions(this.magazineId);

    x.subscribe(data => {
      this.editions = data;
    });
  }

  subscribe(){

  }

  displayEdition(id: string){

  }

}
