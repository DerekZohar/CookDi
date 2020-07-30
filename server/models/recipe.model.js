const db = require('../utils/database.util');

module.exports = {
    all: _ => db.query(`call recipes_get_all()`),
    add: (recipeName,userId,createdAt,description) => db.query(`call recipes_add("${recipeName}","${userId}","${createdAt}","${description}")`),
    remove: (recipeId,userId) => db.query(`call recipes_remove_by_id("${recipeId}","${userId}")`),
    get_recipe_by_recipe_id: (userId,recipeId) => db.query(`call recipes_get_by_recipe_id("${userId}","${recipeId}")`),
    get_recipe_by_user_id: (userId)  => db.query(`call recipse_get_by_user_id("${userId}")`)
}