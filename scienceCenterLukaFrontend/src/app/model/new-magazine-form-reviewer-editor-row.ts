export class NewMagazineFormReviewerEditorRow{

    scAreaId: number;
    reviewersId: string[] = [];
    editorsId: string;

    constructor(scAreaId: number){
        this.scAreaId = scAreaId;
    }
}