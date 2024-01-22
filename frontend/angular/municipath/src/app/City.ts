import { Position } from "./Position";

export interface City{
    id : string;
	name : string;
    curator : string;
    cap : number;
    pos : Position;
}