package com.example.binge.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binge.FollowersActivity;
import com.example.binge.FollowingActivity;
import com.example.binge.Models.MovieModel;
import com.example.binge.Models.Users;
import com.example.binge.R;
import com.example.binge.SignInActivity;
import com.example.binge.adapters.MovieFirebaseAdapter;
import com.example.binge.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import com.example.binge.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {


    FirebaseAuth auth;
    ImageView profileImage,changeCoverPic,coverPic,changeDp,logoutIc;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseUsers, followerCountRef;
    FirebaseStorage storage;
    EditText tv_userName;
    TextView tvMovieSeeAll,followersCountTv,followingCountTv, moviesCountTv,movieWatchTimeTv,favEmptyContent,watchlistEmptyContent,watchedEmptyContent,tvEdit;
    CardView followingCard,followersCard,editUsername;
    ActivityResultLauncher<String> activityResultLauncherCover;
    ActivityResultLauncher<String> activityResultLauncherProfile;
    FragmentProfileBinding binding;

    RecyclerView watchedMovieRv,watchlistRv,favMovieRv;
    MovieFirebaseAdapter movieFirebaseAdapter,watchlistAdapter,favMovieAdapter;
    DatabaseReference database;
    ArrayList<MovieModel> list,watchlistList,favMovieList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    Button logoutBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.fragment_profile,container);

//        tv_userName.setEnabled(false);
//        tv_userName.setFocusableInTouchMode(true);

