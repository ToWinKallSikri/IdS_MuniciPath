
import { Score} from "./Score";

export interface Post {    
    id : string;
    pointId : string;
    cityId : string;
    title : string;
    type : string;
    author : string;
    pos : string;
    text : string;
    multimediaData : string[];
    groups : string[];
    published : boolean;
    meteo : string;
    startTime : Date;
    endTime : Date;
    publicationTime : Date;
    persistence : boolean;
    ofCity : boolean;
    viewsCount : number;
    vote : Score;
}