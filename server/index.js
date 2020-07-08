const express = require('express')
const app = express()
const morgan = require('morgan')

const enviromentName = "dev"
app.use(morgan(enviromentName));

const port = 3000
app.listen(port, () => {
    console.log(`A Node Js API is listening on port: ${port}`)
});

app.get('/', (request, response) => {
    response.send('Hello World!');
});