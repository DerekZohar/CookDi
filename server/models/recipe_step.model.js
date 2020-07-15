const db = require('../utils/database.util');

module.exports = {
    //add: (recipeId,ingredientId) => db.query(`call recipe_ingredients_add("${recipeId}","${ingredientId}")`),
    remove: (recipeId,stepId) => db.query(`call recipe_steps_remove_by_id("${recipeId}","${stepId}")`),
    get_recipe_ingredients_by_recipe_id: (recipeId) => db.query(`call recipe_steps_get_by_recipe_id("${recipeId}")`)
}