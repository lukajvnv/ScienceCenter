import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from 'src/app/service/article/article.service';
import { NewArticle } from 'src/app/model/new-article';
import { Term } from 'src/app/model/term';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-new-article',
  templateUrl: './new-article.component.html',
  styleUrls: ['./new-article.component.css']
})
export class NewArticleComponent implements OnInit {

  private articleFormDto = null;
  private taskId: string;
  private processInstanceId: string;

  private articleData: NewArticle = new NewArticle();
  private customTerm: Term = new Term();
  private customUser: User = new User();

  private activeForm: string = 'custom';

  constructor(private activatedRoute: ActivatedRoute, private articleService: ArticleService, private router: Router) { }

  ngOnInit() {
    // this.activeForm == 'custom';
    this.activatedRoute.paramMap.subscribe(data => {
      const magazineId = data.get("magazineId");
      
      let x = this.articleService.startNewArticle(magazineId);

      x.subscribe(
        res => {
          console.log(res);
          this.articleFormDto = res;
          this.taskId = res.taskId;
          this.processInstanceId = res.processInstanceId;
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
    // let  file: File;
    // const imageWrapper: DisplayImageWrapper = new DisplayImageWrapper();
    // for (const f of event.target.files) {
    //   let  file: File;

    //   // provera tipa na ulazu
    //   const type: string = f.type;
    //   const contains: boolean = type.includes('image');
    //   if (!contains) {
    //     this.toastrService.warning('Dozvoljen je unos samo slike');
    //     return;
    //   }
    //   const imageWrapper: DisplayImageWrapper = new DisplayImageWrapper();

    //   file = f;
    //   const reader = new FileReader();

    //   // ucitavanje fajla preko readera, pregled fajla -> filter formata
    //   reader.onload = () => {

    //     imageWrapper.putanja = file.name;
    //     imageWrapper.value = reader.result;
    //     this.selectedImages.push(imageWrapper);
    //   };
    //   // ovo ispod trigerije ovo iznad
    //   reader.readAsDataURL(file);   // rezultat na kraju je 64bitni enkodirana slika
    // }

    let file: File = event.target.files[0];
    const reader = new FileReader();

      // ucitavanje fajla preko readera, pregled fajla -> filter formata
      reader.onload = () => {

        console.log(file.name);
        console.log(reader);
        console.log(reader.result);
        this.articleData.file = reader.result;
        
      };
      // ovo ispod trigerije ovo iznad
      // https://developer.mozilla.org/en-US/docs/Web/API/FileReader
      // reader.readAsArrayBuffer(file);
      // reader.readAsBinaryString(file);
      // reader.readAsText(file);
      reader.readAsDataURL(file);   // rezultat na kraju je 64bitni enkodirana string
      
    console.log(event);

    
  }

  onSubmit(value, form){
    console.log(this.articleData);
    console.log(this.taskId);

    let x = this.articleService.postNewArticle(this.articleData, this.taskId);

    x.subscribe(res => {
      console.log(res);
    }, err => {

    });
  }

}
