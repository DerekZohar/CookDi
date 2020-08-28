package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("/recipe/all")
    Call<List<RecipeDetail>> getAllRecipe(@Query("page") int page);

    @POST("/recipe/add")
    Call<Map<String, String>> addRecipe(@Body() HashMap<String, Object> params);
}
