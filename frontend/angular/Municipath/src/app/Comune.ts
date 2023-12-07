import { Post } from './Post';

export interface Comune{
    name : string;
    cap : string;
    lat : number;
    lng : number;
    posts : Post[];
}