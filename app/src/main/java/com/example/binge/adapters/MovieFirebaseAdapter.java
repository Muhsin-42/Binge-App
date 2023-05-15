package com.example.binge.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.binge.Models.MovieModel;
import com.example.binge.MovieDetails;
import com.example.binge.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class MovieFirebaseAdapter extends RecyclerView.Adapter<MovieFirebaseAdapter.MyViewHolder>{

    Context context;
    ArrayList<MovieModel> list;


    public MovieFirebaseAdapter(Context context, ArrayList<MovieModel> list) {
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
        MovieModel movieModel = list.get(position);
        Glide.with(holder.riv_movie.getContext()).load("https://image.tmdb.org/t/p/w500/"+movieModel.getPoster_path()).into(holder.riv_movie);

        int pst =position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MovieDetails.class);
                intent.putExtra("movie", getSelectedMovie(pst));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView riv_movie;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            riv_movie = itemView.findViewById(R.id.riv_movie);
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