import { User } from './user';
import { ScienceArea } from './science-area';
import { Magazine } from './magazine';

export class EditorReviewer {

    editorByScArId: number;
	
	editor: boolean;
	
	editorReviewer: User;
	
    scienceArea: ScienceArea;
	
	magazine: Magazine;

    constructor(){}
}