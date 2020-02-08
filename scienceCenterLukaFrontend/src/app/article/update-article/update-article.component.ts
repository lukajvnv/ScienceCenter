import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model/user';
import { Term } from 'src/app/model/term';
import { NewArticle } from 'src/app/model/new-article';
import { ArticleService } from 'src/app/service/article/article.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ScienceArea } from 'src/app/model/science-area';
import { EncodeDecode } from 'src/app/util/base64';
import { DomSanitizer } from '@angular/platform-browser';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-update-article',
  templateUrl: './update-article.component.html',
  styleUrls: ['./update-article.component.css']
})
export class UpdateArticleComponent implements OnInit {

  private update = null;
  private articleFormDto = null;
  private taskId: string;
  private processInstanceId: string;

  private articleData: NewArticle = new NewArticle();
  private customTerm: Term = new Term();
  private customUser: User = new User();

  private scArea: ScienceArea = new ScienceArea();
  private magScArea: ScienceArea[] = [];

  private activeForm: string = 'custom';

  private fileUrl;

  constructor(private activatedRoute: ActivatedRoute, private articleService: ArticleService, private router: Router, private toastrService: ToastrService
    , private sanitizer: DomSanitizer) { }

  ngOnInit() {
    // this.activeForm == 'custom';
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      
      let x = this.articleService.startUpdateArticle(taskId);

      x.subscribe(
        res => {
          this.update = res;
          console.log(res);
          this.articleFormDto = res.newArticleRequestDto;
          this.taskId = res.newArticleRequestDto.taskId;
          this.processInstanceId = res.newArticleRequestDto.processInstanceId;

          this.articleData = res.newAarticleResponseDto;

          this.magScArea = this.articleFormDto.articleScienceAreas;
          this.scArea = this.magScArea.filter(sc => sc.scienceAreaId === +this.articleData.articleScienceArea)[0];
        
        
          // let t1 = EncodeDecode.b64DecodeUnicode(this.articleData.file.toString());
          // const blob1 = new Blob([t1], { type: 'application/octet-stream' });
          // this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob1));
        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  add(term: Term){
    this.articleData.articleTerm.push(term);
  }

  delete(term: Term){
    let index = this.articleData.articleTerm.indexOf(term);
    this.articleData.articleTerm.splice(index, 1);
  }

  addCustom(term: Term){
    const t: Term = new Term();
    // t.termId = -1;
    t.termName = term.termName
    this.articleData.articleTerm.push(t);
    term.termName = '';
  }

  addCustomCouathor(value, form){
    
    this.articleData.articleCoAuthors.push(this.customUser);
    this.customUser = new User();
    form.submitted = false;
  }

  deleteCoauthor(user: User){
    let index = this.articleData.articleCoAuthors.indexOf(user);
    this.articleData.articleCoAuthors.splice(index, 1);
  }

  addExisting(user: User){
    this.articleData.articleCoAuthors.push(user);
  }

  // onFileUpload(event) {
    
  //   let file: File = event.target.files[0];
  //   const reader = new FileReader();

  //     // ucitavanje fajla preko readera, pregled fajla -> filter formata
  //     reader.onload = () => {

  //       console.log(file.name);
  //       console.log(reader);
  //       console.log(reader.result);
  //       this.articleData.file = reader.result;
        
  //     };
      
  //     reader.readAsDataURL(file);   // rezultat na kraju je 64bitni enkodirana string
      
  //   console.log(event);

    
  // }

  onFileUpload(event) {
    let fileList: FileList = event.target.files;
    let file: File = fileList.item(0);
    
    let formData = new FormData(); 
    formData.append('file', file, file.name); 

    let x = this.articleService.uploadFile(this.taskId, formData);

    x.subscribe(data => {

    }, err => {
      console.log(err.error);
    });
    
  }

  download() {
    let articleId: number = this.update.articleId;
    this.articleService.downloadFile(articleId).subscribe( (data: Blob )=> {
			//let blob:any = new Blob([response.blob()], { type: 'text/json; charset=utf-8' });
			//const url= window.URL.createObjectURL(blob);
      //window.open(url);

      
      
      var file = new Blob([data], { type: 'application/pdf' })
      var fileURL = URL.createObjectURL(file);

      //window.location.href = data.type;

      window.open(fileURL); 
      var a         = document.createElement('a');
      a.href        = fileURL; 
      a.target      = '_blank';
      a.download    = 'bill.pdf';
      document.body.appendChild(a);
      a.click();
      
			//fileSaver.saveAs(blob, 'employees.json');
		}, error =>  {
      console.log('Error downloading the file');
    });  
  }


  onSubmit(value, form) {
    console.log(this.articleData);
    console.log(this.taskId);

    let x = this.articleService.updateArticle(this.articleData, this.taskId);

    x.subscribe(res => {
      console.log(res);
      this.toastrService.success('New article published! You will be informed for the further instructions.');
      this.router.navigate(['home']);
    }, err => {

    });
  }

}
