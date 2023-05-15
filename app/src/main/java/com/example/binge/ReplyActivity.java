package com.example.binge;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binge.Models.CommentModel;
import com.example.binge.Models.Follow;
import com.example.binge.Models.MovieModel;
import com.example.binge.Models.NotificationModel;
import com.example.binge.Models.ReplyModel;
import com.example.binge.adapters.ReplyAdapter;
import com.example.binge.databinding.ActivityReplyBinding;
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

public class ReplyActivity extends AppCompatActivity {

    MovieModel movieModel;
    CommentModel comment;
    ActivityReplyBinding binding;

    RecyclerView repliesRv;
    ArrayList<ReplyModel> replyModelList=new ArrayList<>();
    CommentModel commentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        getSupportActionBar().hide();


        //to get comment id from intent
        String commentId,movieId;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                commentId= null;
                movieId = null;
            } else {
                commentId= extras.getString("commentId");
                movieId = extras.getString("movieId");
            }
        } else {
            commentId= (String) savedInstanceState.getSerializable("commentId");
            movieId = (String) savedInstanceState.getSerializable("movieId");
        }


        binding = ActivityReplyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        sendBtnOnOff();


        //////////////////////////////////////////////////
        //  Comment
        /////////////////////////////////////////////////
        FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(movieId).child(commentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentModel = snapshot.getValue(CommentModel.class);

                binding.commentMsg.setText(commentModel.getCommentMsg());

                //To set image
                if(commentModel.getCommentImg()==null)
                {
                    binding.commentImage.setVisibility(View.GONE);
                }
                else{
                    Picasso.get().load(commentModel.getCommentImg())
                            .into(binding.commentImage);
                }

                //to set username
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(commentModel.getCommentBy()).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String un = snapshot.getValue(String.class);
                        binding.cmtUsername.setText(un);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                //Time & Date

                Date date = new Date(commentModel.getCommentAt());
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                binding.cmtDate.setText(""+formatter.format(date));


                //to set profile pic
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(commentModel.getCommentBy()).child("profilepic").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String pp = snapshot.getValue(String.class);
                        Picasso.get().load(pp)
                                .placeholder(R.drawable.fight)
                                .into(binding.profilePic);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


//                //If Already Liked
                FirebaseDatabase.getInstance().getReference().child("Comments")
                        .child(commentModel.getMovieId())
                        .child(commentModel.getCommentId())
                        .child("LikedBy").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            binding.like.setImageResource(R.drawable.ic_favorite_filled);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

                //Likes count
                FirebaseDatabase.getInstance().getReference().child("Comments")
                        .child(commentModel.getMovieId())
                        .child(commentModel.getCommentId())
                        .child("LikedBy").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long likesCount =  snapshot.getChildrenCount();
                        binding.likesCount.setText(Long.toString(likesCount));
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
                        binding.replyCount.setText(Long.toString(repliesCount));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                ////////////////////////////
                // Like and Unlike
                //////////////////////////
                binding.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference().child("Comments")
                                .child(commentModel.getMovieId())
                                .child(commentModel.getCommentId())
                                .child("LikedBy").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                // Unlike
                                if(snapshot.exists())
                                {
                                    FirebaseDatabase.getInstance().getReference().child("Comments")
                                            .child(commentModel.getMovieId())
                                            .child(commentModel.getCommentId())
                                            .child("LikedBy").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            binding.like.setImageResource(R.drawable.ic_favourite_outlined);
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
                                            .child(commentModel.getCommentId())
                                            .child("LikedBy")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(likeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            binding.like.setImageResource(R.drawable.ic_favorite_filled);

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ////////////////////////////////////////
        //  WHEN SEND IS CLICKED
        ///////////////////////////////////////
        binding.sendIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReplyModel replyModel = new ReplyModel();
                replyModel.setReplyId(binding.replyText.getText().toString());
                replyModel.setReplyBy(FirebaseAuth.getInstance().getUid());
                replyModel.setReplyAt(new Date().getTime());
                replyModel.setCommentId(commentId);
                replyModel.setReplyMsg(binding.replyText.getText().toString());

                String tempKey = FirebaseDatabase.getInstance().getReference().child("Comments")
                        .child(movieId)
                        .child(commentId).child("replies").push().getKey();
                replyModel.setReplyId(tempKey);

                FirebaseDatabase.getInstance().getReference().child("Comments")
                        .child(movieId)
                        .child(commentId)
                        .child("replies")
                        .child(tempKey).setValue(replyModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.replyText.setText("");
//                        Intent intent = new Intent(ReplyActivity.this,MovieDetails.class);
//                        intent.putExtra("movie", movieModel);
//                        startActivity(intent);
//                        NotificationModel notificationModel =new NotificationModel();
//                        notificationModel.setNotifType("reply");
//                        notificationModel.setNotifAt(new Date().getTime());
//                        notificationModel.setNotifBy(FirebaseAuth.getInstance().getUid());
//                        notificationModel.setCommentId(commentModel.getCommentId());
//                        notificationModel.setCommentBy(commentModel.getCommentBy());
//                        notificationModel.setReplyId(tempKey);
//
//                        FirebaseDatabase.getInstance().getReference()
//                                .child("Notification")
//                                .child(commentModel.getCommentBy())
//                                .push()
//                                .setValue(notificationModel);
                    }
                });

            }
        });






        ///////////////////////////////////////////
        //  Replies Recycler View
        ///////////////////////////////////////////
        ReplyAdapter replyAdapter = new ReplyAdapter(ReplyActivity.this,replyModelList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ReplyActivity.this);
//        commentsRv = findViewById(R.id.commentsRv);
        binding.repliesRv.setLayoutManager(layoutManager);
        binding.repliesRv.setAdapter(replyAdapter);

        FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(movieId)
                .child(commentId)
                .child("replies").orderByChild("replyAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                replyModelList.clear();
//                if(snapshot.exists())
//                {
//                    noCommentTv.setVisibility(View.GONE);
//                }
                for(DataSnapshot datasnapshot: snapshot.getChildren())
                {
                    ReplyModel replyModel =  datasnapshot.getValue(ReplyModel.class);
                    replyModelList.add(replyModel);
                }
//                Collections.reverse(replyModelList);
                replyAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });




    }

    void sendBtnOnOff()
    {
        /////////////////////////////
        //  Enable and disable Post btn when we type
        /////////////////////////////

        if(binding.replyText.getText().toString().equals(""))
        {
            binding.sendIc.setEnabled(false);
        }
        binding.replyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!binding.replyText.getText().toString().isEmpty())
                {
                    binding.sendIc.setEnabled(true);
                }
                else
                {
                    binding.sendIc.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        sendBtnOnOff();
    }
    @Override
    public void onStart() {
        super.onStart();
        sendBtnOnOff();
    }
    @Override
    public void onRestart() {
        super.onRestart();

        sendBtnOnOff();
    }
}