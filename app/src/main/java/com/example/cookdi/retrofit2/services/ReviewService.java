package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.Review;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReviewService {
    @GET("/comment/all")
    Call<List<Review>> getAllReview(@Query("recipe_id") int id);

    @POST("/comment/add")
    Call<Map<String, String>> addReview(@Body()Map<String, Object> params);

    @DELETE("/comment/remove")
    Call<Map<String, String>> removeReview(@Query("user_id") String uid, @Query("recipe_id") int rid);
}
