import { Component, OnInit } from '@angular/core';
import { ReviewingDto } from 'src/app/model/reviewing-dto';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/service/review/review.service';
import { ToastrService } from 'ngx-toastr';
import { DomSanitizer } from '@angular/platform-browser';
import { EncodeDecode } from 'src/app/util/base64';
import { FormFieldDtoWrapper } from 'src/app/model/form-field-dto';
import { ArticleService } from 'src/app/service/article/article.service';

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

  private formFieldsWrapper: FormFieldDtoWrapper[] = [];


  constructor(private activatedRoute: ActivatedRoute, private reviewService: ReviewService, private router: Router,
    private toastr: ToastrService, private sanitizer: DomSanitizer, private toasterService: ToastrService, private articleService: ArticleService) { }

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

          // let t1 = EncodeDecode.b64DecodeUnicode(this.reviewingDto.article.file.toString());
          // const blob1 = new Blob([t1], { type: 'application/octet-stream' });
          // this.fileUrl = this.sanitizer.bypassSecurityTrustResourceUrl(window.URL.createObjectURL(blob1));
          
          res.fields.forEach( (field) =>{
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

  reviewPost(fieldValues, form){
    let valid = this.validate(fieldValues, form);
    if(!valid){
      return;
    }

    let fieldResults: any[] = [];
    for(let fieldWrapper of this.formFieldsWrapper) {
      let value = fieldValues[fieldWrapper.field.id];

      let multiple: string = (fieldWrapper.field.properties['multiple']) ? 'multiple' : '';
      if(multiple === 'multiple'){
        let multi: Array<string> = value;
    
        value = multi.join(':');
      }
      fieldResults.push({fieldId : fieldWrapper.field.id, fieldValue : value, multiple: multiple,  multiEnum: ''});

    }

    this.reviewingDto.fields = [];
    this.reviewingDto.fieldResults = fieldResults;

    let x = this.reviewService.postReviewAdditional(this.reviewingDto, this.taskId);

    x.subscribe( res => {
      console.log('resi');

      this.toasterService.success('Analyzing article is finished.');
      this.router.navigate(['home']);
    }, err => {

    })

  }

  download() {
    let articleId: number = this.reviewingDto.article.articleId;
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
          if(!value){
            fieldWrapper.errorField = 'Value is required';
            return false;
          }

          if(value instanceof Array){
            if(value.length < 2){
              fieldWrapper.errorField = 'Please select at least 2';
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
