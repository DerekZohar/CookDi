package com.example.cookdi.ManageRecipe;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookdi.HomeFragment.HomeFragment;
import com.example.cookdi.HomeFragment.RecipeAdapter.RecipeHomeAdapter;
import com.example.cookdi.R;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.sharepref.SharePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageRecipeActivity extends AppCompatActivity {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<RecipeDetail> recipeHomeList;
    private final int ITEMS_LIMIT = 10;

    private RecyclerView list;
    private boolean isItemLoading = false;
    private int page = 0;
    private RecipeHomeAdapter recipeHomeAdapter;

    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_recipe);

        context = getApplicationContext();

        list = (RecyclerView) findViewById(R.id.rviewHomeRecipes);
        getHomeData();
    }


    private void getHomeData(){
        ServiceManager.getInstance().getRecipeService().getAllRecipe(page, Integer.parseInt(SharePref.getInstance(context).getUuid())).enqueue(new Callback<List<RecipeDetail>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<RecipeDetail>> call, Response<List<RecipeDetail>> response) {
                if (page == 0) {
                    recipeHomeList = response.body();
                    initScrollListener();
                } else {
                    if (response.body() != null) {
                        boolean isSuccess = recipeHomeList.addAll(response.body());
                    }
                }

                if (response.body() == null) {
                    list.clearOnScrollListeners();
                } else {
                    page += 1;
                }

                setRecipeHomeAdapter();
            }

            @Override
            public void onFailure(Call<List<RecipeDetail>> call, Throwable t) {

            }
        });
    }

    private void setRecipeHomeAdapter() {
        recipeHomeAdapter = new RecipeHomeAdapter(context, recipeHomeList);
        list.setAdapter(recipeHomeAdapter);
    }

    private void initScrollListener(){
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(linearLayoutManager != null){
                    if(!isItemLoading){
                        if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == recipeHomeList.size() - 1){
                            loadMore();
                            isItemLoading = true;
                        }
                    }
                }
            }
        });
    }

    public void loadMore(){
        recipeHomeList.add(null);
        recipeHomeAdapter.notifyItemInserted(recipeHomeList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recipeHomeList.remove(recipeHomeList.size() - 1);
                int scrollPosition = recipeHomeList.size();
                recipeHomeAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + recipeHomeAdapter.getItemLimit();

                while (currentSize - 1 < nextLimit) {
                    getHomeData();

                    currentSize++;
                }

                recipeHomeAdapter.notifyDataSetChanged();
                isItemLoading = false;
            }
        }, 2000);
    }
}
