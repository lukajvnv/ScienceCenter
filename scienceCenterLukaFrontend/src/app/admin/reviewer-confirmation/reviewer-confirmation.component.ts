import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user/user.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-reviewer-confirmation',
  templateUrl: './reviewer-confirmation.component.html',
  styleUrls: ['./reviewer-confirmation.component.css']
})
export class ReviewerConfirmationComponent implements OnInit {

  private taskId: string;
  private acceptAsReviewer: boolean = false;

  private displayDto = null;
  


  constructor(private userService: UserService, private toastrService: ToastrService, private activatedRoute: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;

      let x = this.userService.startReviewerConfirmation(taskId);

      x.subscribe(
        res => {
          this.displayDto = res;
          console.log("Rijesi");        


        
      },
        err => {
          console.log("Error occured");
      }
      );

    });

    
  }

  submit(value, form){
    let x = this.userService.postReviewerConfirmation(this.acceptAsReviewer, this.taskId);

    x.subscribe(
      res => {
        console.log("Rijesi");        

      },
      err => {
        console.log("Error occured");
      }
    );

  
  }

}
