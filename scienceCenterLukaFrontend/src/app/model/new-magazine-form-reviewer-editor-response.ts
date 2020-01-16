import { ScienceArea } from './science-area';
import { ReviewerEditor } from './reviewer-editor';

export class NewMagazineEditorReviewerRequest {

     scienceAreas: ScienceArea[];
	 reviewers: ReviewerEditor[];
	 editors: ReviewerEditor[];
	
	 taskId: string;
	 processId: string;

    constructor(){}
}