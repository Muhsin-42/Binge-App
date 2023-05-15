package com.example.binge;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.binge.Models.CommentModel;
import com.example.binge.Models.MovieModel;
import com.example.binge.adapters.CommentsAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MovieDetails extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseDatabase database;

    // Widgets
    private ImageView imageViewDetails,favouriteIc,watchedIcon,watchlistIcon,goToCommentIc;
    private TextView titleDetails, descDetails,releaseDate,noCommentTv;
    private RatingBar ratingBarDetails;
    private List<MovieModel> mMovies;
    String movieId,movieTitle,movieRating,movieRelease,movieDescription;
    Button tmpBtn;

    RecyclerView commentsRv;
    ArrayList<CommentModel> commentModelList=new ArrayList<>();



    MovieModel movieModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();

        imageViewDetails = findViewById(R.id.imageView_details);
        goToCommentIc = findViewById(R.id.goToCommentIc);
        titleDetails = findViewById(R.id.textView_title_details);
        releaseDate = findViewById(R.id.release_date);
        descDetails = findViewById(R.id.textView_desc_details);
        ratingBarDetails = findViewById(R.id.ratingBar_details);
        favouriteIc = findViewById(R.id.favouriteIc);
        watchedIcon = findViewById(R.id.watchedIcon);
        watchlistIcon = findViewById(R.id.watchlistIcon);
        commentsRv = findViewById(R.id.commentsRv);
        noCommentTv = findViewById(R.id.noCommentTv);
        tmpBtn = findViewById(R.id.tmpBtn);

        GetDataFromIntent();


        ////////////////////////////////
        ///     Go to comment Activity
        ///////////////////////////////
        goToCommentIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetails.this,WriteCommentActivity.class);
                intent.putExtra("movie", movieModel);
                startActivity(intent);
            }
        });


        //////////////////////////////////////////////////
        //////   Watched Movies
        //////////////////////////////////////////////////

        // IF Movie is Watched ==>
            // 1) MAKE The checked mark green
            // 2) Make the Wishlist icon invisible
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("WatchedMovies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(movieId))
                {
                    watchlistIcon.setVisibility(INVISIBLE);
                    favouriteIc.setVisibility(VISIBLE);
                    watchedIcon.setImageResource(R.drawable.ic_check_on);
                }
                else{
                    favouriteIc.setVisibility(INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //When Watched icon is clicked
        // 1) add the movie details to the firebase watchedMOvies table
        // 2) Make the icon green
        // 3) make Watchlist icon invisible
        watchedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("WatchedMovies").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(movieId))
                        {
                            //Unwatch movie
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("WatchedMovies")
                                    .child(movieId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    watchlistIcon.setVisibility(VISIBLE);
                                    favouriteIc.setVisibility(INVISIBLE);
                                    watchedIcon.setImageResource(R.drawable.ic_check_off);
                                }
                            });

                            //Remove from favourite
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("FavouriteMovies")
                                    .child(movieId).removeValue();
                        }
                        else{
                            // add movie to the database

                            MovieModel movieModel = getIntent().getParcelableExtra("movie");
                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("WatchedMovies")
                                    .child(movieId)
                                    .setValue(movieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("WatchedMovies")
                                            .child(movieId)
                                            .child("addedOn").setValue(new Date().getTime());
                                    watchlistIcon.setVisibility(INVISIBLE);
                                    favouriteIc.setVisibility(VISIBLE);
                                    watchedIcon.setImageResource(R.drawable.ic_check_on);
                                }
                            });

                            //Remove movie from watchlist
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("Watchlist")
                                    .child(movieId).removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        //////////////////////////////////////////////////
        //////   Watchlist Movies
        //////////////////////////////////////////////////

        //if movie is on watchlist
            // Change the watchlist icon to filled
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("Watchlist").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(movieId))
                {
                    watchlistIcon.setImageResource(R.drawable.ic_wishlist_added);
                }
                else{
//                    Toast.makeText(MovieDetails.this, "Doesnt Exists wl "+movieId, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Wishlist clicked
        watchlistIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("Watchlist").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(movieId))
                        {
                            //Remove from watchlist
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("Watchlist")
                                    .child(movieId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    watchlistIcon.setImageResource(R.drawable.ic_wishlist_add);
                                }
                            });

                        }
                        else{
                            MovieModel movieModel = getIntent().getParcelableExtra("movie");
                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("Watchlist")
                                    .child(movieId)
                                    .setValue(movieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("Watchlist")
                                            .child(movieId)
                                            .child("addedOn").setValue(new Date().getTime());
                                    watchlistIcon.setImageResource(R.drawable.ic_wishlist_added);
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



        //////////////////////////////////////////////////
        //////   Favourite Movies
        //////////////////////////////////////////////////
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("FavouriteMovies").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(movieId))
                {
                    favouriteIc.setImageResource(R.drawable.ic_favorite_filled);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        favouriteIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("FavouriteMovies").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(movieId))
                        {
                            //Remove from favourite
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("FavouriteMovies")
                                    .child(movieId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    favouriteIc.setImageResource(R.drawable.ic_favourite_outlined);
                                }
                            });

                        }
                        else{
                            //Add movie to favourite list
                            MovieModel movieModel = getIntent().getParcelableExtra("movie");
                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("FavouriteMovies")
                                    .child(movieId)
                                    .setValue(movieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //ADD TIMESTAMP
                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("FavouriteMovies")
                                            .child(movieId)
                                            .child("addedOn").setValue(new Date().getTime());
                                    favouriteIc.setImageResource(R.drawable.ic_favorite_filled);
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

        ///////////////////////////////////////////////
        //  Comments RecyclerView
        //////////////////////////////////////////////
        CommentsAdapter commentsAdapter = new CommentsAdapter(MovieDetails.this,commentModelList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MovieDetails.this);
        commentsRv = findViewById(R.id.commentsRv);
        commentsRv.setLayoutManager(layoutManager);
        commentsRv.setAdapter(commentsAdapter);

        FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(movieId).orderByChild("commentAt").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentModelList.clear();
                if(snapshot.exists())
                {
                    noCommentTv.setVisibility(View.GONE);
                }
                for(DataSnapshot datasnapshot: snapshot.getChildren())
                {
                    CommentModel commentModel =  datasnapshot.getValue(CommentModel.class);
//                    Toast.makeText(MovieDetails.this, "sdf = "+datasnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    commentModel.setUniqueKey(datasnapshot.getKey());
                    commentModelList.add(commentModel);
                }
                Collections.reverse(commentModelList);
                commentsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });



        /////////////////////////////////////////////////
        //  Temporary thing to add trending movies into database
        /////////////////////////////////////////////////
