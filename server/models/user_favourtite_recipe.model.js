const db = require('../utils/database.util');

module.exports = {
    remove: (userId,recipeId) => db.query(`call user_favourite_recipes_remove("${userId}","${recipeId}")`),
    get_by_user_id: (userId) => db.query(`call user_favourite_recipes_get_by_user_id(${userId})`),
    add: (userId,recipeId) => db.query(`call user_favourite_recipes_add("${userId}","${recipeId}")`)
}