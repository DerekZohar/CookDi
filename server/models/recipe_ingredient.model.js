const db = require('../utils/database.util');

module.exports = {
    add: (recipeId,ingredientId) => db.query(`call recipe_ingredients_add("${recipeId}","${ingredientId}")`),
    remove: (recipeId,ingredientId) => db.query(`call recipe_ingredients_remove_by_id("${recipeId}","${ingredientId}")`),
    get_recipe_ingredients_by_recipe_id: (recipeId) => db.query(`call recipe_ingredients_get_by_recipe_id("${recipeId}")`)
}