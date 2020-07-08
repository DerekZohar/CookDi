package com.example.cookdi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabBarMenu;
    TabItem tabHome;
    TabItem tabFavorite;

    ViewPager viewPagerMain;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabBarMenu = findViewById(R.id.tabBarMenu);
        tabHome = findViewById(R.id.tabHome);
        tabFavorite = findViewById(R.id.tabFavorite);
        viewPagerMain = findViewById(R.id.viewPagerMain);

        initViewPager();

    }

    public void initViewPager(){
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPagerMain.setAdapter(pagerAdapter);

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