import { Position } from "./Position";
import { Post } from "./Post";
    
export interface Point {
    pointId : string;
	pos : Position;
	cityId : string;
	posts : Post[];
	idCount : number;
}