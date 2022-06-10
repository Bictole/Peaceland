const express = require('express');
const app = express();

app.use(express.json());
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
  });

const alert = {message: "No alert"}

app.get("/alert", (req, res) => {
    res.send(alert)
})

app.post("/alert", (req, res) => {
    alert = req.body
    res.send(alert)
})

app.listen(4000)