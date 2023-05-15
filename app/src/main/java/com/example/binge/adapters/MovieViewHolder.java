package com.example.binge.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binge.R;


public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    // Widgets
    TextView title, release_date, duration,toStoreIdOfMovie;
    ImageView imageView;
    RatingBar ratingBar;

    // Click Listener
    OnMovieListener onMovieListener;




    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);


        this.onMovieListener = onMovieListener;

        title = itemView.findViewById(R.id.movie_title);
        release_date = itemView.findViewById(R.id.releaseDate);
        duration = itemView.findViewById(R.id.movieLang);
        toStoreIdOfMovie = itemView.findViewById(R.id.toStoreIdOfMovie);
        imageView = itemView.findViewById(R.id.movie_img);
        ratingBar = itemView.findViewById(R.id.rating_bar);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
