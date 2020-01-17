import { Component, OnInit } from '@angular/core';
import { ReviewingDto } from 'src/app/model/reviewing-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/service/review/review.service';
import { ToastrService } from 'ngx-toastr';
import { DomSanitizer } from '@angular/platform-browser';
import { EncodeDecode } from 'src/app/util/base64';
import { ReviewingEditorDto } from 'src/app/model/reviewing-editor-dto';
import { OpinionAboutArticle } from 'src/app/model/opinion-about-article';

@Component({
  selector: 'app-review-editor',
  templateUrl: './review-editor.component.html',
  styleUrls: ['./review-editor.component.css']
})
export class ReviewEditorComponent implements OnInit {

  private taskId: string;

  private reviewingEditorDto: ReviewingEditorDto;

  private displayEditorReview: boolean = true;

  private reviewToDisplay: OpinionAboutArticle;

  private fileUrl;


  private possibleOpinionsValues: string[] = ['ACCEPTED','REJECTED','DO_MINOR_CHANGES', 'DO_MAJOR_CHANGES', 'ADD_ADDITIONAL_EDITOR'];
 

  constructor(private activatedRoute: ActivatedRoute, private reviewService: ReviewService, private router: Router,
    private toastr: ToastrService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
   
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;

      let x = this.reviewService.startReviewEditor(taskId);

      x.subscribe(
        res => {
          console.log(res);
          this.reviewingEditorDto = res;
        
          let t1 = EncodeDecode.b64DecodeUnicode(this.reviewingEditorDto.article.file.toString());
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
    let x = this.reviewService.postReviewEditor(this.reviewingEditorDto, this.taskId);

    x.subscribe( res => {
      console.log('resi');
      this.toastr.success('Operations of editor reviewing is completed.');
      this.router.navigate(['home']);
    }, err => {

    })

  }

}
