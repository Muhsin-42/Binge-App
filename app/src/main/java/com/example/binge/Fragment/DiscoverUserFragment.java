package com.example.binge.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.binge.Models.Users;
import com.example.binge.adapters.UserAdapter;
import com.example.binge.databinding.FragmentDiscoverUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DiscoverUserFragment extends Fragment {

    FragmentDiscoverUserBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase database;

    public DiscoverUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        View view = inflater.inflate(R.layout.fragment_profile,container);
        binding = FragmentDiscoverUserBinding.inflate(inflater,container,false);

        UserAdapter adapter = new UserAdapter(getContext(),list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.usersRv.setLayoutManager(layoutManager);
        binding.usersRv.setAdapter(adapter);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserid(dataSnapshot.getKey());
                    if(!dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid()))
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
        // Inflate the layout for this fragment
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}