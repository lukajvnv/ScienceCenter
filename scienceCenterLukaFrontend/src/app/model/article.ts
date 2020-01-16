import { ScienceArea } from './science-area';
import { User } from './user';
import { Term } from './term';

export class Article {
    taskId: string;
	processInstanceId: string;
	
	 articleId: number;
	 articleTitle: string;
	 articleAbstract: string;
	
	 scienceArea: ScienceArea;
	 publishingDate: string;
	
	 author: User;
	 coAuthors: User[];
	 keyTerms: Term[];
	
	 price: number;
	
	 file: string | ArrayBuffer;

	 constructor(){}
}