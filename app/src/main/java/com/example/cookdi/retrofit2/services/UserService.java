package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @GET("/user")
    Call<User> getUserInformation();

    @GET("/user/name")
    Call<User> getUserByName(@Query("username") String name);

    @POST("/user/add")
    Call<Map<String, String>> registerAccount(@Body()Map<String, Object> params);

    @POST("/user/auth")
    Call<Map<String, String>> authentication(@Body()Map<String, Object> params);
}
