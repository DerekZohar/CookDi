package com.example.cookdi.retrofit2;

import com.example.cookdi.config.Config;
import com.example.cookdi.retrofit2.services.FavoriteService;
import com.example.cookdi.retrofit2.services.FriendService;
import com.example.cookdi.retrofit2.services.RecipeService;
import com.example.cookdi.retrofit2.services.ReviewService;
import com.example.cookdi.retrofit2.services.UserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceManager {
    private ServiceManager() {
        initRetrofit();
    }

    private static ServiceManager instance = new ServiceManager();

    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    private Retrofit retrofit;

    private void initRetrofit() {
        retrofit = new Retrofit.Builder().baseUrl(Config.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public UserService getUserService() {
        return retrofit.create(UserService.class);
    }

    public RecipeService getRecipeService() {
        return retrofit.create(RecipeService.class);
    }

    public FavoriteService getFavoriteService() {
        return retrofit.create(FavoriteService.class);
    }

    public ReviewService getReviewService() {
        return retrofit.create(ReviewService.class);
    }

    public FriendService getFriendService() {
        return retrofit.create(FriendService.class);
    }
}
