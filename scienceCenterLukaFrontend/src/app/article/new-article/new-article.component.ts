import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from 'src/app/service/article/article.service';
import { NewArticle } from 'src/app/model/new-article';
import { Term } from 'src/app/model/term';
import { User } from 'src/app/model/user';
import { ToastrService } from 'ngx-toastr';
import { FormFieldDtoWrapper } from 'src/app/model/form-field-dto';

@Component({
  selector: 'app-new-article',
  templateUrl: './new-article.component.html',
  styleUrls: ['./new-article.component.css']
})
export class NewArticleComponent implements OnInit {

  private articleFormDto = null;
  private taskId: string;
  private processInstanceId: string;
  private formFieldsWrapper: FormFieldDtoWrapper[] = [];


  private articleData: NewArticle = new NewArticle();
  private customTerm: Term = new Term();
  private customUser: User = new User();

  private activeForm: string = 'custom';

  constructor(private activatedRoute: ActivatedRoute, private articleService: ArticleService, private router: Router, private toastrService: ToastrService) { }

  ngOnInit() {
    // this.activeForm == 'custom';
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      
      let x = this.articleService.startNewArticle(taskId);

      x.subscribe(
        res => {
          console.log(res);
          this.articleFormDto = res;
          this.taskId = res.taskId;
          this.processInstanceId = res.processInstanceId;

          this.articleFormDto.fields.forEach( (field) =>{
            let newFieldWrapper = new FormFieldDtoWrapper(field);
            if( field.type.name=='enum'){
              newFieldWrapper.dataSource = Object.keys(field.type.values);
            }

            if( field.type.name=='multiEnum' || field.type.name=='multiEnumCoAuthor'){
              newFieldWrapper.dataSource = Object.keys(field.type.values);
            }

            if(field.type.name === 'multiEnumCoAuthorOut' ){
            
            } else if (field.type.name === 'multiEnumTermOut') {

            }
             else {
              this.formFieldsWrapper.push(newFieldWrapper);
            }

           

          });
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
  //   // let  file: File;
  //   // const imageWrapper: DisplayImageWrapper = new DisplayImageWrapper();
  //   // for (const f of event.target.files) {
  //   //   let  file: File;

  //   //   // provera tipa na ulazu
  //   //   const type: string = f.type;
  //   //   const contains: boolean = type.includes('image');
  //   //   if (!contains) {
  //   //     this.toastrService.warning('Dozvoljen je unos samo slike');
  //   //     return;
  //   //   }
  //   //   const imageWrapper: DisplayImageWrapper = new DisplayImageWrapper();

  //   //   file = f;
  //   //   const reader = new FileReader();

  //   //   // ucitavanje fajla preko readera, pregled fajla -> filter formata
  //   //   reader.onload = () => {

  //   //     imageWrapper.putanja = file.name;
  //   //     imageWrapper.value = reader.result;
  //   //     this.selectedImages.push(imageWrapper);
  //   //   };
  //   //   // ovo ispod trigerije ovo iznad
  //   //   reader.readAsDataURL(file);   // rezultat na kraju je 64bitni enkodirana slika
  //   // }

  //   let file: File = event.target.files[0];
  //   const reader = new FileReader();

  //     // ucitavanje fajla preko readera, pregled fajla -> filter formata
  //     reader.onload = () => {

  //       console.log(file.name);
  //       console.log(reader);
  //       console.log(reader.result);
  //       this.articleData.file = reader.result;
        
  //     };
  //     // ovo ispod trigerije ovo iznad
  //     // https://developer.mozilla.org/en-US/docs/Web/API/FileReader
  //     // reader.readAsArrayBuffer(file);
  //     // reader.readAsBinaryString(file);
  //     // reader.readAsText(file);
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

  onSubmit(fieldValues, form){
    console.log(this.articleData);
    console.log(this.taskId);

    let valid = this.validate(fieldValues, form);
    if(!valid){
      return;
    }

    for(let fieldWrapper of this.formFieldsWrapper) {
      let value = fieldValues[fieldWrapper.field.id];
      let fieldMultiValues : any[]= [];

      let multiple: string = (fieldWrapper.field.properties['multiple']) ? 'multiple' : '';
      if(multiple === 'multiple'){
        let multi: Array<string> = value;
        value = multi.join(':');
      }

      let multiEnum: string = '';

      if( fieldWrapper.field.type.name == 'multiEnum'  || fieldWrapper.field.type.name=='multiEnumCoAuthor'
        || fieldWrapper.field.type.name == 'multiEnumTermOut'  || fieldWrapper.field.type.name=='multiEnumCoAuthorOut'){
        multiEnum = 'multiEnum'
        if(value === ''){
          fieldMultiValues = [];
        } else {
          fieldMultiValues = value;
        }
        if (fieldMultiValues.length > 0){
          value = value.join(':');
        }
        
      }

      this.articleData.fields.push({fieldId : fieldWrapper.field.id, 
                              fieldValue : value, 
                              multiple: multiple, 
                              multiEnum: multiEnum,
                              fieldMultiValues: fieldMultiValues});


    
    }


    this.articleData.fields.push({fieldId : 'article_co_authors_out', 
                      fieldValue : '', 
                      multiple: '', 
                      multiEnum: 'multiEnum',
                      fieldMultiValues: this.articleData.articleCoAuthors});

    this.articleData.fields.push({fieldId : 'article_key_terms_out', 
          fieldValue : '', 
          multiple: '', 
          multiEnum: 'multiEnum',
          fieldMultiValues: this.articleData.articleTerm});

    let x = this.articleService.postNewArticle(this.articleData, this.taskId);

    x.subscribe(res => {
      console.log(res);

      this.toastrService.success('New article published! You will be informed for the further instructions.');
      this.router.navigate(['home']);
    }, err => {

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
