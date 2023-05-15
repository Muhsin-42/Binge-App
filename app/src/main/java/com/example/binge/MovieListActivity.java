package com.example.binge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.binge.Models.MovieModel;
import com.example.binge.adapters.MovieRecyclerView;
import com.example.binge.adapters.OnMovieListener;
import com.example.binge.viewmodels.MovieListViewModel;

import java.util.List;


public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    // RecyclerView
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;


    // ViewModel
    private MovieListViewModel movieListViewModel;
    boolean isPopular=true;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        // SearchView
        SetupSearchView();

        recyclerView = findViewById(R.id.recyclerView);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        ObserveAnyChange();
        ObservePopularMovies();
        //getting popular
        movieListViewModel.searchMoviePop(1);
    }

    private void ObservePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observing for any data change
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){
                        // Get the data in log
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }

            }
        });

    }

    // Observing any data change
    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                // Observing for any data change
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){
                        // Get the data in log
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }

    // 5- Intializing recyclerView & adding data to it
    private void ConfigureRecyclerView(){
        // LIve Data can't be passed via the constructor
        movieRecyclerAdapter = new MovieRecyclerView( this);
        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // RecyclerView Pagination
        // Loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    // Here we need to display the next search results on the next page of api
                    movieListViewModel.searchNextpage();
                }
            }
        });
    }
    @Override
    public void onMovieClick(int position) {
       // Toast.makeText(this, "The Position "  +position, Toast.LENGTH_SHORT).show();

        // We don't need position of the movie in recyclerview
        // WE NEED THE ID OF THE MOVIE IN ORDER TO GET ALL IT"S DETAILS

        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {

    }

    // Get data from searchview & query the api to get the results (Movies)
    private void SetupSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        // The search string getted from searchview
                        query,
                        1
                );
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}