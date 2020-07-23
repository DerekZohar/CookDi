const db = require('../utils/database.util');

module.exports = {
    add: (userId,recipeId) => db.query(`call likes_add("${userId}","${recipeId}")`),
    get_by_recipe_id: (recipeId) => db.query(`call likes_get_by_recipe_id("${recipeId}")`),
    remove: (userId,recipeId) => db.query(`call likes_remove("${userId}","${recipeId}")`)
}