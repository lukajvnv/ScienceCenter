import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model/user';
import { Term } from 'src/app/model/term';
import { NewArticle } from 'src/app/model/new-article';
import { ArticleService } from 'src/app/service/article/article.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

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

  private activeForm: string = 'custom';

  constructor(private activatedRoute: ActivatedRoute, private articleService: ArticleService, private router: Router, private toastrService: ToastrService) { }

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

  onFileUpload(event) {
    
    let file: File = event.target.files[0];
    const reader = new FileReader();

      // ucitavanje fajla preko readera, pregled fajla -> filter formata
      reader.onload = () => {

        console.log(file.name);
        console.log(reader);
        console.log(reader.result);
        this.articleData.file = reader.result;
        
      };
      
      reader.readAsDataURL(file);   // rezultat na kraju je 64bitni enkodirana string
      
    console.log(event);

    
  }

  onSubmit(value, form){
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
