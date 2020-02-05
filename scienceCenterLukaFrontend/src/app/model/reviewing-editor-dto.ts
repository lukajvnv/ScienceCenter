import { Article } from './article';

import { Magazine } from './magazine';

import { OpinionAboutArticle } from './opinion-about-article';

export class ReviewingEditorDto{

    article: Article;
    magazine: Magazine;
    opinions: OpinionAboutArticle[];
    authorsMessages: OpinionAboutArticle[];

    editorOpinion: OpinionAboutArticle;

    fields: any[];
    fieldResults: any[];

   constructor(){}
}