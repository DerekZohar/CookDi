package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.RecipeDetail;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FavoriteService {

    @POST("/favorite/add")
    Call<Map<String, String>> addToFavorite(@Body()Map<String, Object> params);

    @HTTP(method = "DELETE", path = "/favorite/remove", hasBody = true)
    Call<Map<String, String>> delete(@Body()Map<String, Object> params);

    @GET("/favorite/all")
    Call<List<RecipeDetail>> getAllFavoriteRecipe(@Query("page") int page, @Query("user_id") int user_id);
}
