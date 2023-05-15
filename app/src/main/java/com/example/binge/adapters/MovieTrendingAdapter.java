package com.example.binge.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.binge.Models.MovieModel;
import com.example.binge.MovieDetails;
import com.example.binge.R;
import com.example.binge.databinding.MovieProfileSampleBinding;

import java.util.ArrayList;

public class MovieTrendingAdapter extends RecyclerView.Adapter<MovieTrendingAdapter.MyViewHolder> {
    Context context;
    ArrayList<MovieModel> list;

    public MovieTrendingAdapter(Context context, ArrayList<MovieModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_profile_sample,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Toast.makeText(context, "sdf", Toast.LENGTH_SHORT).show();
        MovieModel movieModel = list.get(position);
        Glide.with(holder.binding.rivMovie.getContext()).load("https://image.tmdb.org/t/p/w500/" + movieModel.getPoster_path()).into(holder.binding.rivMovie);


        int pst = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MovieDetails.class);
                intent.putExtra("movie", getSelectedMovie(pst));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MovieProfileSampleBinding binding;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = MovieProfileSampleBinding.bind(itemView);
        }
    }

    public MovieModel getSelectedMovie(int position){
        if (list != null){
            if (list.size() > 0){
                return list.get(position);
            }
        }
        return  null;
    }

}
