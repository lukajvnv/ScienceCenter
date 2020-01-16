import { ScienceArea } from './science-area';

export class Magazine {
    
    magazineId: number;
	issn: string;
	name: string;
	
	scienceAreas: ScienceArea[];
    
    constructor(){}
}