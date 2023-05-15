package com.example.binge.viewmodels;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

//import net.androidsquad.movieapp.models.MovieModel;
//import net.androidsquad.movieapp.repositories.MovieRepository;

import com.example.binge.Models.MovieModel;
import com.example.binge.repositories.MovieRepository;

import java.util.List;

    // this class is used for VIEWMODEL
public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;

    // Constructor
    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getPop(){
            return movieRepository.getPop();
        }

    // 3- Calling method in view-model
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.serachMovieApi(query, pageNumber);
    }
    public void searchMoviePop(int pageNumber){
        movieRepository.searchMoviePop( pageNumber);
    }

    public void searchNextpage(){
        movieRepository.searchNextPage();
    }
}
