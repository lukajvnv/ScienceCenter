import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user/user.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { FormSignUpSubmission } from 'src/app/model/form-sign-up-submission';
import { FormFieldDtoWrapper } from 'src/app/model/form-field-dto';
import { NewEditor } from 'src/app/model/new-editor-dto';
import { EditorGuard } from 'src/app/path-guards/editor.guard';

@Component({
  selector: 'app-new-editor',
  templateUrl: './new-editor.component.html',
  styleUrls: ['./new-editor.component.css']
})
export class NewEditorComponent implements OnInit {

  private formFieldsWrapper: FormFieldDtoWrapper[] = [];
  private formData: NewEditor = new NewEditor();


  constructor(private userService: UserService, private toastrService: ToastrService, private router: Router) { }

  ngOnInit() {
    let x = this.userService.startRegister();

    x.subscribe(
      res => {
        
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(value, form){
    let x = this.userService.registerEditor(this.formData);

    x.subscribe(
      res => {
        console.log(res);
        
        this.toastrService.success("New editor is in the system");
        this.router.navigate(['home']);
      },
      (err : any) => {
        console.log("Error occured");
        this.toastrService.error('Code:'+ err.error.status + ', message:' + err.error.message);
      }
    );
  }

}
