package com.example.binge.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binge.Models.CommentModel;
import com.example.binge.Models.Follow;
import com.example.binge.Models.MovieModel;
import com.example.binge.Models.NotificationModel;
import com.example.binge.R;
import com.example.binge.ReplyActivity;
import com.example.binge.databinding.RvCommentsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>{

    Context context;
    ArrayList<CommentModel> list;

    public CommentsAdapter(Context context, ArrayList<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_comments,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CommentModel commentModel = list.get(position);
        MovieModel movieModel = new MovieModel();
        Picasso.get().load(commentModel.getCommentImg())
                .into(holder.binding.commentImage);
        holder.binding.commentMsg.setText(commentModel.getCommentMsg());

        if(commentModel.getCommentImg()==null)
        {
            holder.binding.commentImage.setVisibility(View.GONE);
        }
        //Time & Date

        Date date = new Date(commentModel.getCommentAt());
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        holder.binding.cmtDate.setText(""+formatter.format(date));



        //to set username
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(commentModel.getCommentBy()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String un = snapshot.getValue(String.class);
                holder.binding.cmtUsername.setText(un);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        //to set profile pic
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(commentModel.getCommentBy()).child("profilepic").addListenerForSingleValueEvent(new ValueEventListener() {
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

        //If Already Liked
        FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(commentModel.getMovieId())
                .child(commentModel.getUniqueKey())
                .child("LikedBy").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    holder.binding.like.setImageResource(R.drawable.ic_favorite_filled);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        //Likes count
        FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(commentModel.getMovieId())
                .child(commentModel.getUniqueKey())
                .child("LikedBy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long likesCount =  snapshot.getChildrenCount();
                holder.binding.likesCount.setText(Long.toString(likesCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Replies count
        FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(commentModel.getMovieId())
                .child(commentModel.getCommentId())
                .child("replies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long repliesCount =  snapshot.getChildrenCount();
                holder.binding.replyCount.setText(Long.toString(repliesCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ////////////////////////////
        // Like and Unlike
        //////////////////////////
        holder.binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Comments")
                        .child(commentModel.getMovieId())
                        .child(commentModel.getUniqueKey())
                        .child("LikedBy").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Unlike
                        if(snapshot.exists())
                        {
                            FirebaseDatabase.getInstance().getReference().child("Comments")
                                    .child(commentModel.getMovieId())
                                    .child(commentModel.getUniqueKey())
                                    .child("LikedBy").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    holder.binding.like.setImageResource(R.drawable.ic_favourite_outlined);
                                }
                            });
                        }
                        //Like
                        else
                        {
                            Follow likeModel = new Follow();
                            likeModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            likeModel.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference().child("Comments")
                                    .child(commentModel.getMovieId())
                                    .child(commentModel.getUniqueKey())
                                    .child("LikedBy")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(likeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    holder.binding.like.setImageResource(R.drawable.ic_favorite_filled);

                                    NotificationModel notificationModel =new NotificationModel();
                                    notificationModel.setNotifType("like");
                                    notificationModel.setNotifAt(new Date().getTime());
                                    notificationModel.setNotifBy(FirebaseAuth.getInstance().getUid());
                                    notificationModel.setCommentId(commentModel.getCommentId());
                                    notificationModel.setCommentBy(commentModel.getCommentBy());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Notification")
                                            .child(commentModel.getCommentBy())
                                            .push()
                                            .setValue(notificationModel);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        /////////////////////////////////
        //  GO TO REPLY ACTIVITY
        ////////////////////////////////
        holder.binding.replyIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReplyActivity.class);
//                intent.putExtra("movie", movieModel);
                String cid = commentModel.getCommentId();
                String mid = commentModel.getMovieId();
                intent.putExtra("commentId", cid);
                intent.putExtra("movieId", mid);
                context.startActivity(intent);
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


    public class MyViewHolder extends RecyclerView.ViewHolder{
        RvCommentsBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RvCommentsBinding.bind(itemView);
        }
    }
}
