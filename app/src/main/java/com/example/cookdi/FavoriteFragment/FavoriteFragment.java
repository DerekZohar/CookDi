package com.example.cookdi.FavoriteFragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cookdi.FavoriteFragment.RecipeAdapter.RecipeFavoriteAdapter;
import com.example.cookdi.R;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.sharepref.SharePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefreshLayout;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<RecipeDetail> recipeFavoriteList;
    private int page = 0;

    private RecyclerView list;

    private RecipeFavoriteAdapter recipeFavoriteAdapter;


    public FavoriteFragment() {
    }


    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.favorite_fragment);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        list = (RecyclerView) view.findViewById(R.id.rviewFavoriteRecipes);
        recipeFavoriteAdapter = new RecipeFavoriteAdapter(getActivity(), recipeFavoriteList);
        getFavoriteData();

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getFavoriteData();
            }
        });
        return view;
    }

    private void getFavoriteData(){
        int user_id = Integer.valueOf(SharePref.getInstance(getContext()).getUuid());
        ServiceManager.getInstance().getFavoriteService().getAllFavoriteRecipe(page, user_id).enqueue(new Callback<List<RecipeDetail>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<RecipeDetail>> call, final Response<List<RecipeDetail>> response) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (page == 0) {
                            recipeFavoriteList = response.body();
                        }

                        setRecipeFavoriteAdapter();
                    }
                }, 700);

            }

            @Override
            public void onFailure(Call<List<RecipeDetail>> call, Throwable t) {

            }
        });
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setRecipeFavoriteAdapter() {
        recipeFavoriteAdapter = new RecipeFavoriteAdapter(getActivity(), recipeFavoriteList);
        list.setAdapter(recipeFavoriteAdapter);
    }

    @Override
    public void onRefresh() {
        getFavoriteData();
    }
}