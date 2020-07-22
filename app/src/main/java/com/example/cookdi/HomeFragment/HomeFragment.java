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

        recipeHomeList = getHomeData();

        list = (RecyclerView) view.findViewById(R.id.rviewHomeRecipes);
        recipeHomeAdapter = new RecipeHomeAdapter(getActivity(), recipeHomeList);
        list.setAdapter(recipeHomeAdapter);
        initScrollListener();
        return view;
    }

    private ArrayList<RecipeModel> getHomeData(){

        RecipeModel r1 = new RecipeModel(123, 123, 4.5, false, true,
                "https://image.tmdb.org/t/p/w185//2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg",
                "https://www.bbcgoodfood.com/sites/default/files/recipe-collections/collection-image/2013/05/chorizo-mozarella-gnocchi-bake-cropped.jpg",
                "Nguyen Huu Tien", "Mi Y", "30-35p");

        RecipeModel r2 = new RecipeModel(124, 125, 3.0, true, true,
                "https://f0.pngfuel.com/png/340/946/man-face-illustration-avatar-user-computer-icons-software-developer-avatar-png-clip-art.png",
                "https://ichef.bbci.co.uk/food/ic/food_16x9_832/recipes/sardinesca_94123_16x9.jpg",
                "Tien Nguyen Huu", "Bun Cha Ha Hoi", "60-125p");

        RecipeModel r3 = new RecipeModel(123, 123, 4.5, false, false,
                "https://image.tmdb.org/t/p/w185//2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg",
                "https://www.bbcgoodfood.com/sites/default/files/recipe-collections/collection-image/2013/05/chorizo-mozarella-gnocchi-bake-cropped.jpg",
                "Doc Co Cau Bai", "Pho Sinh Vien", "120-180p");

        RecipeModel r4 = new RecipeModel(123, 123, 4.5, true, false,
                "https://image.tmdb.org/t/p/w185//2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg",
                "https://www.bbcgoodfood.com/sites/default/files/recipe-collections/collection-image/2013/05/chorizo-mozarella-gnocchi-bake-cropped.jpg",
                "AbcXyz All Night", "Banh Canh Cua", "90-100p");

        ArrayList<RecipeModel> list = new ArrayList<>();
        list.add(r1);
        list.add(r2);
        list.add(r3);
        list.add(r4);
        list.add(r4);
        list.add(r1);
        list.add(r2);
        list.add(r3);
        list.add(r4);
        list.add(r4);
        return list;
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

        Log.d("ffffffffffff", "to loadmore");

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

                    RecipeModel re = new RecipeModel(123, 123, 4.5, false, true,
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTaw3hvjrmp7igf5FTOX_ULVLg_c9nFXFokpA&usqp=CAU",
                            "https://food.fnr.sndimg.com/content/dam/images/food/fullset/2012/11/2/0/DV1510H_fried-chicken-recipe-10_s4x3.jpg.rend.hgtvcom.826.620.suffix/1568222255998.jpeg",
                            "Sick My Duck", "Dui Ga Chien Nuoc Mam", "40-45p");
                    recipeHomeList.add(re);

                    currentSize++;
                }

                recipeHomeAdapter.notifyDataSetChanged();
                isItemLoading = false;
            }
        }, 2000);

    }
}