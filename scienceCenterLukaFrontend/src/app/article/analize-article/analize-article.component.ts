import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, UrlHandlingStrategy, Router } from '@angular/router';
import { ArticleService } from 'src/app/service/article/article.service';
import { Article } from 'src/app/model/article';
import { URL } from 'url';
import { DomSanitizer } from '@angular/platform-browser';
import { EncodeDecode } from 'src/app/util/base64';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-analize-article',
  templateUrl: './analize-article.component.html',
  styleUrls: ['./analize-article.component.css']
})
export class AnalizeArticleComponent implements OnInit {

  private taskIdTopic: string;
  private taskIdText: string;
  private displayArticle;
  private isOk: boolean = false;
  private isTextOk: boolean = false;
  private comment: string = '';
  private article: Article;

  private fileUrl;

  private proba: string | ArrayBuffer;

  constructor(private activatedRoute: ActivatedRoute, private articleService: ArticleService, private sanitizer: DomSanitizer, private toastrService: ToastrService,
    private router: Router) { }

  ngOnInit() {
    // this.activeForm == 'custom';
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskIdTopic = taskId;

      let x = this.articleService.analizeArticle(taskId);

      x.subscribe(
        res => {
          console.log(res);
          this.displayArticle = res;
        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  analizeBasicSubmit(form, value){
    let x = this.articleService.analizeBasicResult(this.taskIdTopic, this.isOk);

    x.subscribe(res => {

      if(!this.isOk){
        this.toastrService.success('Analyzing article is finished.');
        this.router.navigate(['home']);
      }

      this.article = res;
      console.log(this.article.file.toString());
      this.taskIdText = res.taskId;
      const data = 'some text';
      // const blob = new Blob([this.article.file.toString()], { type: 'application/octet-stream' });
      
      let t1 = EncodeDecode.b64DecodeUnicode(this.article.file.toString());
      const blob1 = new Blob([t1], { type: 'application/octet-stream' });
      this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob1));

      // let t2 = EncodeDecode.b64Decode(this.article.file);
      let t2 = this.article.file;
      // let t2 = atob(this.article.file.toString());
      // let t2 = EncodeDecode.b64DecodeUnicode(this.article.file);
      // const blob2 = new Blob([t2], { type: 'application/pdf' });
      // this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob2));

      // this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob));
      // this.fileUrl = window.URL.createObjectURL(blob);

      let r: Blob = new Blob();
    }, err => {

    });
  }

  preview(){
    // let blob:any = new Blob([this.article.file.toString()], { type: 'text/json; charset=utf-8' });
    let blob:any = new Blob([this.article.file.toString()]);
    const url= window.URL.createObjectURL(blob);
    window.open(url);
    // window.location.href = url
  }

  analizeTextSubmit(form, value){
    if(this.comment === '') {this.comment = 'x';}
    let x = this.articleService.analizeTextResult(this.taskIdText, this.comment, this.isTextOk);

    x.subscribe(res => {
      this.article = res;

      this.toastrService.success('Analyzing article is finished.');
      this.router.navigate(['home']);

    }, err => {

    });
  }

}
