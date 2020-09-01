package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
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
    Call<List<RecipeDetail>> getAllRecipe(@Query("page") int page, @Query("user_id") int user_id );

    @GET("/recipe/detail")
    Call<RecipeDetailSteps> getRecipeSteps(@Query("recipe_id") int id, @Query("user_id") int user_id );

    @GET("/recipe/user/recipe")
    Call<List<RecipeDetail>> getManagedRecipe(@Query("page") int page, @Query("user_id") int user_id );

    @GET("/recipe/search")
    Call<List<RecipeDetail>> searchRecipe(@Query("page") int page, @Query("user_id") int user_id, @Query("keyword") String keyword);

    @POST("/recipe/add")
    Call<Map<String, String>> addRecipe(@Body() HashMap<String, Object> params);

    @POST("/recipe/update")
    Call<Map<String, String>> updateRecipe(@Body() HashMap<String, Object> params);


}
