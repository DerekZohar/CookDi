package com.example.cookdi.HomeFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    RecyclerView list;
    ArrayList<RecipeModel> recipeHomeList;
    RecipeHomeAdapter recipeHomeAdapter;


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
        recipeHomeList = getHomeData();
        recipeHomeAdapter = new RecipeHomeAdapter(getActivity(), recipeHomeList);
        list.setAdapter(recipeHomeAdapter);
        list.setHasFixedSize(true);
        return view;

        //test UI only
//        return inflater.inflate(R.layout.home_recipe, container, false);
    }


    public ArrayList<RecipeModel> getHomeData(){

        RecipeModel r1 = new RecipeModel(123, 123, 4.5, false, true,
                "https://image.tmdb.org/t/p/w185//2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg",
                "https://disease.sh/assets/img/flags/vn.png",
                "Nguyen Huu Tien", "Mi Y", "30-35p");

        RecipeModel r2 = new RecipeModel(124, 125, 3.0, true, true,
                "https://f0.pngfuel.com/png/340/946/man-face-illustration-avatar-user-computer-icons-software-developer-avatar-png-clip-art.png",
                "https://ichef.bbci.co.uk/food/ic/food_16x9_832/recipes/sardinesca_94123_16x9.jpg",
                "Nguyen Huu", "Bun Cha", "20-35p");

        ArrayList<RecipeModel> list = new ArrayList<>();
        list.add(r1);
        list.add(r2);
        list.add(r1);
        list.add(r2);
        list.add(r1);
        return list;
    }
}