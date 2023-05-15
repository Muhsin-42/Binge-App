package com.example.binge.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.binge.Fragment.MovieDiscoverFragment;
import com.example.binge.MainActivity;
import com.example.binge.Models.MovieModel;
import com.example.binge.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.core.Tag;


import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent,false);
        return new MovieViewHolder(view, onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        ((MovieViewHolder)holder).title.setText(mMovies.get(i).getTitle());
        ((MovieViewHolder)holder).release_date.setText(mMovies.get(i).getRelease_date());

        ((MovieViewHolder)holder).duration.setText(mMovies.get(i).getOriginal_language());

        // vote average is over 10, and our rating bar is over 5 stars: dividing by 2
        ((MovieViewHolder)holder).ratingBar.setRating((mMovies.get(i).getVote_average())/2);


        // ImageView: Using Glide Library
        Glide.with(holder.itemView.getContext())
                .load( "https://image.tmdb.org/t/p/w500/"
                        +mMovies.get(i).getPoster_path())
                .into(((MovieViewHolder)holder).imageView);



    }

    @Override
    public int getItemCount() {
        if (mMovies != null){
            return mMovies.size();
        }
        return 0;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }


    // Getting the id of the movie clicked
    public MovieModel getSelectedMovie(int position){
        if (mMovies != null){
            if (mMovies.size() > 0){

                return mMovies.get(position);
            }
        }
        return  null;
    }

}
