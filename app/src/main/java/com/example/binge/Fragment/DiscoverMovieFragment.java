// This Fragment is base fragment for both movie search and User discovery
// It is named DiscoverMoviesFragment by negligence

package com.example.binge.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.binge.R;
import com.example.binge.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class DiscoverMovieFragment extends Fragment {

    ViewPager viewPager;
    TabLayout tabLayout;


    public DiscoverMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_discover_movie, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);


        return  view;
    }
}