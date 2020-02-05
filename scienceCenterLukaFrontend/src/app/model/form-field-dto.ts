export class FormFieldDtoWrapper {
    field;
    dataSource: any[] = [];
    errorField: string = '';

    // constructor(field, datasource){
    //     this.field = field;
    //     this.dataSource = datasource;
    // }

    constructor(field){
        this.field = field;
        
    }
}