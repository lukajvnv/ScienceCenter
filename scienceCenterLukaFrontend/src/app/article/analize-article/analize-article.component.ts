import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, UrlHandlingStrategy, Router } from '@angular/router';
import { ArticleService } from 'src/app/service/article/article.service';
import { Article } from 'src/app/model/article';
import { DomSanitizer } from '@angular/platform-browser';
import { EncodeDecode } from 'src/app/util/base64';
import { ToastrService } from 'ngx-toastr';
import { FormFieldDtoWrapper } from 'src/app/model/form-field-dto';

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

  private formFieldsWrapper: FormFieldDtoWrapper[] = [];

  private display: boolean = true;

  // private fileUrl;

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

          this.displayArticle.fields.forEach( (field) =>{
            let newFieldWrapper = new FormFieldDtoWrapper(field);
            if( field.type.name=='enum'){
              newFieldWrapper.dataSource = Object.keys(field.type.values);
            }
            this.formFieldsWrapper.push(newFieldWrapper);
          });
        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  analizeBasicSubmit(fieldValues, form){
    let valid = this.validate(fieldValues, form);
    if(!valid){
      return;
    }

    this.displayArticle.fieldResults = [];
    for(let fieldWrapper of this.formFieldsWrapper) {
      let value = fieldValues[fieldWrapper.field.id];

      let multiple: string = (fieldWrapper.field.properties['multiple']) ? 'multiple' : '';
      if(multiple === 'multiple'){
        let multi: Array<string> = value;
        value = multi.join(':');
      }

      if( fieldWrapper.field.type.name=='boolean' && value === ''){
        value = false;
      }

      this.displayArticle.fieldResults.push({fieldId : fieldWrapper.field.id, fieldValue : value, multiple: multiple, multiEnum: ''});
      this.displayArticle.fields = [];
    
    }

    let x = this.articleService.analizeBasicResultPost(this.taskIdTopic, this.displayArticle);

    x.subscribe(res => {

      if(res.redirect){
        this.toastrService.success('Analyzing article is finished.');
        this.router.navigate(['home']);
        return;
      }

      
      this.article = res.displayArticle;
      console.log(this.article.file.toString());
      this.taskIdText = res.displayArticle.taskId;
      const data = 'some text';
      // const blob = new Blob([this.article.file.toString()], { type: 'application/octet-stream' });
      
      // let t1 = EncodeDecode.b64DecodeUnicode(this.article.file.toString());
      // const blob1 = new Blob([t1], { type: 'application/octet-stream' });
      // this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob1));

      // let t2 = EncodeDecode.b64Decode(this.article.file);
      let t2 = this.article.file;
      // let t2 = atob(this.article.file.toString());
      // let t2 = EncodeDecode.b64DecodeUnicode(this.article.file);
      // const blob2 = new Blob([t2], { type: 'application/pdf' });
      // this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob2));

      // this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob));
      // this.fileUrl = window.URL.createObjectURL(blob);

      let r: Blob = new Blob();

      this.formFieldsWrapper = [];
      res.fields.forEach( (field) =>{
        let newFieldWrapper = new FormFieldDtoWrapper(field);
        if( field.type.name=='enum'){
          newFieldWrapper.dataSource = Object.keys(field.type.values);
        }
        this.formFieldsWrapper.push(newFieldWrapper);
      });

      this.display = false;
    }, err => {
      console.log("Error occured");
        this.toastrService.error(err.error);
    });
  }

  preview(){
    // let blob:any = new Blob([this.article.file.toString()], { type: 'text/json; charset=utf-8' });
    let blob:any = new Blob([this.article.file.toString()]);
    const url= window.URL.createObjectURL(blob);
    window.open(url);
    // window.location.href = url
  }

  analizeTextSubmit(fieldValues, form){
    this.displayArticle.fieldResults = [];
    for(let fieldWrapper of this.formFieldsWrapper) {
      let value = fieldValues[fieldWrapper.field.id];

      let multiple: string = (fieldWrapper.field.properties['multiple']) ? 'multiple' : '';
      if(multiple === 'multiple'){
        let multi: Array<string> = value;
        value = multi.join(':');
      }

      if( fieldWrapper.field.type.name=='boolean' && value === ''){
        value = false;
      }

      this.displayArticle.fieldResults.push({fieldId : fieldWrapper.field.id, fieldValue : value, multiple: multiple, multiEnum: '',
      });
      this.displayArticle.fields = [];
    
    }

    // if(this.comment === '') {this.comment = 'x';}
    // let x = this.articleService.analizeTextResult(this.taskIdText, this.comment, this.isTextOk);

    let x = this.articleService.analizeTextResultPost(this.taskIdText, this.displayArticle);

    x.subscribe(res => {
      this.article = res;

      this.toastrService.success('Analyzing article is finished.');
      this.router.navigate(['home']);

    }, err => {

    });
  }

  download() {
    let articleId: number = this.displayArticle.displayArticle.articleId;
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

  validate(fieldValues, form) {
    this.formFieldsWrapper.forEach(field => field.errorField = '');

    for(let fieldWrapper of this.formFieldsWrapper) {
      let value = fieldValues[fieldWrapper.field.id];

      let fieldValid = this.validateByType(value, fieldWrapper);
      if(!fieldValid){
        return false;
      }

      console.log(fieldWrapper);
    }

    return true;
  }

  validateByType(value, fieldWrapper) {
    for(let constraint of fieldWrapper.field.validationConstraints){
      switch(constraint.name){
        case 'required':
          if(value === ''){
            fieldWrapper.errorField = 'Value is required';
            return false;
          }

          if(value instanceof Array){
            if(value.length === 0){
              fieldWrapper.errorField = 'Please select something';
              return false;
            }
            
          }
          break;
        case 'minlength':
          if(value.length < +constraint.configuration){
            fieldWrapper.errorField = 'Min length should be: ' + constraint.configuration;
            return false;
          }
          break;
        case 'maxlength':
          if(value.length > +constraint.configuration){
            fieldWrapper.errorField = 'Max length should be: ' + constraint.configuration;
            return false;
          }
          break;
        case 'min':
          if(value < +constraint.configuration){
            fieldWrapper.errorField = 'Minimum value should be: ' + constraint.configuration;
            return false;
          }
          break;
        case 'max':
          if(value > +constraint.configuration){
            fieldWrapper.errorField = 'Maximum value should be: ' + constraint.configuration;
            return false;
          }
          break;
        case 'readonly':
          break;
        default:
          break;
      }
    }


    return true;
  }

}
