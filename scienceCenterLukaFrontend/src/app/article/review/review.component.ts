import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/service/review/review.service';
import { ToastrService } from 'ngx-toastr';
import { Magazine } from 'src/app/model/magazine';
import { Article } from 'src/app/model/article';
import { OpinionAboutArticle } from 'src/app/model/opinion-about-article';
import { ReviewingDto } from 'src/app/model/reviewing-dto';
import { EncodeDecode } from 'src/app/util/base64';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {

  private taskId: string;

  private reviewingDto: ReviewingDto;

  private fileUrl;


  private possibleOpinionsValues: string[] = ['ACCEPTED','REJECTED','DO_MINOR_CHANGES', 'DO_MAJOR_CHANGES'];
  // private displayedMagazine: Magazine;
  // private displayedArticle: Article;
  // private opinion: OpinionAboutArticle;

  constructor(private activatedRoute: ActivatedRoute, private reviewService: ReviewService, private router: Router,
    private toastr: ToastrService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
   
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;

      let x = this.reviewService.startReview(taskId);

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
    let x = this.reviewService.postReview(this.reviewingDto, this.taskId);

    x.subscribe( res => {
      console.log('resi');
    }, err => {

    })

  }

}
