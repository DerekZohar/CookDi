const db = require('../utils/database.util');

module.exports = {
    get_friends_by_id: _ => db.query(`call friendships_get_by_id()`),
    add: (userId,friendId) => db.query(`call friendships_add("${userId}","${friendId}")`),
    remove: (userId,friendId) => db.query(`call friendships_remove("${userId}","${friendId}")`)
}