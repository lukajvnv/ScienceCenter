import { Component, OnInit } from '@angular/core';
import { NewArticle } from 'src/app/model/new-article';
import { Term } from 'src/app/model/term';
import { User } from 'src/app/model/user';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from 'src/app/service/article/article.service';
import { UpdateArticleChanges } from 'src/app/model/update-article-changes-dto';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-update-article-changes',
  templateUrl: './update-article-changes.component.html',
  styleUrls: ['./update-article-changes.component.css']
})
export class UpdateArticleChangesComponent implements OnInit {

  private activateTab: string;

  private update: UpdateArticleChanges = null;
  private articleFormDto = null;
  private taskId: string;
  private processInstanceId: string;

  private articleData: NewArticle = new NewArticle();
  private customTerm: Term = new Term();
  private customUser: User = new User();

  private activeForm: string = 'custom';

  constructor(private activatedRoute: ActivatedRoute, private articleService: ArticleService, private router: Router, private toastr: ToastrService) { }

  ngOnInit() {
    // this.activeForm == 'custom';
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      // this.activateTab = 'updateForm';
      
      let x = this.articleService.startUpdateChangesArticle(taskId);

      x.subscribe(
        res => {
          this.update = res;
          console.log(res);
          this.articleFormDto = res.newArticleRequestDto;
          this.taskId = res.newArticleRequestDto.taskId;
          this.processInstanceId = res.newArticleRequestDto.processInstanceId;

          this.articleData = res.newAarticleResponseDto;

          this.activateTab = 'updateForm';

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

    this.update.newAarticleResponseDto = this.articleData;

    let x = this.articleService.updateChangesArticle(this.update, this.taskId);

    x.subscribe(res => {
      console.log(res);
      this.toastr.success('Operations of update article is completed.');
      this.router.navigate(['home']);
    }, err => {

    });
  }

}
