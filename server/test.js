const db = require('./utils/database.util');
const us = require('./models/user.model');

//(async () =>  console.log(await us.add("Tran Thuan Thanh","123","123",0,12)))();

(async () =>  console.log(await us.remove(21)))();
(async () =>  console.log(await us.remove(22)))();
(async () =>  console.log(await us.remove(23)))();
