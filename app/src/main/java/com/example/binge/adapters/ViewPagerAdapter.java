package com.example.binge.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.binge.Fragment.MovieDiscoverFragment;
import com.example.binge.Fragment.DiscoverMovieFragment;
import com.example.binge.Fragment.DiscoverUserFragment;
import com.example.binge.Fragment.NotificationFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new MovieDiscoverFragment();
            case 1: return new DiscoverUserFragment();
            default: return new DiscoverMovieFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position==0)
        {
            title = "MOVIES";
        }
        else if(position == 1)
            title = "USERS";

        return title;
    }
}
