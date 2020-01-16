import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user/user.service';
import { ToastrService } from 'ngx-toastr';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { NewMagazineFormSubmissionDto } from 'src/app/model/new-magazine-form-submission-dto';
import { NewMagazineFormReviewerEditor } from 'src/app/model/new-magazine-form-reviewer-editor';
import { NewMagazineFormReviewerEditorRow } from 'src/app/model/new-magazine-form-reviewer-editor-row';
import { NewMagazineEditorReviewerRequest } from 'src/app/model/new-magazine-form-reviewer-editor-response';
import { ScienceArea } from 'src/app/model/science-area';

@Component({
  selector: 'app-new-magazine',
  templateUrl: './new-magazine.component.html',
  styleUrls: ['./new-magazine.component.css']
})
export class NewMagazineComponent implements OnInit {

  private repeated_password = "";
  private categories = [];
  private formFieldsDto = null;
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


  constructor(private magazineService: MagazineService, private toastrService: ToastrService) { }

  ngOnInit() {
    let x = this.magazineService.startNewMagazine();

    x.subscribe(
      res => {
        console.log(res);
        //this.categories = res;
        this.formFieldsDto = res;
        // this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;

        let payment = this.formFieldsDto.payment_option;
        this.paymentOptionsValues = Object.keys(payment.type.values)

        let scArea = this.formFieldsDto.science_area;
        this.scAreaValues = Object.keys(scArea.type.values)

        // this.formFields.forEach( (field) =>{
          
        //   if( field.type.name=='enum'){
        //     this.enumValues = Object.keys(field.type.values);
        //   }
        // });
      },
      err => {
        console.log("Error occured");
      }
    );
  }

  onSubmit(value, form){
    console.log(this.formData);
    // let controls = form.controls;
    // for (var field in controls){
    //   let t = controls[field];
    //   if ( t.status == 'INVALID'){
    //     this.toastrService.error(field + ' is required!');
    //     return;
    //   }
    // }

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
      }
    );

  
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
        alert('Resi');

      },
      err => {
        console.log("Error occured");
      }
    );

  
  }

  hasDuplicates(array): boolean {
    return (new Set(array)).size !== array.length;
  }

}
