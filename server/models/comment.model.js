const db = require('../utils/database.util');

module.exports = {
    comments_get_by_recipe_id: recipeId => db.query(`call comments_get_by_recipe_id("${recipeId}")`),
    add: (userId,recipeId,commentContent,commentTime) => db.query(`call comments_add("${userId}","${recipeId}","${commentContent}","${commentTime}")`),
    remove: (userId,recipeId) => db.query(`call comments_remove("${userId}","${recipeId}")`)
}