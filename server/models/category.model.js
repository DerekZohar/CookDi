const db = require('../utils/database.util');

module.exports = {
    all: _ => db.query(`call categories_get_all()`),
    add: (categoryName) => db.query(`call categories_add("${categoryName}")`),
    remove: (categoryId) => db.query(`call categories_remove("${categoryId}")`)
}