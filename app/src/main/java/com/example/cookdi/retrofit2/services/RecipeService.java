package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.RecipeDetailSteps;
import com.example.cookdi.retrofit2.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("/recipe/all")
    Call<List<RecipeDetail>> getAllRecipe(@Query("page") int page);

    @GET("/recipe?recipe_id=1")
    Call<RecipeDetailSteps> getRecipeSteps();
}