//        binding.tv_userName.setOnClickListener
//        binding.tvUserName.setText("sdf");
//        tv_userName.setText("eee");
        //////////////////////////////////////////////
        /////////// To Display Profile, cover, username
        ////////////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Users users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getCoverPhoto()).placeholder(R.drawable.fight).into(coverPic);
                    Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.tyrion).into(profileImage);
                    tv_userName.setText(""+users.getUsername());
                    tv_userName.setKeyListener(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        
        ///////////////////////////////////////
        /////////To display followers count
        /////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int count = (int) snapshot.getChildrenCount();
                    followersCountTv.setText(""+count);
                }
                else
                {
                    followersCountTv.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///////////////////////////////////////////////
        /////////To display Following count
        //////////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int count = (int) snapshot.getChildrenCount();
                    followingCountTv.setText(""+count);
                }
                else
                {
                    followingCountTv.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///////////////////////////////////////////////
        /////////To display Movies count
        //////////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("WatchedMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int count = (int) snapshot.getChildrenCount();
                    moviesCountTv.setText(""+count);
                }
                else
                {
                    moviesCountTv.setText("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ///////////////////////////////////////////////
        /////////To display Movies watch time
        //////////////////////////////////////////////
        firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("WatchedMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    int count = (int) snapshot.getChildrenCount();

                    Double watchTime = new Double(count*(1.45));
                    Double truncatedDouble = BigDecimal.valueOf(watchTime)
                            .setScale(1, RoundingMode.HALF_UP)
                            .doubleValue();
                    movieWatchTimeTv.setText(truncatedDouble+" hr");
                }
                else
                {
                    movieWatchTimeTv.setText("0");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///////////////////////////////////////////////
        //////// Inflate the layout for this fragment
        /////////////////////////////////////////////
        return inflater.inflate(R.layout.fragment_profile, container, false);
//        return view;
    }
//
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileImage = view.findViewById(R.id.profilePic);
        auth = FirebaseAuth.getInstance();
        auth.getCurrentUser();

        changeDp = view.findViewById(R.id.changeDp);
        changeCoverPic = view.findViewById(R.id.changeCoverPic);
        coverPic = view.findViewById(R.id.coverPic);
        tv_userName = view.findViewById(R.id.tvUserName);
        followersCountTv = view.findViewById(R.id.followersCountTv);
        followingCountTv = view.findViewById(R.id.followingCountTv);
        movieWatchTimeTv = view.findViewById(R.id.movieWatchTimeTv);
        moviesCountTv = view.findViewById(R.id.moviesCountTv);
        watchedMovieRv = view.findViewById(R.id.watchedMovieRV);
        favEmptyContent = view.findViewById(R.id.favEmptyContent);
        watchlistEmptyContent = view.findViewById(R.id.watchlistEmptyContent);
        watchedEmptyContent = view.findViewById(R.id.watchedEmptyContent);
        followingCard = view.findViewById(R.id.followingCard);
        followersCard = view.findViewById(R.id.followersCard);
        editUsername = view.findViewById(R.id.editUsername);
        tvEdit = view.findViewById(R.id.tvEdit);
        logoutIc = view.findViewById(R.id.logoutIc);

        
        ///////////////////////////////////////
        //logout
        /////////////////////////////////////////
        logoutIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), SignInActivity.class);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });        
        
        
        /////////////////////////////////////////
        //  EDIT USERNAME
        ////////////////////////////////////////
        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardText = tvEdit.getText().toString();
                if(cardText.equals("Edit"))
                {
                    tv_userName.setKeyListener(new EditText(getContext()).getKeyListener());
                    editUsername.setCardBackgroundColor(Color.parseColor("#10CE39"));
                    tvEdit.setText("Save");
                    tvEdit.setTextColor(Color.WHITE);
                }
                else
                {
                    tvEdit.setText("Edit");
                    tvEdit.setTextColor(view.getContext().getResources().getColor(R.color.black_overlay));
                    editUsername.setCardBackgroundColor(Color.WHITE);

                    Map newPost = new HashMap();
                    newPost.put("username", tv_userName.getText().toString());


                    FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(newPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            tv_userName.setKeyListener(null);
                            Toast.makeText(getContext(), "Username updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        
        
        ////////////////////////////////////////
        // Followers card onclick
        /////////////////////////////////////
        followersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("userId", FirebaseAuth.getInstance().getUid());
                startActivity(intent);
            }
        });

        /////////////////////////////////////
        // Follwing card onclick
        /////////////////////////////////////
        followingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowingActivity.class);
                intent.putExtra("userId", FirebaseAuth.getInstance().getUid());
                startActivity(intent);
            }
        });


        //////////////////////////////////////////////
        ///////////Change cover pic
        /////////////////////////////////////////////
        activityResultLauncherCover = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                coverPic.setImageURI(uri);
                final StorageReference reference = storage.getReference().child("cover_photo")
                        .child(FirebaseAuth.getInstance().getUid());

                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        });
        changeCoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncherCover.launch("image/*");
            }
        });

        ///////////////////////////////////////////////
        //////////////Change Profile
        ///////////////////////////////////////////////
        activityResultLauncherProfile = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                profileImage.setImageURI(uri);
                final StorageReference reference = storage.getReference().child("profile_photo")
                        .child(FirebaseAuth.getInstance().getUid());

                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("profilepic").setValue(uri.toString());
                            }
                        });
                    }
                });
            }
        });
        changeDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncherProfile.launch("image/*");
            }
        });


        ///////////////////////////////////////////
        //      Watched Movie RecyclerView
        ////////////////////////////////////////
        watchedMovieRv = view.findViewById(R.id.watchedMovieRV);
        database = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("WatchedMovies");
        watchedMovieRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));;

        list = new ArrayList<>();
        movieFirebaseAdapter = new MovieFirebaseAdapter(getContext(),list);
        watchedMovieRv.setAdapter(movieFirebaseAdapter);

        database.orderByChild("addedOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot datasnapshot: snapshot.getChildren())
                {
                    MovieModel movieModel =  datasnapshot.getValue(MovieModel.class);
                    list.add(movieModel);
                }
                Collections.reverse(list);
                movieFirebaseAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });



        ///////////////////////////////////////////
        //      Watchlist RecyclerView
        ////////////////////////////////////////
        watchlistRv = view.findViewById(R.id.watchlistRV);
        watchlistRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));;

        watchlistList = new ArrayList<>();
        watchlistAdapter = new MovieFirebaseAdapter(getContext(),watchlistList);
        watchlistRv.setAdapter(watchlistAdapter);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("Watchlist").orderByChild("addedOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchlistList.clear();
                for(DataSnapshot datasnapshot: snapshot.getChildren())
                {
//                    watchlistEmptyContent.setVisibility(View.GONE);
                    MovieModel model =  datasnapshot.getValue(MovieModel.class);
                    watchlistList.add(model);
                }
                Collections.reverse(watchlistList);
                watchlistAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        ///////////////////////////////////////////
        //      Favourite Movies RecyclerView
        ////////////////////////////////////////
        favMovieRv = view.findViewById(R.id.favMovieRv);
        favMovieRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        favMovieList = new ArrayList<>();
        favMovieAdapter = new MovieFirebaseAdapter(getContext(),favMovieList);
        favMovieRv.setAdapter(favMovieAdapter);

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("FavouriteMovies").orderByChild("addedOn").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favMovieList.clear();
                for(DataSnapshot datasnapshot: snapshot.getChildren())
                {
                    MovieModel model2 =  datasnapshot.getValue(MovieModel.class);
                    favMovieList.add(model2);
                }
                Collections.reverse(favMovieList);
                favMovieAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }


    @Override
    public void onResume()
    {
        super.onResume();
        favMovieAdapter.notifyDataSetChanged();
        watchlistAdapter.notifyDataSetChanged();
        movieFirebaseAdapter.notifyDataSetChanged();


        /////////////////////////////////////
        ////    Watched Empty Placeholder
        /////////////////////////////////////
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("WatchedMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    watchedEmptyContent.setVisibility(View.GONE);
                }
                else
                {
                    watchedEmptyContent.setVisibility(View.VISIBLE);
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
                .child(FirebaseAuth.getInstance().getUid())
                .child("Watchlist").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    watchlistEmptyContent.setVisibility(View.GONE);
                }
                else
                {
                    watchlistEmptyContent.setVisibility(View.VISIBLE);
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
                .child(FirebaseAuth.getInstance().getUid())
                .child("FavouriteMovies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    favEmptyContent.setVisibility(View.GONE);
                }
                else{
                    favEmptyContent.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}