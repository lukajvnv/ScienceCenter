import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Magazine } from 'src/app/model/magazine';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { CheckingMagazineDto } from 'src/app/model/checking-magazine-dto';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-check-new-magazine-data',
  templateUrl: './check-new-magazine-data.component.html',
  styleUrls: ['./check-new-magazine-data.component.css']
})
export class CheckNewMagazineDataComponent implements OnInit {

  private checkingMagazine: CheckingMagazineDto;
  private taskId: string;


  constructor(private activatedRoute: ActivatedRoute, private magazineService: MagazineService, private router: Router,
    private toastrService: ToastrService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;

      let x = this.magazineService.checkingMagazineDataStart(taskId);

      x.subscribe(
        res => {
          console.log(res);
          this.checkingMagazine = res;

        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  submit(value, form){
    let x = this.magazineService.checkingMagazineDataPost(this.checkingMagazine, this.taskId);

    x.subscribe(
      res => {
        console.log("Rijesi");   
        this.toastrService.success("Operation completed");
        this.router.navigate(['home']);      

      },
      err => {
        console.log("Error occured");
      }
    );

  
  }

}
