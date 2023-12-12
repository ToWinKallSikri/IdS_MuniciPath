import { Post } from './Post';

export interface Comune{
    id : number;
    name : string;
    cap : string;
    lat : number;
    lng : number;
    posts : Post[];
}