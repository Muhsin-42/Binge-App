package com.example.binge.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.binge.Models.MovieModel;
import com.example.binge.R;

import java.util.List;

public class PopularMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    private static final int DISPLAY_POP=1;
    private static final int DISPLAY_SEARCH=2;

    public PopularMoviesAdapter(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent,false);
//        return new MovieViewHolder(view, onMovieListener);

        View view = null;
        if(viewType==DISPLAY_SEARCH)
        {
            view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent,false);
            return new MovieViewHolder(view, onMovieListener);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_profile_sample, parent,false);
            return new Popular_View_Holder(view, onMovieListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        int itemViewType = getItemViewType(i);
        if(itemViewType == DISPLAY_SEARCH)
        {
            // ImageView: Using Glide Library
            Glide.with(holder.itemView.getContext())
                    .load( "https://image.tmdb.org/t/p/w500/"
                            +mMovies.get(i).getPoster_path())
                    .into(((MovieViewHolder)holder).imageView);

        }
        else
        {
            // ImageView: Using Glide Library
            Glide.with(holder.itemView.getContext())
                    .load( "https://image.tmdb.org/t/p/w500/"
                            +mMovies.get(i).getPoster_path())
                    .into(((MovieViewHolder)holder).imageView);

        }




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
