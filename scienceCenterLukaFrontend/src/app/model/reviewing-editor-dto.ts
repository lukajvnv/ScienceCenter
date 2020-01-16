import { Article } from './article';

import { Magazine } from './magazine';

import { OpinionAboutArticle } from './opinion-about-article';

export class ReviewingEditorDto{

    article: Article;
    magazine: Magazine;
    opinions: OpinionAboutArticle[];

    editorOpinion: OpinionAboutArticle;

   constructor(){}
}