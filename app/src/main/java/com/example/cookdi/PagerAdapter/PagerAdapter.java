package com.example.cookdi.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cookdi.FavoriteFragment.FavoriteFragment;
import com.example.cookdi.HomeFragment.HomeFragment;
import com.example.cookdi.SavedFragment.SavedFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private final int NUMBER_OF_TAB = 3;
    private final int  HOME_TAB_POSITION = 0;
    private final int FAVORITE_TAB_POSITION = 1;
    private final int SAVED_TAB_POSITION = 2;

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case HOME_TAB_POSITION:
                return new HomeFragment();
            case FAVORITE_TAB_POSITION:
                return new FavoriteFragment();
            case SAVED_TAB_POSITION:
                return new SavedFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TAB;
    }
}