//        tmpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase.getInstance().getReference().child("TrendingMovies")
//                       .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                            //Add movie to favourite list
//                            MovieModel movieModel = getIntent().getParcelableExtra("movie");
//                            FirebaseDatabase.getInstance().getReference().child("TrendingMovies")
//                                    .child(movieId)
//                                    .setValue(movieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    //ADD TIMESTAMP
//                                    Toast.makeText(MovieDetails.this, "bhai movie added", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });
        tmpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieModel movieModel = getIntent().getParcelableExtra("movie");
                FirebaseDatabase.getInstance().getReference().child("TrendingMovies")
                        .child(movieId)
                        .setValue(movieModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //ADD TIMESTAMP
                        FirebaseDatabase.getInstance().getReference().child("TrendingMovies")
                                .child(movieId)
                                .child("addedOn").setValue(new Date().getTime());
                        Toast.makeText(MovieDetails.this, "bhai movie added", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
    private void GetDataFromIntent() {
        if (getIntent().hasExtra("movie")){
             movieModel = getIntent().getParcelableExtra("movie");

            movieId = Integer.toString(movieModel.getMovie_id());
            movieTitle = movieModel.getTitle();
            movieRelease = movieModel.getRelease_date();
            movieDescription = movieModel.getMovie_overview();
            movieRating = Float.toString(movieModel.getVote_average());

            titleDetails.setText(movieModel.getTitle());
            releaseDate.setText(movieModel.getRelease_date());
            descDetails.setText(movieModel.getMovie_overview());
            ratingBarDetails.setRating((movieModel.getVote_average())/2);
            Glide.with(this).load("https://image.tmdb.org/t/p/w500/"+movieModel.getPoster_path()).into(imageViewDetails);
        }
    }
}