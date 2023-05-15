package com.example.binge.request;



import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.binge.AppExecutors;
import com.example.binge.Models.MovieModel;
import com.example.binge.request.MovieApiClient.RetrieveMoviesRunnable;
import com.example.binge.response.MovieSearchResponse;
import com.example.binge.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    // LiveData for search
    private MutableLiveData<List<MovieModel>> mMovies ;
    private static MovieApiClient instance;

    // making Global RUNNABLE
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    //Live data for popular movies
    private MutableLiveData<List<MovieModel>>   mMoviesPop;

    //making popular runnable
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;



    public static MovieApiClient getInstance(){
        if (instance == null){
            instance = new MovieApiClient();
        }
        return  instance;

    }



    private MovieApiClient(){
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPop(){
        return mMoviesPop;
    }






    //1- This method that we are going to call through the classes
    public void searchMoviesApi(String query,int pageNumber) {

        if (retrieveMoviesRunnable != null){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler.cancel(true);

            }
        }, 3000, TimeUnit.MILLISECONDS);
    }

    public void searchMoviesPop(int pageNumber) {

        if (retrieveMoviesRunnablePop != null){
            retrieveMoviesRunnablePop = null;
        }

        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnablePop);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler2.cancel(true);

            }
        }, 1000, TimeUnit.MILLISECONDS);
    }




    // Retreiving data from REstAPI by runnable class
    // WE have 2 types of Queries : the ID & search Queries
    class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;


        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects

            try{

                Response response = getMovies(query, pageNumber).execute();

                if (cancelRequest) {

                    return;
                }
                if (response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
//                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1){
                        // Sending data to live data
                        // PostValue: used for background thread
                        // setValue: not for background thread
                        mMovies.postValue(list);

                    }else{
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);

                    }

                }else{
                    String error = response.errorBody().string();
                    Log.v("Tagy", "Error " +error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);

            }
        }

        // Search Method/ query
        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
                    return Servicey.getMovieApi().searchMovie(
                            Credentials.API_KEY,
                            query,
                            pageNumber

                    );

        }
        private void cancelRequest(){
            Log.v("Tag", "Cancelling Search Request" );
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnablePop implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;


        public RetrieveMoviesRunnablePop( int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects

            try{

                Response response2 = getPop( pageNumber).execute();

                if (cancelRequest) {

                    return;
                }
                if (response2.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
//                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1){
                        // Sending data to live data
                        // PostValue: used for background thread
                        // setValue: not for background thread
                        mMoviesPop.postValue(list);

                    }else{
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }

                }else{
                    String error = response2.errorBody().string();
                    Log.v("Tagy", "Error " +error);
                    mMoviesPop.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }
        }

        // Search Method/ query
        private Call<MovieSearchResponse> getPop( int pageNumber){
            return Servicey.getMovieApi().getTrendingMovie(
                    Credentials.API_KEY,
                    pageNumber
            );

        }
        private void cancelRequest(){
            Log.v("Tag", "Cancelling Search Request" );
            cancelRequest = true;
        }
    }
}
