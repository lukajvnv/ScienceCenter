import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Magazine } from 'src/app/model/magazine';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { CheckingMagazineDto } from 'src/app/model/checking-magazine-dto';
import { ToastrService } from 'ngx-toastr';
import { FormSignUpSubmission } from 'src/app/model/form-sign-up-submission';
import { FormFieldDtoWrapper } from 'src/app/model/form-field-dto';

@Component({
  selector: 'app-check-new-magazine-data',
  templateUrl: './check-new-magazine-data.component.html',
  styleUrls: ['./check-new-magazine-data.component.css']
})
export class CheckNewMagazineDataComponent implements OnInit {

  private checkingMagazine: CheckingMagazineDto;
  private taskId: string;

  private formFieldsWrapper: FormFieldDtoWrapper[] = [];


  constructor(private activatedRoute: ActivatedRoute, private magazineService: MagazineService, private router: Router,
    private toastrService: ToastrService) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      this.taskId = taskId;

      let x = this.magazineService.checkingMagazineDataStart(taskId);

      x.subscribe(
        res => {
          console.log(res);
          this.checkingMagazine = res;

          this.checkingMagazine.fields.forEach( (field) =>{
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

  submit(fieldValues, form){
    this.checkingMagazine.fieldsResponse = [];
  
    for(let fieldWrapper of this.formFieldsWrapper) {
      let value = fieldValues[fieldWrapper.field.id];

      let mult: string = (fieldWrapper.field.properties['multiple']) ? 'multiple' : '';
      if(mult === 'multiple'){
        let multi: Array<string> = value;
        value = multi.join(':');
      }

      if( fieldWrapper.field.type.name=='boolean' && value === ''){
        value = false;
      }

      this.checkingMagazine.fieldsResponse.push({fieldId : fieldWrapper.field.id, fieldValue : value, multiple: mult});


    
    }

    this.checkingMagazine.fields = [];
    let x = this.magazineService.checkingMagazineDataPost(this.checkingMagazine, this.taskId);

    x.subscribe(
      res => {
        console.log("Rijesi");   
        this.toastrService.success("Operation completed");
        this.router.navigate(['home']);      

      },
      err => {
        console.log("Error occured");
      }
    );

  
  }

}
