const express = require('express');
const app = express();

app.use(express.json());
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
  });

let alert = ""
let lastalert = false

app.get("/alert", (req, res) => {
    if (!lastalert || alert === "") {
        lastalert = true
        res.send(alert)
    }
    res.end()
})

app.post("/alert", (req, res) => {
    alert = req.body
    lastalert = false
    res.send(alert)
})

app.listen(4000)