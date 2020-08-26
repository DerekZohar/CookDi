const db = require('../utils/database.util');

module.exports = {
    all: _ => db.query(`call ingredients_get_all()`),
    add: (ingredientName) => db.query(`call ingredients_add("${friendId}")`),
    remove: (ingredientId) => db.query(`call ingredients_remove("${ingredientId}")`)
}