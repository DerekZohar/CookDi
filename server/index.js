const express = require('express')
const app = express()
const morgan = require('morgan')
const bodyParser = require('body-parser');

const enviromentName = "dev"
app.use(morgan(enviromentName));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
const port = 3000

const userRoutes = require('./routes/user.route.js');

// app.use('/', require('./routes/index.route.js'));
app.use('/user', require('./routes/user.route'));
app.use('/recipe', require('./routes/recipe.route'));

var server=app.listen(port, () => {
    console.log(`A Node Js API is listening on port: ${port}`)
});

const ioSocket= require("./chat/iosocketchat.js");

ioSocket.IOSocketServer(server);

module.exports = app;