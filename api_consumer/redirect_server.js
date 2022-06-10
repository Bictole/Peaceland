const express = require('express');
const app = express();

app.use(express.json());
app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
  });

let queue_alert = []

app.get("/alert", (req, res) => {
    if (queue_alert.length !== 0) {
        res.send(queue_alert.pop())
    }
    res.end()
})

app.post("/alert", (req, res) => {
    queue_alert.push(req.body)
    res.end()
})

app.listen(4000)