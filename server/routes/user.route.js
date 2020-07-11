var express = require('express');
var router = express.Router();
var userModel = require('../models/user.model.js');
const { json } = require('express');

router.get('/', async function(req, res) {
    var userId = req.query.id;
    var rows = await userModel.get_user_by_id(userId);
    var user;
    if(rows.length > 0 ) {
        user = rows[0][0];
    } else {
        return res.json(
            {
                "message": "User is not exist"
            }
        );
    }
    console.log(user.user_name);
    return res.json(
        {
            "id": user.user_id,
            "name": user.user_name,
            "email": user.user_email,
            "gender": user.user_gender,
            "age": user.user_age

        }
    );
});

module.exports = router;