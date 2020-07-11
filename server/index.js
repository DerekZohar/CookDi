const express = require('express')
const app = express()
const morgan = require('morgan')

const enviromentName = "dev"
app.use(morgan(enviromentName));

const port = 3000

const userRoutes = require('./routes/user.route.js');

// app.use('/', require('./routes/index.route.js'));
app.use('/user', require('./routes/user.route'));

app.listen(port, () => {
    console.log(`A Node Js API is listening on port: ${port}`)
});

module.exports = app;