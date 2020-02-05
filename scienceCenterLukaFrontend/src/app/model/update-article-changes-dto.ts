import { OpinionAboutArticle } from './opinion-about-article';
import { NewArticle } from './new-article';

export class UpdateArticleChanges {

     newArticleRequestDto;   
     newAarticleResponseDto: NewArticle;
                                                 
     reviewersOpinion: OpinionAboutArticle[];  
     editorsOpinion: OpinionAboutArticle[];    
                                                 
    authorsMessage: OpinionAboutArticle;    
    
    fields: any[] = [];
    fieldResults: any[] = [];

    constructor() {}
}