const db = require('../utils/database.util');

module.exports = {
    all: _ => db.query(`call users_get_all()`),
    add: (userName,userEmail,userPassword,userGender,userAge, avatarUrl) => db.query(`call users_add("${userName}","${userEmail}","${userPassword}","${userGender}","${userAge}","${avatarUrl}")`),
    get_user_by_id: (userId) => db.query(`call users_get_by_id(${userId})`),
    //update: (field,value) => db.query(`call user_update_field("${field}","${value}")`),
    remove: (userId) => db.query(`call users_remove_by_id("${userId}")`),
    get_user_by_username: (username) => db.query(`call users_get_by_username("${username}")`),
    get_user_pw_by_username: (username) => db.query(`call users_get_pw_by_username("${username}")`)
}