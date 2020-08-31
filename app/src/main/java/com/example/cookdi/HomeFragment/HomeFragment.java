package com.example.cookdi.HomeFragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cookdi.HomeFragment.RecipeAdapter.RecipeHomeAdapter;
import com.example.cookdi.R;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.Recipe;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.sharepref.SharePref;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass. Use the {@link HomeFragment#newInstance}
 * factory method to create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

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

    private View fragmentView;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the
     * provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            return fragmentView;
        } else {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.home_fragment, container, false);
            list = (RecyclerView) view.findViewById(R.id.rviewHomeRecipes);
            getHomeData();
            fragmentView = view;
            return view;
        }
    }

    private void getHomeData() {

        ServiceManager.getInstance().getRecipeService()
                .getAllRecipe(page, Integer.parseInt(SharePref.getInstance(getContext()).getUuid()))
                .enqueue(new Callback<List<RecipeDetail>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<List<RecipeDetail>> call, Response<List<RecipeDetail>> response) {
                        if (page == 0) {
                            recipeHomeList = response.body();
                        } else {
                            if (response.body() != null) {
                                recipeHomeList.remove(recipeHomeList.size() - 1);

                                int scrollPosition = recipeHomeList.size();
                                recipeHomeAdapter.notifyItemRemoved(scrollPosition);

                                recipeHomeList.addAll(response.body());

                                recipeHomeAdapter.notifyDataSetChanged();
                                isItemLoading = false;
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
        recipeHomeAdapter = new RecipeHomeAdapter(getActivity(), recipeHomeList);
        list.setAdapter(recipeHomeAdapter);
        initScrollListener();
    }

    private void initScrollListener() {
        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (linearLayoutManager != null) {
                    if (!isItemLoading) {
                        if (linearLayoutManager.findLastVisibleItemPosition() == recipeHomeList.size() - 1) {
                            loadMore();
                            isItemLoading = true;
                        }
                    }
                }
            }
        });
    }

    public void loadMore() {
        recipeHomeList.add(null);
        recipeHomeAdapter.notifyItemInserted(recipeHomeList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getHomeData();
            }
        }, 2000);
    }
}