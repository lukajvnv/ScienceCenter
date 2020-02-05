import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user/user.service';
import { ToastrService } from 'ngx-toastr';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { NewMagazineFormSubmissionDto } from 'src/app/model/new-magazine-form-submission-dto';
import { NewMagazineFormReviewerEditor } from 'src/app/model/new-magazine-form-reviewer-editor';
import { NewMagazineFormReviewerEditorRow } from 'src/app/model/new-magazine-form-reviewer-editor-row';
import { NewMagazineEditorReviewerRequest } from 'src/app/model/new-magazine-form-reviewer-editor-response';
import { ScienceArea } from 'src/app/model/science-area';
import { Router } from '@angular/router';
import { FormFieldDtoWrapper } from 'src/app/model/form-field-dto';

@Component({
  selector: 'app-new-magazine',
  templateUrl: './new-magazine.component.html',
  styleUrls: ['./new-magazine.component.css']
})
export class NewMagazineComponent implements OnInit {

  private repeated_password = "";
  private categories = [];
  private formFieldsDto : NewMagazineFormSubmissionDto = null;
  private formFieldsWrapper: FormFieldDtoWrapper[] = [];
  private formFieldsEditor: NewMagazineEditorReviewerRequest = null;
  private flag1 = true;
  private flag2 = false;
  private formFields = [];
  private choosen_category = -1;
  private processInstance = "";
  private enumValues = [];
  private tasks = [];

  private paymentOptionsValues;
  private scAreaValues;

  private formData: NewMagazineFormSubmissionDto = new NewMagazineFormSubmissionDto();
  private formDataEditorsReviewers: NewMagazineFormReviewerEditor = new NewMagazineFormReviewerEditor();


  constructor(private magazineService: MagazineService, private toastrService: ToastrService, private router: Router) { }

  ngOnInit() {
    let x = this.magazineService.startNewMagazine();

    x.subscribe(
      res => {
        console.log(res);
        //this.categories = res;
        this.formFieldsDto = res;
        // this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;

       

        this.formFieldsDto.formFields.forEach( (field) =>{
          let newFieldWrapper = new FormFieldDtoWrapper(field);
          if( field.type.name=='enum'){
            newFieldWrapper.dataSource = Object.keys(field.type.values);
          }

          if( field.type.name=='multiEnum'){
            newFieldWrapper.dataSource = Object.keys(field.type.values);
          }
          this.formFieldsWrapper.push(newFieldWrapper);
        });
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(fieldValues, form){
    console.log(this.formData);
    // let controls = form.controls;
    // for (var field in controls){
    //   let t = controls[field];
    //   if ( t.status == 'INVALID'){
    //     this.toastrService.error(field + ' is required!');
    //     return;
    //   }
    // }

    let valid = this.validate(fieldValues, form);
    if(!valid){
      return;
    }

    for(let fieldWrapper of this.formFieldsWrapper) {
      let value = fieldValues[fieldWrapper.field.id];

      let multiple: string = (fieldWrapper.field.properties['multiple']) ? 'multiple' : '';
      if(multiple === 'multiple'){
        let multi: Array<string> = value;
        value = multi.join(':');
      }
      this.formData.formFields.push({fieldId : fieldWrapper.field.id, fieldValue : value, multiple: multiple});


    
    }

    
    let x = this.magazineService.postMagazineBasicData(this.formData, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        console.log(res);
        this.formFieldsEditor = res;
        this.flag1 = false;
        
        let rows: NewMagazineFormReviewerEditorRow[] = [];

        const scAreas: ScienceArea[] = this.formFieldsEditor.scienceAreas;
        for (let scArea of scAreas) {
          rows.push(new NewMagazineFormReviewerEditorRow(scArea.scienceAreaId));
        }

        this.formDataEditorsReviewers.rows = rows;
        this.flag2 = true;

      },
      err => {
        console.log("Error occured");
        this.toastrService.error(err.error);
      }
    );

  
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

  submitReviewersEditors(){
    let scAreasId: string[] = [];
    this.formDataEditorsReviewers.rows.forEach(r => {
      scAreasId.push(r.editorsId);
    });
    if(this.hasDuplicates(scAreasId)){
      this.toastrService.error("Editors for science areas should be uniques");
      return;
    }

    let x = this.magazineService.postMagazineEditorsReviewersData(this.formDataEditorsReviewers, this.formFieldsEditor.taskId);

    x.subscribe(
      res => {
        this.toastrService.success('New magazine is sent to the admin for verification!');
        this.router.navigate(['home']);

      },
      err => {
        console.log("Error occured");
        this.toastrService.error(err.error);
      }
    );

  
  }

  hasDuplicates(array): boolean {
    return (new Set(array)).size !== array.length;
  }

}
