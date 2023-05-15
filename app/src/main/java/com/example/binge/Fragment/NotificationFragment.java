package com.example.binge.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.binge.Models.NotificationModel;
import com.example.binge.R;
import com.example.binge.adapters.NotificationAdapter;
import com.example.binge.databinding.FragmentNotificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    FragmentNotificationBinding binding;
    ArrayList<NotificationModel> list = new ArrayList<>();
    public NotificationFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);
        binding = FragmentNotificationBinding.inflate(inflater,container,false);

        ////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////


        NotificationAdapter adapter = new NotificationAdapter(getContext(),list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.notificationRv.setLayoutManager(layoutManager);
        binding.notificationRv.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("Notification")
                .child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            NotificationModel notification = dataSnapshot.getValue(NotificationModel.class);
                            notification.setNotifId(dataSnapshot.getKey());
                            list.add(notification);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        return   binding.getRoot();
    }
}