package com.example.binge.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.binge.Models.MovieModel;
import com.example.binge.MovieDetails;
import com.example.binge.R;
import com.example.binge.adapters.MovieRecyclerView;
import com.example.binge.adapters.OnMovieListener;
import com.example.binge.viewmodels.MovieListViewModel;




import java.util.List;

//The Tt1 class is used to extend Fragment and AppCopactaActivity on DiscoverFragment class
class Tt1 extends Fragment{
    class Tt2 extends AppCompatActivity{
        //
    }
}
public class MovieDiscoverFragment extends Tt1 implements OnMovieListener {

    // RecyclerView
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;

    // ViewModel
    private MovieListViewModel movieListViewModel;
    TextView preMovieSearchTV;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        preMovieSearchTV = view.findViewById(R.id.preMovieSearchTV);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Searching for movie");
        progressDialog.setMessage("Wait a moment...");
        // SearchView
        SetupSearchView(view);

        recyclerView = view.findViewById(R.id.recyclerView);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        ObserveAnyChange();
    }

    // Observing any data change
    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(getActivity(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                progressDialog.dismiss();
                // Observing for any data change
                if (movieModels != null){
                    preMovieSearchTV.setVisibility(View.GONE);
                    for (MovieModel movieModel: movieModels){
                        // Get the data in log
                        movieRecyclerAdapter.setmMovies(movieModels);

                    }
                }
                else {
                    Log.v("tktk","helllooooooooooooo");
                    Toast.makeText(getActivity(), "No Movies found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 5- Intializing recyclerView & adding data to it
    private void ConfigureRecyclerView(){
        // LIve Data can't be passed via the constructor
        movieRecyclerAdapter = new MovieRecyclerView( this);

        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


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
        // We don't need position of the movie in recyclerview
        // WE NEED THE ID OF THE MOVIE IN ORDER TO GET ALL IT"S DETAILS

        Intent intent = new Intent(getActivity(), MovieDetails.class);

        intent.putExtra("movie", movieRecyclerAdapter.getSelectedMovie(position));


        startActivity(intent);
//        Toast.makeText(getActivity(), "sdf"+id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(String category) {

    }

    // Get data from searchview & query the api to get the results (Movies)
    private void SetupSearchView(View view) {
        final SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.show();
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