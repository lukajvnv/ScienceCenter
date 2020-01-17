import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/service/review/review.service';
import { Article } from 'src/app/model/article';
import { Magazine } from 'src/app/model/magazine';
import { EditorReviewer } from 'src/app/model/editor-reviewer-dto';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-add-reviewer',
  templateUrl: './add-reviewer.component.html',
  styleUrls: ['./add-reviewer.component.css']
})
export class AddReviewerComponent implements OnInit {

  private taskId: string;
  private processInstanceId: string;

  private displayedArticle: Article;
  private displayedMagazine: Magazine;

  private scFilter: boolean = false;
  private geoFilter: boolean = false;

  private possibleReviewers: EditorReviewer[];
  private filteredReviewers: EditorReviewer[] = [];
  private selectedReviewers: EditorReviewer[] = [];

  constructor(private activatedRoute: ActivatedRoute, private reviewService: ReviewService, private router: Router,
    private toastr: ToastrService) { }

  ngOnInit() {
    console.log('Bg-NS: ' + this.getDistanceFromLatLonInKm(45.27, 19.83, 44.79, 20.45));
    console.log('NI-NS: ' + this.getDistanceFromLatLonInKm(45.27, 19.83, 43.32, 21.89));
    console.log('NS-Rio de Janeiro: ' + this.getDistanceFromLatLonInKm(45.27, 19.83, -22.9, -43.17));

    // this.activeForm == 'custom';
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;
      
      let x = this.reviewService.startAddingReviewers(taskId);

      x.subscribe(
        res => {
          console.log(res);
          // this.taskId = res.taskId;
          // this.processInstanceId = res.processInstanceId;
          this.displayedArticle = res.articleDto;
          this.displayedMagazine = res.magazineDto;
          this.possibleReviewers = res.editorsReviewersDto;
          this.filteredReviewers = res.editorsReviewersDto;
        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  add(rev: EditorReviewer){
    this.selectedReviewers.push(rev);
  }

  delete(rev: EditorReviewer){
    let index = this.selectedReviewers.indexOf(rev);
    this.selectedReviewers.splice(index, 1);
  }

  addReviewer(){
    if(this.selectedReviewers.length < 2){
      this.toastr.error('Please select at least 2 reviewers');
      return;
    }

    let x = this.reviewService.postAddingReviewers(this.taskId, this.selectedReviewers);

    x.subscribe(res => {
      console.log('resi');

      this.toastr.success('Operations of adding reviewer is completed.');
      this.router.navigate(['home']);
    }, err => {

    });
  }

  filter(){
    if(this.scFilter){
      this.filteredReviewers = this.filteredReviewers.filter(r => r.scienceArea.scienceAreaId === this.displayedArticle.scienceArea.scienceAreaId);
    }

    if(!this.scFilter && !this.geoFilter){
      this.filteredReviewers = this.possibleReviewers;
    }
  }

  getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
    var R = 6371; // Radius of the earth in km
    var dLat = this.deg2rad(lat2-lat1);  // deg2rad below
    var dLon = this.deg2rad(lon2-lon1); 
    var a = 
      Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * 
      Math.sin(dLon/2) * Math.sin(dLon/2)
      ; 
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
    var d = R * c; // Distance in km
    return d;
  }
  
   deg2rad(deg) {
    return deg * (Math.PI/180)
  }

}
