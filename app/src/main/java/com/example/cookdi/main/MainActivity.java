package com.example.cookdi.main;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.cookdi.ContactFragment.ContactFragment;
import com.example.cookdi.FavoriteFragment.FavoriteFragment;
import com.example.cookdi.HomeFragment.HomeFragment;
import com.example.cookdi.Model.RecipeModel;
import com.example.cookdi.PagerAdapter.PagerAdapter;
import com.example.cookdi.R;
import com.example.cookdi.RecipeAdapter.RecipeHomeAdapter;
import com.example.cookdi.SavedFragment.SavedFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PagerAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setElevation(0);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        tabAdapter.addFragment(new HomeFragment(), "Home");
        tabAdapter.addFragment(new FavoriteFragment(), "Favorite");
        tabAdapter.addFragment(new SavedFragment(), "Saved");
        tabAdapter.addFragment(new ContactFragment(), "Contact");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        highLightCurrentTab(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(tabAdapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(tabAdapter.getSelectedTabView(position));
    }

}