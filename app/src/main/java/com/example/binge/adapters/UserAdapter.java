package com.example.binge.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binge.FriendProfileActivity;
import com.example.binge.Models.Follow;
import com.example.binge.Models.NotificationModel;
import com.example.binge.Models.Users;
import com.example.binge.R;
import com.example.binge.databinding.UserSampleBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    ArrayList<Users> list;

    public UserAdapter(Context context, ArrayList<Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {


        Users users = list.get(position);

        ////////////////////////////////////
        ////    For followers and following
        ////////////////////////////////////
        if(users.getTypeFollowing())
        {
            FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(users.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users users2 = snapshot.getValue(Users.class);
                    holder.binding.userName.setText(users2.getUsername());
                    Picasso.get().load(users2.getProfilepic())
                            .placeholder(R.drawable.tyrion)
                            .into(holder.binding.profilePic);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        Picasso.get().load(users.getProfilepic())
                .placeholder(R.drawable.tyrion)
                .into(holder.binding.profilePic);
        holder.binding.userName.setText(users.getUsername());


        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(users.getUserid())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.following_btn));
                    holder.binding.followBtn.setText("following");
                    holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.light_blue_600));
//                    holder.binding.followBtn.setEnabled(false);


                    //Unfollow
                    holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("following")
                                    .child(users.getUserid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(users.getUserid())
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid()).removeValue();
                                    holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_btn_bg));
                                    holder.binding.followBtn.setText("FOLLOW");
                                    holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.white));                        }
                            });
                        }
                    });



                }
                else
                {
                    // To follow
                    holder.binding.followBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Follow followModel = new Follow();
                            Follow followingModel = new Follow();
                            followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            followModel.setFollowedAt(new Date().getTime());
                            followingModel.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(users.getUserid())
                                    .child("followers")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(followModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    holder.binding.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.following_btn));
                                    holder.binding.followBtn.setText("following");
                                    holder.binding.followBtn.setTextColor(context.getResources().getColor(R.color.light_blue_600));
//                                    holder.binding.followBtn.setEnabled(false);

                                    NotificationModel notificationModel = new NotificationModel();
                                    notificationModel.setNotifBy(FirebaseAuth.getInstance().getUid());
                                    notificationModel.setNotifAt(new Date().getTime());
                                    notificationModel.setNotifType("follow");

                                    FirebaseDatabase.getInstance().getReference().child("Notification")
                                            .child(users.getUserid())
                                            .push()
                                            .setValue(notificationModel);
                                }
                            });

                            //Following
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("following")
                                    .child(users.getUserid())
                                    .setValue(followingModel);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        int pst = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FriendProfileActivity.class);
                intent.putExtra("userId", users.getUserid());
                context.startActivity(intent);
            }
        });


    }

//    @Override
//    public void onViewRecycled(@NonNull viewHolder holder) {
//        super.onViewRecycled(holder);
//
////        holder.binding.followBtn.setText("asdfasd");
//    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        UserSampleBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = UserSampleBinding.bind(itemView);
        }
    }



    public Users getSelectedUser(int position){
        if (list != null){
            if (list.size() > 0){
                return list.get(position);
            }
        }
        return  null;
    }
}
