package com.example.binge.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binge.Models.ReplyModel;
import com.example.binge.R;
import com.example.binge.databinding.ReplySampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> {

    Context context;
    ArrayList<ReplyModel> list;

    public ReplyAdapter(Context context, ArrayList<ReplyModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reply_sample,parent,false);
        return new ReplyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReplyModel replyModel = list.get(position);


        holder.binding.replyMsg.setText(replyModel.getReplyMsg());
        Date date = new Date(replyModel.getReplyAt());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        holder.binding.date.setText(""+formatter.format(date));

        //to set username
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(replyModel.getReplyBy()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String un = snapshot.getValue(String.class);
                holder.binding.username.setText(un);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        //to set profile pic
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(replyModel.getReplyBy()).child("profilepic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pp = snapshot.getValue(String.class);
                Picasso.get().load(pp)
                        .placeholder(R.drawable.fight)
                        .into(holder.binding.profilePic);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ReplySampleBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReplySampleBinding.bind(itemView);
        }
    }
}
