const db = require('../utils/database.util');

module.exports = {
    get_followees_by_id: (userId) => db.query(`call followings_get_followees_by_id(${userId})`),
    get_followers_by_id: (userId) => db.query(`call followings_get_followers_by_id(${userId})`),
    add: (followerId,followeeeId) => db.query(`call followings_add("${followerId}","${followeeeId}")`),
    remove: (followerId,followeeeId) => db.query(`call followings_remove("${followerId}","${followeeeId}")`)
}