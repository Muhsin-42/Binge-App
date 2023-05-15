package com.example.binge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.binge.Models.Users;
import com.example.binge.adapters.UserAdapter;
import com.example.binge.databinding.ActivityFollowersBinding;
import com.example.binge.databinding.ActivityFollowingBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowersActivity extends AppCompatActivity {

    ArrayList<Users> list = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;
    String userId;

    ActivityFollowersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        /////////////////////////////////////////////////
        //  To get User id from intent
        ////////////////////////////////////////////////
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                userId= null;
            } else {
                userId= extras.getString("userId");
            }
        } else {
            userId= (String) savedInstanceState.getSerializable("userId");
        }




        binding = ActivityFollowersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserAdapter adapter = new UserAdapter(FollowersActivity.this,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(FollowersActivity.this);
        binding.usersRv.setLayoutManager(layoutManager);
        binding.usersRv.setAdapter(adapter);

        database.getReference().child("Users").child(userId)
                .child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    binding.noFollowersTv.setVisibility(View.GONE);
                }
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserid(dataSnapshot.getKey());
                    users.setTypeFollowing(true);
                    if(!dataSnapshot.getKey().equals(userId))
                    {
                        list.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}