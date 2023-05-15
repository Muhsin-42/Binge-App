package com.example.binge.repositories;

import androidx.lifecycle.LiveData;

import com.example.binge.Models.MovieModel;
import com.example.binge.request.MovieApiClient;


import java.util.List;

// This class is acting as repositories
public class MovieRepository {

    private static MovieRepository instance;

    private MovieApiClient movieApiClient;

    private String mQuery;
    private int mPageNumber;

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository(){
        movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }
    public LiveData<List<MovieModel>> getPop(){
        return movieApiClient.getMoviesPop();
    }

    // 2- Calling the method in repository
    public void serachMovieApi(String query, int pageNumber){
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(query, pageNumber);
    }
    public void searchMoviePop(int pageNumber){
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesPop( pageNumber);
    }

    public void searchNextPage(){
        serachMovieApi(mQuery, mPageNumber+1);
    }
}




