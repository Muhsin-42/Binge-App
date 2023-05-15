package com.example.binge.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binge.FriendProfileActivity;
import com.example.binge.Models.NotificationModel;
import com.example.binge.R;
import com.example.binge.databinding.NotificationRvDesignBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    Context context;
    ArrayList<NotificationModel> list;


    public NotificationAdapter(Context context, ArrayList<NotificationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_rv_design,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NotificationModel notification = list.get(position);

        Date date = new Date(notification.getNotifAt());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


        //to set username
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(notification.getNotifBy()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String un = snapshot.getValue(String.class);
                holder.binding.username.setText(un);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        //to set pp
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(notification.getNotifBy()).child("profilepic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pp = snapshot.getValue(String.class);
                Picasso.get().load(pp)
                        .placeholder(R.drawable.fight)
                        .into(holder.binding.profilePic);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });




        holder.binding.dateNtime.setText(""+formatter.format(date));
        if(notification.getNotifType().equals("like"))
        {
            holder.binding.notificationMsg.setText("Has liked your comment");
        }
        else{
            holder.binding.notificationMsg.setText("Has followed you");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(context, FriendProfileActivity.class);
                    intent.putExtra("userId", notification.getNotifBy());
                    context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        NotificationRvDesignBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding =  NotificationRvDesignBinding.bind(itemView);
        }
    }
}