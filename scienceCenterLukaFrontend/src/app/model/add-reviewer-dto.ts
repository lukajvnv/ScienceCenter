import { EditorReviewer } from './editor-reviewer-dto';
import { Magazine } from './magazine';
import { Article } from './article';

export class AddReviewerDto {

     articleDto: Article;                                   
     magazineDto: Magazine;                                 
     editorsReviewersDto: EditorReviewer[];
                                                         
     subProcessMfExecutionId: string;
     insideMf: boolean;                          

    constructor() {}
}