package com.example.binge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.binge.Fragment.MovieDiscoverFragment;
import com.example.binge.Fragment.MovieFragment;
import com.example.binge.Fragment.ProfileFragment;
import com.example.binge.Fragment.ShowsFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class TtActivity extends AppCompatActivity {

    private MeowBottomNavigation bnv_Main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tt);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        bnv_Main = findViewById(R.id.bnv_Main);
        bnv_Main.add(new MeowBottomNavigation.Model(1,R.drawable.ic_movie_foreground));
        bnv_Main.add(new MeowBottomNavigation.Model(2,R.drawable.ic_discover_foreground));
        bnv_Main.add(new MeowBottomNavigation.Model(3,R.drawable.ic_profile_foreground));

        bnv_Main.show(1,true);
        bnv_Main.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId())
                {
                    case 1:
                        replace(new MovieFragment());
                        break;
                    case 2:
                        replace(new MovieDiscoverFragment());
                        break;
                    case 3:
                        replace(new ProfileFragment());
                        break;
                }
                return null;
            }
        });
    }
    private void replace(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framett,fragment);
        transaction.commit();
    }
}