package com.example.cookdi.retrofit2.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class UserDetail {
    @SerializedName("")
    private ArrayList<User> user;

    public ArrayList<User> getList(){ return user;}
}
