package com.example.cookdi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.cookdi.Model.RecipeModel;
import com.example.cookdi.PagerAdapter.PagerAdapter;
import com.example.cookdi.RecipeAdapter.RecipeHomeAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout tabBarMenu;
    TabItem tabHome;
    TabItem tabFavorite;
    TabItem tabSaved;

    ViewPager viewPagerMain;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabBarMenu = findViewById(R.id.tabBarMenu);
        tabHome = findViewById(R.id.tabHome);
        tabFavorite = findViewById(R.id.tabFavorite);
        tabSaved = findViewById(R.id.tabSaved);
        viewPagerMain = findViewById(R.id.viewPagerMain);

        initViewPager();

    }

    public void initViewPager(){
        pagerAdapter = new com.example.cookdi.PagerAdapter.PagerAdapter(getSupportFragmentManager());
        viewPagerMain.setAdapter(pagerAdapter);

        RecyclerView rviewHomeRecipes = findViewById(R.id.rviewHomeRecipes);
//        RecyclerView rviewFavoriteRecipes = findViewById(R.id.rviewFavoriteRecipes);
//        RecyclerView rviewSavedRecipes = findViewById(R.id.rviewSavedRecipes);

        ArrayList<RecipeModel> recipeHomeList = new ArrayList<>();
        RecipeHomeAdapter recipeHomeAdapter = new RecipeHomeAdapter(this, recipeHomeList);

        rviewHomeRecipes.setHasFixedSize(true);
        rviewHomeRecipes.setLayoutManager(new LinearLayoutManager(this));
        rviewHomeRecipes.setAdapter(recipeHomeAdapter);

        tabBarMenu.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerMain.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}