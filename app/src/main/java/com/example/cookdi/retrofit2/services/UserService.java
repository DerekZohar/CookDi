package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.Recipe;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.User;
import com.example.cookdi.retrofit2.entities.UserDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @GET("/user")
    Call<User> getUserInformation();

    @GET("/user/all")
    Call<ArrayList<User>> getAllUsers();

    @GET("/user")
    Call<User> getUserByID(@Query("id") String user_id);

    @GET("/user/name")
    Call<User> getUserByName(@Query("username") String name);


    @POST("/user/add")
    Call<Map<String, String>> registerAccount(@Body() Map<String, Object> params);

    @POST("/user/auth")
    Call<Map<String, String>> authentication(@Body() Map<String, Object> params);

    @POST("/user/edit/mail")
    Call<Map<String, String>> editUserEmail(@Body() Map<String, Object> params);

    @POST("/user/edit/avatar")
    Call<Map<String, String>> editUserAvatar(@Body() Map<String, Object> params);

    @POST("/user/edit/pass")
    Call<Map<String, String>> editUserPass(@Body() Map<String, Object> params);

}
