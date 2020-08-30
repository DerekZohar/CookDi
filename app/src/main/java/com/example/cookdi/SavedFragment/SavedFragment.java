package com.example.cookdi.SavedFragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cookdi.SavedFragment.RecipeAdapter.RecipeSavedAdapter;
import com.example.cookdi.R;
import com.example.cookdi.db.RecipeListDBAdapter;
import com.example.cookdi.db.UserListDBAdapter;
import com.example.cookdi.retrofit2.ServiceManager;
import com.example.cookdi.retrofit2.entities.Recipe;
import com.example.cookdi.retrofit2.entities.RecipeDetail;
import com.example.cookdi.retrofit2.entities.SavedRecipe;
import com.example.cookdi.retrofit2.entities.SavedUser;
import com.example.cookdi.retrofit2.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<RecipeDetail> recipeSavedList;
    private final int ITEMS_LIMIT = 10;

    private RecyclerView list;
    private boolean isItemLoading = false;
    private int page = 0;
    private RecipeSavedAdapter recipeSavedAdapter;


    public SavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
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
        View view = inflater.inflate(R.layout.saved_fragment, container, false);
        list = (RecyclerView) view.findViewById(R.id.rviewSavedRecipes);
        getSavedData();
        return view;
    }

    private void getSavedData(){
        recipeSavedList = new ArrayList<RecipeDetail>();
        ArrayList<SavedRecipe> recipes = RecipeListDBAdapter.getAllRecipes();
        for(SavedRecipe re : recipes){
            RecipeDetail recipeDetail = new RecipeDetail();
            recipeDetail.setRecipe(re);

            SavedUser user = UserListDBAdapter.getUserById(re.getUserId());
            if(user != null){
                recipeDetail.setChef(user);
                recipeSavedList.add(recipeDetail);
            }
            else {
                Log.d("USER ID=", "NOT FOUND " + re.getUserId());
            }
        }

        setRecipeSavedAdapter();
    }

    private void setRecipeSavedAdapter() {
        recipeSavedAdapter = new RecipeSavedAdapter(getActivity(), recipeSavedList);
        list.setAdapter(recipeSavedAdapter);
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
                        if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == recipeSavedList.size() - 1){
                            loadMore();
                            isItemLoading = true;
                        }
                    }
                }
            }
        });
    }

    public void loadMore(){
        recipeSavedList.add(null);
        recipeSavedAdapter.notifyItemInserted(recipeSavedList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recipeSavedList.remove(recipeSavedList.size() - 1);
                int scrollPosition = recipeSavedList.size();
                recipeSavedAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + recipeSavedAdapter.getItemLimit();

                while (currentSize - 1 < nextLimit) {
                    getSavedData();

                    currentSize++;
                }

                recipeSavedAdapter.notifyDataSetChanged();
                isItemLoading = false;
            }
        }, 2000);
    }
}