const db = require('../utils/database.util');

module.exports = {
    all: _ => db.query(`call users_get_all()`),
    add: (userName,userEmail,userPassword,userGender,userAge) => db.query(`call users_add("${userName}","${userEmail}","${userPassword}","${userGender}","${userAge}")`),
    get_user_by_id: (userId) => db.query(`call users_get_by_id(${userId})`),
    //update: (field,value) => db.query(`call user_update_field("${field}","${value}")`),
    remove: (userId) => db.query(`call users_remove_by_id("${userId}")`)
}