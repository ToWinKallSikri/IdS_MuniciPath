import { Position } from "./Position";
    
export interface Point {
    id : string;
	pos : Position;
	cityId : string;
	posts : string[];
	idCount : number;
}