import { Component, OnInit } from '@angular/core';
import { ReviewingDto } from 'src/app/model/reviewing-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/service/review/review.service';
import { ToastrService } from 'ngx-toastr';
import { DomSanitizer } from '@angular/platform-browser';
import { EncodeDecode } from 'src/app/util/base64';

@Component({
  selector: 'app-review-additional',
  templateUrl: './review-additional.component.html',
  styleUrls: ['./review-additional.component.css']
})
export class ReviewAdditionalComponent implements OnInit {

  private taskId: string;

  private reviewingDto: ReviewingDto;

  private fileUrl;


  private possibleOpinionsValues: string[] = ['ACCEPTED','REJECTED','DO_MINOR_CHANGES', 'DO_MAJOR_CHANGES'];
  // private displayedMagazine: Magazine;
  // private displayedArticle: Article;
  // private opinion: OpinionAboutArticle;

  constructor(private activatedRoute: ActivatedRoute, private reviewService: ReviewService, private router: Router,
    private toastr: ToastrService, private sanitizer: DomSanitizer, private toasterService: ToastrService) { }

  ngOnInit() {
   
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;

      let x = this.reviewService.startReviewAdditional(taskId);

      x.subscribe(
        res => {
          console.log(res);
          this.reviewingDto = res;
          // this.displayedMagazine = res.magazine;
          // this.displayedArticle = res.article;
          // this.opinion = res.opinion;

          let t1 = EncodeDecode.b64DecodeUnicode(this.reviewingDto.article.file.toString());
          const blob1 = new Blob([t1], { type: 'application/octet-stream' });
          this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob1));
          
        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  reviewPost(){
    let x = this.reviewService.postReviewAdditional(this.reviewingDto, this.taskId);

    x.subscribe( res => {
      console.log('resi');

      this.toasterService.success('Analyzing article is finished.');
      this.router.navigate(['home']);
    }, err => {

    })

  }

}
