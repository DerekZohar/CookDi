package com.example.cookdi.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.provider.Settings.Secure;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.cookdi.ContactFragment.ContactFragment;
import com.example.cookdi.FavoriteFragment.FavoriteFragment;
import com.example.cookdi.HomeFragment.HomeFragment;
import com.example.cookdi.Model.RecipeModel;
import com.example.cookdi.PagerAdapter.PagerAdapter;
import com.example.cookdi.R;
import com.example.cookdi.SavedFragment.SavedFragment;
import com.example.cookdi.chat.ioSocketConnector.IOSocketConnector;
import com.example.cookdi.config.Config;
import com.example.cookdi.search.SearchActivity;
import com.example.cookdi.sharepref.SharePref;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PagerAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setElevation(0);
        String uuid=SharePref.getInstance(getApplicationContext()).getUuid();

        if(!uuid.isEmpty() && uuid.length()>0){
            Config.IOSocketChatConnector = new IOSocketConnector(Config.BASE_URL,uuid);
        }

        //Try send message
        Config.IOSocketChatConnector.SendMessage(uuid,"Message");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

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