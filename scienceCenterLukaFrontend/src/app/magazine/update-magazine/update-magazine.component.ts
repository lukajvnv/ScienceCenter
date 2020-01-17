import { Component, OnInit } from '@angular/core';
import { NewMagazineEditorReviewerRequest } from 'src/app/model/new-magazine-form-reviewer-editor-response';
import { NewMagazineFormSubmissionDto } from 'src/app/model/new-magazine-form-submission-dto';
import { NewMagazineFormReviewerEditor } from 'src/app/model/new-magazine-form-reviewer-editor';
import { ToastrService } from 'ngx-toastr';
import { MagazineService } from 'src/app/service/magazine/magazine.service';
import { NewMagazineFormReviewerEditorRow } from 'src/app/model/new-magazine-form-reviewer-editor-row';
import { ScienceArea } from 'src/app/model/science-area';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-magazine',
  templateUrl: './update-magazine.component.html',
  styleUrls: ['./update-magazine.component.css']
})
export class UpdateMagazineComponent implements OnInit {

  
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


  constructor(private magazineService: MagazineService, private toastrService: ToastrService, private activatedRoute: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(data => {
      const taskId = data.get("taskId");
      // if(taskId){

      // }
      let x = this.magazineService.correctNewMagazine(taskId);

      x.subscribe(
        res => {
          console.log(res);
          this.formFieldsDto = res;
          // this.processInstance = res.processInstanceId;
          this.formData.name = this.formFieldsDto.name.value.value;
          this.formData.issn_number = this.formFieldsDto.issn_number.value.value;
          this.formData.membership_price = this.formFieldsDto.membership_price.value.value;
          this.formData.payment_option = this.formFieldsDto.payment_option.value.value;
          this.formData.taskId = this.formFieldsDto.taskId;
          this.formData.processInstanceId = this.formFieldsDto.processInstanceId;
          // this.formData.science_area_name

          let payment = this.formFieldsDto.payment_option;
          this.paymentOptionsValues = Object.keys(payment.type.values)

          let scArea = this.formFieldsDto.science_area;
          this.scAreaValues = Object.keys(scArea.type.values)

        
        },
        err => {
          console.log("Error occured");
        }
      );
    });

    
  }

  onSubmit(value, form){
    console.log(this.formData);
  
    let x = this.magazineService.correctMagazineBasicData(this.formData, this.formFieldsDto.taskId);

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
        this.toastrService.success('New magazine is sent to the admin for verification!');
        this.router.navigate(['home']);

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
