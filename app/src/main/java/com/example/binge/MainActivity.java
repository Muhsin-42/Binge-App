package com.example.binge;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.binge.Fragment.DiscoverMovieFragment;
import com.example.binge.Fragment.MovieFragment;
import com.example.binge.Fragment.NotificationFragment;
import com.example.binge.Fragment.ProfileFragment;
import com.example.binge.databinding.ActivityMainBinding;
import com.iammert.library.readablebottombar.ReadableBottomBar;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.toolbar22);
//        MainActivity.this.setTitle("My Profile");
//        binding.toolbar22.setVisibility(View.GONE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new MovieFragment());
        transaction.commit();


        binding.readableBottomBar.setOnItemSelectListener(new ReadableBottomBar.ItemSelectListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (i) {
                    case 0:
//                        binding.toolbar22.setVisibility(View.GONE);
                        transaction.replace(R.id.container, new MovieFragment());
                        break;
                    case 1:
//                        binding.toolbar22.setVisibility(View.GONE);
                        transaction.replace(R.id.container, new DiscoverMovieFragment());
                        break;
                    case 2:
//                        binding.toolbar22.setVisibility(View.GONE);
                        transaction.replace(R.id.container, new NotificationFragment());
                        break;
                    case 3:
//                        binding.toolbar22.setVisibility(View.VISIBLE);
                        transaction.replace(R.id.container, new ProfileFragment());
                        break;
                }
                transaction.commit();
            }
        });
    }
}