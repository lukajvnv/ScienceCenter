import { Component, OnInit } from '@angular/core';
import { TestService } from 'src/app/service/test.service';
import { UserService } from 'src/app/service/user/user.service';
import { FormSignUpSubmission } from 'src/app/model/form-sign-up-submission';
import { ToastrService } from 'ngx-toastr';
import { ErrorObject } from 'src/app/model/error-object';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private repeated_password = "";
  private categories = [];
  private formFieldsDto = null;
  private formFields = [];
  private choosen_category = -1;
  private processInstance = "";
  private enumValues = [];
  private tasks = [];

  constructor(private userService: UserService, private toastrService: ToastrService) { }

  ngOnInit() {
    let x = this.userService.startRegister();

    x.subscribe(
      res => {
        console.log(res);
        //this.categories = res;
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum'){
            this.enumValues = Object.keys(field.type.values);
          }
        });
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(value, form){
    let controls = form.controls;
    for (var field in controls){
      let t = controls[field];
      if ( t.status == 'INVALID'){
        this.toastrService.error(field + ' is required!');
        return;
      }
    }

    let o = new Array();
    let dto = new FormSignUpSubmission();
    for (var property in value) {
      console.log(property);
      console.log(value[property]);
      if (property == "science_area") {
        dto.scienceAreaId = value[property];
        continue;
      }
      o.push({fieldId : property, fieldValue : value[property]});
    }

    dto.formFields = o;

    console.log(o);
    let x = this.userService.registerUser(dto, this.formFieldsDto.taskId);

    x.subscribe(
      res => {
        console.log(res);
        
        this.toastrService.success("You registered successfully!")
      },
      (err : any) => {
        console.log("Error occured");
        this.toastrService.error('Code:'+ err.error.status + ', message:' + err.error.message);
      }
    );
  }

}
