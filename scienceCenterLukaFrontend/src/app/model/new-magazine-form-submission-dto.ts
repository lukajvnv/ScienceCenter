export class NewMagazineFormSubmissionDto{

     taskId: string;
     processInstanceId: string;
      // name;
      // issn_number;
      // payment_option;
      // membership_price;
      // science_area_name: string[];

      formFields: any[] = [];
      commentIfExist: string;

    constructor() {}
}