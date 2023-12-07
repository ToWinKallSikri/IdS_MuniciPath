const express = require('express');
const app = express();
const port = 3000;
app.listen(port, () => console.log("Test backend ascolta dalla porta " + port));
app.get('/api/v1/home', (req, res) => {
    comuni = [
        {
            name: "Camerino",
            CAP: "62032",
            lat: 43.133782867271215,
            lng: 13.069559141541596,
            posts: [
                {
                    title: "Piazza",
                    type: 1,
                    imgs: [],
                    text: "blablabl",
                    rate: 1,
                    lat: 43.13177794405671,
                    lng: 13.066230605920268
                },
                {
                    title: "Pincio",
                    imgs: [],
                    text: "blablabl",
                    rate: 1.1,
                    lat: 2,
                    lng: 3
                }
            ]
        },
        {
            name: "Civitanova",
            CAP: "62012",
            lat: 7,
            lng: 8,
            posts: [
                {
                    title: "Piazza",
                    imgs: [],
                    text: "blablabl",
                    rate: 1,
                    lat: 4,
                    lng: 5
                },
                {
                    title: "Pincio",
                    imgs: [],
                    text: "blablabl",
                    rate: 1.1,
                    lat: 5,
                    lng: 3
                }
            ]
        },
    ]
    res.send(comuni);
});

/*
export interface Comune{
    name : string;
    cap : string;
    lat : number;
    lng : number;
    posts : Post[];
}
    title: string;
    imgs: string[];
    text: string;
    rate : number;
    lat : number;
    lng : number;







*/