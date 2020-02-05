import { Component, OnInit } from '@angular/core';
import { EditorReviewer } from 'src/app/model/editor-reviewer-dto';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/service/review/review.service';
import { Article } from 'src/app/model/article';
import { Magazine } from 'src/app/model/magazine';
import { AddReviewerDto } from 'src/app/model/add-reviewer-dto';
import { FormFieldDtoWrapper } from 'src/app/model/form-field-dto';

@Component({
  selector: 'app-add-reviewer-when-error',
  templateUrl: './add-reviewer-when-error.component.html',
  styleUrls: ['./add-reviewer-when-error.component.css']
})
export class AddReviewerWhenErrorComponent implements OnInit {

  private taskId: string;
  private processInstanceId: string;

  

  private scFilter: boolean = false;
  private geoFilter: boolean = false;

  private possibleReviewers: EditorReviewer[];
  private filteredReviewers: EditorReviewer[] = [];
  private selectedReviewers: EditorReviewer[] = [];

  private reviewerDto: AddReviewerDto;

  private formFieldsWrapper: FormFieldDtoWrapper[] = [];


  constructor(private activatedRoute: ActivatedRoute, private reviewService: ReviewService, private router: Router,
    private toastr: ToastrService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;
      
      let x = this.reviewService.startAddingReviewerWhenError(taskId);

      x.subscribe(
        res => {
          console.log(res);

          this.reviewerDto = res;
         
          this.possibleReviewers = res.editorsReviewersDto;
          this.filteredReviewers = res.editorsReviewersDto;

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

  add(rev: EditorReviewer){
    this.selectedReviewers.push(rev);
  }

  delete(rev: EditorReviewer){
    let index = this.selectedReviewers.indexOf(rev);
    this.selectedReviewers.splice(index, 1);
  }

  addReviewer(fieldValues, form){
    // if(this.selectedReviewers.length > 1){
    //   this.toastr.error('Please select only one');
    //   return;
    // }

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
      fieldResults.push({fieldId : fieldWrapper.field.id, fieldValue : value, multiple: multiple});

    }

    this.reviewerDto.fieldValues = fieldResults;
    this.reviewerDto.fields = [];
    this.reviewerDto.editorsReviewersDto = this.selectedReviewers;

    // let x = this.reviewService.postAddingReviewerWhenError(this.taskId, this.reviewerDto);
    let x;

    if(this.reviewerDto.insideMf){
       x = this.reviewService.postAddingReviewerWhenError(this.taskId, this.reviewerDto);
    }else{
       x = this.reviewService.postAddingReviewerAdditional(this.taskId, this.reviewerDto);
    }

    x.subscribe(res => {
      console.log('resi');
      this.toastr.success('Operations of adding reviewer is completed.');
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
            if(value.length < 1){
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

  // filter(){
  //   if(this.scFilter){
  //     this.filteredReviewers = this.filteredReviewers.filter(r => r.scienceArea.scienceAreaId === this.reviewerDto.articleDto.scienceArea.scienceAreaId);
  //   }

  //   if(!this.scFilter && !this.geoFilter){
  //     this.filteredReviewers = this.possibleReviewers;
  //   }
  // }

  // getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
  //   var R = 6371; // Radius of the earth in km
  //   var dLat = this.deg2rad(lat2-lat1);  // deg2rad below
  //   var dLon = this.deg2rad(lon2-lon1); 
  //   var a = 
  //     Math.sin(dLat/2) * Math.sin(dLat/2) +
  //     Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * 
  //     Math.sin(dLon/2) * Math.sin(dLon/2)
  //     ; 
  //   var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
  //   var d = R * c; // Distance in km
  //   return d;
  // }
  
  //  deg2rad(deg) {
  //   return deg * (Math.PI/180)
  // }

}
