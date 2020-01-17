import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/service/review/review.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-define-time-for-reviewing',
  templateUrl: './define-time-for-reviewing.component.html',
  styleUrls: ['./define-time-for-reviewing.component.css']
})
export class DefineTimeForReviewingComponent implements OnInit {

  private taskId: string;

  private days: number;
  private hours: number;
  private minutes: number;

  constructor(private activatedRoute: ActivatedRoute, private reviewService: ReviewService, private router: Router,
    private toastr: ToastrService) { }

  ngOnInit() {
   

    // this.activeForm == 'custom';
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;

      // let x = this.reviewService.startAddingReviewers(taskId);

      // x.subscribe(
      //   res => {
      //     console.log(res);
      //     // this.articleFormDto = res;
      //     this.taskId = res.taskId;
          
      //   },
      //   err => {
      //     console.log("Error occured");
      //   }
      // );
    });
  }

  postResult(){
    if(!this.days && !this.minutes && !this.hours){
      this.toastr.error("At least one field you have to fill in!");
      return;
    }

    if(this.days <= 0 || this.minutes <= 0 || this.hours <= 0){
      this.toastr.error("Invalid input!");
      return;
    }

    if(!this.days) this.days = 0;
    if(!this.hours) this.hours = 0;
    if(!this.minutes) this.minutes = 0;

    let template = 'PxDTyHzM';
    template = template.replace('x', this.days.toString());
    template = template.replace('y', this.hours.toString());
    template = template.replace('z', this.minutes.toString());

    const time: string = 'P' + 'D' + 'H' + 'M';

    let x = this.reviewService.defineTimeForReview(this.taskId, template);

    x.subscribe(res => {
      this.toastr.success('Operations of define time for reviewing is completed.');
      this.router.navigate(['home']);
    }, err => {

    });
  }

}
