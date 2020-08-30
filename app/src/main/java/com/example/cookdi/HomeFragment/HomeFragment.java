package com.example.cookdi.HomeFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cookdi.MainActivity;
import com.example.cookdi.Model.RecipeModel;
import com.example.cookdi.R;
import com.example.cookdi.RecipeAdapter.RecipeHomeAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<RecipeModel> recipeHomeList;
    private final int ITEMS_LIMIT = 10;

    private RecyclerView list;
    private boolean isItemLoading = false;

    private RecipeHomeAdapter recipeHomeAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        list = (RecyclerView) view.findViewById(R.id.rviewHomeRecipes);
        getHomeData();
        return view;
    }

    private void getHomeData(){

        ServiceManager.getInstance().getRecipeService().getAllRecipe(page).enqueue(new Callback<List<RecipeDetail>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<RecipeDetail>> call, Response<List<RecipeDetail>> response) {
                Log.d("HOME","onResponse: " + page);
                if (page == 0) {
                    recipeHomeList = response.body();
                    setRecipeHomeAdapter();
                    initScrollListener();
                } else {
                    if (response.body() != null) {
                       boolean isSuccess = recipeHomeList.addAll(response.body());
                    }
                }

                if (response.body() == null) {
//                    list.clearOnScrollListeners();
                } else {
                    page += 1;
                }

            }
    });
    }


    private void initScrollListener(){
//        Log.d("f22222222222", "to inint scroll listener");
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

//        Log.d("ffffffffffff", "to loadmore");

        recipeHomeList.add(null);
        recipeHomeAdapter.notifyItemInserted(recipeHomeList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recipeHomeList.remove(recipeHomeList.size() - 1);
                int scrollPosition = recipeHomeList.size();
                recipeHomeAdapter.notifyItemRemoved(scrollPosition);

//                int currentSize = scrollPosition;
//                int nextLimit = currentSize + recipeHomeAdapter.getItemLimit();
//                RecipeDetail r4 = recipeHomeList.get(0);
//                recipeHomeList.add(r4);

                getHomeData();
                recipeHomeAdapter.notifyDataSetChanged();
                isItemLoading = false;
            }
        }, 2000);

    }
}