var express = require('express');
var router = express.Router();
var userModel = require('../models/user.model.js');
const { json } = require('express');
const bcrypt = require("bcryptjs");

router.get('/', async function(req, res) {
    const userId = req.query.id;
    const rows = await userModel.get_user_by_id(userId);
    var user;
    
    if(rows[0].length > 0 ) {
        user = rows[0][0];
    } else {
        return res.status(403).json(
            {
                "message": "User is not exist"
            }
        );
    }
    return res.json(
            user
    );
});

router.get('/name', async function(req, res) {
    const username = req.query.username;
    const rows = await userModel.get_user_by_username(username);
    var user;
    
    if(rows[0].length > 0 ) {
        user = rows[0][0];
    } else {
        return res.status(403).json(
            {
                "message": "User is not exist"
            }
        );
    }
    return res.send(
            user
    );
});

router.get('/all', async function(req, res) {
    const rows = await userModel.all();
    var users;
    
    if(rows[0].length > 0 ) {
        users = rows[0];
    } else {
        return res.json(
            {
                "message": "User is not exist"
            }
        );
    }

    return res.json(
        {
            "data": users
        }
    );
});

router.delete('/', async function(req, res) {
    const userId = req.query.id;
    const rows = await userModel.remove(userId);

    return res.json({"message" : "success"});
})

router.post('/add', async function(req, res) {
    const data = req.body;
    console.log(data.password);
    let rows = await userModel.add(data.username, data.email, data.password, data.gender, data.age, data.avatarUrl);

    return res.json({"message" : "success"});
});

router.post('/auth', async function(req, res) {
    const data = req.body;
    const rows = await userModel.get_user_pw_by_username(data.username);
    
    if(rows[0].length <= 0 ) {
        return res.status(403).json(
            {
                "message": "User is not exist"
            }
        );
    } 

    return res.json({"pass" : rows[0][0].pass});
});

module.exports = router;