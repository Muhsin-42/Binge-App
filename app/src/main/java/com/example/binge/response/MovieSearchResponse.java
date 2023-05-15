package com.example.binge.response;

import com.example.binge.Models.MovieModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//import net.androidsquad.movieapp.models.MovieModel;

import java.util.List;

// This class is for getting multiple movies (Movies list) - popular movies
public class MovieSearchResponse {
    @SerializedName("total_results")
    @Expose()
    private int total_count;


    @SerializedName("results")
    @Expose()
    private List<MovieModel> movies;


    public int getTotal_count(){
        return total_count;
    }


    public List<MovieModel> getMovies(){
        return movies;
    }

    @Override
    public String toString() {
        return "MovieSearchResponse{" +
                "total_count=" + total_count +
                ", movies=" + movies +
                '}';
    }
}
