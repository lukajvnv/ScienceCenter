import { Article } from './article';
import { Magazine } from './magazine';
import { OpinionAboutArticle } from './opinion-about-article';

export class ReviewingDto{

     article: Article;
	 magazine: Magazine;
     opinion: OpinionAboutArticle;
     
     editorOpinion: OpinionAboutArticle;

     insideMf: boolean;

    constructor(){}
}