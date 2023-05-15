package com.example.binge.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.binge.Models.MovieModel;
import com.example.binge.R;
import com.example.binge.adapters.MovieFirebaseAdapter;
import com.example.binge.databinding.FragmentMovieBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MovieFragment extends Fragment {

//    FragmentNotificationBinding binding;
//    ArrayList<MovieModel> list = new ArrayList<>();
    MovieFirebaseAdapter movieFirebaseAdapter;
FragmentMovieBinding binding;
    ArrayList<MovieModel> list = new ArrayList<>();



    public MovieFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_movie, container, false);
        binding = FragmentMovieBinding.inflate(inflater,container,false);

        ////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////


        MovieFirebaseAdapter movieFirebaseAdapter = new MovieFirebaseAdapter(getContext(),list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
        binding.recyclerViewPop.setLayoutManager(gridLayoutManager);
        binding.recyclerViewPop.setAdapter(movieFirebaseAdapter);

        FirebaseDatabase.getInstance().getReference().child("TrendingMovies")
                .orderByChild("addedOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    MovieModel movieModel = dataSnapshot.getValue(MovieModel.class);
//                    movieModel.setNotifId(dataSnapshot.getKey());
                    list.add(movieModel);
                }
                movieFirebaseAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        return   binding.getRoot();
    }
}


