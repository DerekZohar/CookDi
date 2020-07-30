package com.example.cookdi.retrofit2.services;

import com.example.cookdi.retrofit2.entities.Tag;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TagService {
    @GET("/tags")
    Call<List<Tag>> getAllTag();
}
