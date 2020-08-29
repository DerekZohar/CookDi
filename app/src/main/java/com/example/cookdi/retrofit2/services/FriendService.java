package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FriendService {
    @GET("/friend/all")
    Call<ArrayList<User>> getAllFriends(@Query("id") int id);
}
