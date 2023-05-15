package com.example.binge;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.binge.Models.Follow;
import com.example.binge.Models.MovieModel;
import com.example.binge.Models.Users;
import com.example.binge.adapters.MovieFirebaseAdapter;
import com.example.binge.databinding.ActivityFriendProfileBinding;
import com.example.binge.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class FriendProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    DatabaseReference database;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;

    ActivityResultLauncher<String> activityResultLauncherCover;
    ActivityResultLauncher<String> activityResultLauncherProfile;
    RecyclerView watchedMovieRv, watchlistRv, favMovieRv;
    MovieFirebaseAdapter movieFirebaseAdapter, watchlistAdapter, favMovieAdapter;
    ArrayList<MovieModel> list, watchlistList, favMovieList;
    ActivityFriendProfileBinding bindingg;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        bindingg = ActivityFriendProfileBinding.inflate(getLayoutInflater());
        setContentView(bindingg.getRoot());


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

        followSystem();

        ///////////////////////////////////////////////////////////

        //////////////////////////////////////////////
        /////////// To Display Profile, cover, username
        ////////////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getCoverPhoto()).placeholder(R.drawable.fight).into(bindingg.coverPic);
                    Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.tyrion).into(bindingg.profilePic);
                    bindingg.tvUserName.setText("" + users.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ///////////////////////////////////////
        /////////To display followers count
        /////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(userId).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = (int) snapshot.getChildrenCount();
                    bindingg.followersCountTv.setText("" + count);
                } else {
                    bindingg.followersCountTv.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///////////////////////////////////////////////
        /////////To display Following count
        //////////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(userId).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = (int) snapshot.getChildrenCount();
                    bindingg.followingCountTv.setText("" + count);
                } else {
                    bindingg.followingCountTv.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///////////////////////////////////////////////
        /////////To display Movies count
        //////////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(userId).child("WatchedMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = (int) snapshot.getChildrenCount();
                    bindingg.moviesCountTv.setText("" + count);
                } else {
                    bindingg.moviesCountTv.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ///////////////////////////////////////////////
        /////////To display Movies watch time
        //////////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(userId).child("WatchedMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = (int) snapshot.getChildrenCount();

                    Double watchTime = new Double(count * (1.45));
                    Double truncatedDouble = BigDecimal.valueOf(watchTime)
                            .setScale(1, RoundingMode.HALF_UP)
                            .doubleValue();
                    bindingg.movieWatchTimeTv.setText(truncatedDouble + " hr");
                } else {
                    bindingg.movieWatchTimeTv.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //////////////////////////////////////////////////////////


        ////////////////////////////////////////
        // Followers card onclick
        /////////////////////////////////////
        bindingg.followersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendProfileActivity.this, FollowersActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        /////////////////////////////////////
        // Follwing card onclick
        /////////////////////////////////////
        bindingg.followingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendProfileActivity.this, FollowingActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });


        //////////////////////////////////////////////////////////
        //          Recycler Views
        ///////////////////////////////////////////////////////////
        ///////////////////////////////////////////
        //      Watched Movie RecyclerView
        ////////////////////////////////////////
        database = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId)
                .child("WatchedMovies");
        bindingg.watchedMovieRV.setLayoutManager(new LinearLayoutManager(FriendProfileActivity.this, LinearLayoutManager.HORIZONTAL, false));
        ;

        list = new ArrayList<>();
        movieFirebaseAdapter = new MovieFirebaseAdapter(FriendProfileActivity.this, list);
        bindingg.watchedMovieRV.setAdapter(movieFirebaseAdapter);

        database.orderByChild("addedOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    MovieModel movieModel = datasnapshot.getValue(MovieModel.class);
                    list.add(movieModel);
                }
                Collections.reverse(list);
                movieFirebaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        ///////////////////////////////////////////
        //      Watchlist RecyclerView
        ////////////////////////////////////////
        bindingg.watchlistRV.setLayoutManager(new LinearLayoutManager(FriendProfileActivity.this, LinearLayoutManager.HORIZONTAL, false));
        ;

        watchlistList = new ArrayList<>();
        watchlistAdapter = new MovieFirebaseAdapter(FriendProfileActivity.this, watchlistList);
        bindingg.watchlistRV.setAdapter(watchlistAdapter);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId)
                .child("Watchlist").orderByChild("addedOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchlistList.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
//                    watchlistEmptyContent.setVisibility(View.GONE);
                    MovieModel model = datasnapshot.getValue(MovieModel.class);
                    watchlistList.add(model);
                }
                Collections.reverse(watchlistList);
                watchlistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        ///////////////////////////////////////////
        //      Favourite Movies RecyclerView
        ////////////////////////////////////////
        bindingg.favMovieRv.setLayoutManager(new LinearLayoutManager(FriendProfileActivity.this, LinearLayoutManager.HORIZONTAL, false));

        favMovieList = new ArrayList<>();
        favMovieAdapter = new MovieFirebaseAdapter(FriendProfileActivity.this, favMovieList);
        bindingg.favMovieRv.setAdapter(favMovieAdapter);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId)
                .child("FavouriteMovies").orderByChild("addedOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favMovieList.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    MovieModel model2 = datasnapshot.getValue(MovieModel.class);
                    favMovieList.add(model2);
                }
                Collections.reverse(favMovieList);
                favMovieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        favMovieAdapter.notifyDataSetChanged();
        watchlistAdapter.notifyDataSetChanged();
        movieFirebaseAdapter.notifyDataSetChanged();


        /////////////////////////////////////
        ////    Watched Empty Placeholder
        /////////////////////////////////////
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId)
                .child("WatchedMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    bindingg.watchedEmptyContent.setVisibility(View.GONE);
                } else {
                    bindingg.watchedEmptyContent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /////////////////////////////////////
        ////    Watchlist Empty Placeholder
        /////////////////////////////////////
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId)
                .child("Watchlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    bindingg.watchlistEmptyContent.setVisibility(View.GONE);
                } else {
                    bindingg.watchlistEmptyContent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /////////////////////////////////////
        ////    Favourite Movies Empty Placeholder
        /////////////////////////////////////
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(userId)
                .child("FavouriteMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    bindingg.favEmptyContent.setVisibility(View.GONE);
                } else {
                    bindingg.favEmptyContent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    ///////////////////////////////////////////////
    //  Custome functions
    ////////////////////////////////////////////////

    //follow system
    public void followSystem()
    {
        // if already follows
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("following")
                .child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    bindingg.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(FriendProfileActivity.this,R.drawable.following_btn));
                    bindingg.followBtn.setText("following");
                    bindingg.followBtn.setTextColor(FriendProfileActivity.this.getResources().getColor(R.color.light_blue_600));


                    //  To Unfollow
                    bindingg.followBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("following")
                                    .child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(userId)
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid()).removeValue();
                                    bindingg.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(FriendProfileActivity.this,R.drawable.follow_btn_bg));
                                    bindingg.followBtn.setText("FOLLOW");
                                    bindingg.followBtn.setTextColor(FriendProfileActivity.this.getResources().getColor(R.color.white));                        }
                            });
                        }
                    });
                }
                else
                {
                    bindingg.followBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Follow followModel = new Follow();
                            Follow followingModel = new Follow();
                            followModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            followModel.setFollowedAt(new Date().getTime());
                            followingModel.setFollowedAt(new Date().getTime());

                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(userId)
                                    .child("followers")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .setValue(followModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    bindingg.followBtn.setBackgroundDrawable(ContextCompat.getDrawable(FriendProfileActivity.this,R.drawable.following_btn));
                                    bindingg.followBtn.setText("following");
                                    bindingg.followBtn.setTextColor(FriendProfileActivity.this.getResources().getColor(R.color.light_blue_600));
//                                    holder.binding.followBtn.setEnabled(false);
                                }
                            });

//                            Following
                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("following")
                                    .child(userId)
                                    .setValue(followingModel);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    //////////////////////////////////////////////////
}