import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/service/review/review.service';
import { ToastrService } from 'ngx-toastr';
import { FormFieldDtoWrapper } from 'src/app/model/form-field-dto';

@Component({
  selector: 'app-define-time-for-reviewing',
  templateUrl: './define-time-for-reviewing.component.html',
  styleUrls: ['./define-time-for-reviewing.component.css']
})
export class DefineTimeForReviewingComponent implements OnInit {

  private taskId: string;

  private days: number;
  private hours: number;
  private minutes: number;

  private formFieldsWrapper: FormFieldDtoWrapper[] = [];


  constructor(private activatedRoute: ActivatedRoute, private reviewService: ReviewService, private router: Router,
    private toastr: ToastrService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;

      let x = this.reviewService.defineTimeForReviewStart(this.taskId);

      x.subscribe(
        res => {
          console.log(res);

          res.forEach( (field) =>{
            let newFieldWrapper = new FormFieldDtoWrapper(field);
            if( field.type.name=='enum'){
              newFieldWrapper.dataSource = Object.keys(field.type.values);
            }
            newFieldWrapper.field.value.value = 0;
            this.formFieldsWrapper.push(newFieldWrapper);
          });
          
        },
        err => {
          console.log("Error occured");
        }
      );
    });
  }

  postResult(fieldValues, form){
    // if(!this.days && !this.minutes && !this.hours){
    //   this.toastr.error("At least one field you have to fill in!");
    //   return;
    // }

    // if(this.days <= 0 || this.minutes <= 0 || this.hours <= 0){
    //   this.toastr.error("Invalid input!");
    //   return;
    // }

    // if(!this.days) this.days = 0;
    // if(!this.hours) this.hours = 0;
    // if(!this.minutes) this.minutes = 0;

    // let template = 'PxDTyHzM';
    // template = template.replace('x', this.days.toString());
    // template = template.replace('y', this.hours.toString());
    // template = template.replace('z', this.minutes.toString());

    // const time: string = 'P' + 'D' + 'H' + 'M';

    //let x = this.reviewService.defineTimeForReview(this.taskId, template);

    // let fieldsResultss: any[] = [];
    // for(let fieldWrapper of this.formFieldsWrapper) {
    //   let value = fieldValues[fieldWrapper.field.id];

    //   let multiple: string = (fieldWrapper.field.properties['multiple']) ? 'multiple' : '';
    //   if(multiple === 'multiple'){
    //     let multi: Array<string> = value;
    //     value = multi.join(':');
    //   }
    //   fieldsResults.push({fieldId : fieldWrapper.field.id, fieldValue : value, multiple: multiple});
    // }


    let valid = this.validate(fieldValues, form);
    if(!valid){
      return;
    }

    let fieldsResults: any[] = [];
    for(let fieldWrapper of this.formFieldsWrapper) {
      let value = fieldValues[fieldWrapper.field.id];

      let multiple: string = (fieldWrapper.field.properties['multiple']) ? 'multiple' : '';
      if(multiple === 'multiple'){
        let multi: Array<string> = value;
        value = multi.join(':');
      }
      fieldsResults.push({fieldId : fieldWrapper.field.id, fieldValue : value, multiple: multiple});
    }


    let x = this.reviewService.defineTimeForReviewPost(this.taskId, fieldsResults);


    x.subscribe(res => {
      this.toastr.success('Operations of define time for reviewing is completed.');
      this.router.navigate(['home']);
    }, err => {
        console.log("Error occured");
        this.toastr.error(err.error);
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
