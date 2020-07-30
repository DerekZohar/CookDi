const db = require('./utils/database.util');

(async () =>  console.log(await db.query('select * from users')))();