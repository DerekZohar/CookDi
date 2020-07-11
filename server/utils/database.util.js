const mysql = require('mysql');
const util = require('util');

const pool = mysql.createPool({
    // connectionLimit: 100,
    // host: 'bh1qqiz4n9xynvm2tzc8-mysql.services.clever-cloud.com',
    // port: 3306,
    // user: 'utgf1so6lfjyclgm',
    // password: '2XuYsIrCQWUOsQ4zt43B',
    // database: 'bh1qqiz4n9xynvm2tzc8',
    // insecureAuth: true
    connectionLimit: 100,
    host: 'us-cdbr-east-02.cleardb.com',
    port: 3306,
    user: 'bf429f27095475',
    password: '56349a0e',
    database: 'heroku_6dd61aff90cf7bc',
    insecureAuth: true
});

const pool_query = util.promisify(pool.query).bind(pool);

module.exports = {
    // query: async(sql) => pool_query(sql).then(value => {console.log(value); return value[0]}),
    query: (sql) => {return pool_query(sql);}
};