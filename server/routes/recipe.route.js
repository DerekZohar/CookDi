var express = require('express');
var router = express.Router();
var recipeModel = require('../models/recipe.model.js');
var userModel = require('../models/user.model.js');
const { json } = require('express');
const bcrypt = require("bcryptjs");

router.get('/all', async function(req, res) {
    const rows = await recipeModel.all();
    var data = new Array();

    for(i =0;i<rows[0].length ; ++i) {
        var object = {"recipe" : Object(), "chef": Object()};
        object.recipe = rows[0][i];
        let user = await userModel.get_user_by_id(rows[0][i].user_id);
        object.chef = user[0][0];
        

        let test = JSON.parse(rows[0][i]);
        test["chef"] = user;
        data.push(test);
    }
    
    if(rows[0].length <= 0 ) {
        return res.status(403).json(
            {
                "message": "User is not exist"
            }
        );
    } 

    return res.json(data);
});



module.exports = router;