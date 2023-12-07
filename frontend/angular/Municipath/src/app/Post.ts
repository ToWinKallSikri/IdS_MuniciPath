export interface Post{
    title: string;
    type: number,
    start: Date,
    end: Date,
    save: boolean,
    imgs: string[];
    text: string;
    rate : number;
    lat : number;
    lng : number;
}