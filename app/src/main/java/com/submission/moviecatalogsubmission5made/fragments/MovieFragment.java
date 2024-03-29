package com.submission.moviecatalogsubmission5made.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.submission.moviecatalogsubmission5made.R;
import com.submission.moviecatalogsubmission5made.adapter.MovieAdapter;
import com.submission.moviecatalogsubmission5made.details.DetailMovieActivity;
import com.submission.moviecatalogsubmission5made.models.MovieItem;
import com.submission.moviecatalogsubmission5made.utils.ItemClickSupport;
import com.submission.moviecatalogsubmission5made.viewmodels.MovieSearchViewModel;
import com.submission.moviecatalogsubmission5made.viewmodels.MovieViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class MovieFragment extends Fragment {

    private MovieAdapter movieAdapter;
    private RecyclerView rvMovie;
    private ProgressBar progressBar;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);

        if(searchItem != null){
            searchView = (SearchView) searchItem.getActionView();
        }
        if(searchView != null){
            searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    progressBar.setVisibility(View.VISIBLE);
                    searchRecylerView(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {
            return false;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MovieViewModel movieViewModel;

        rvMovie = view.findViewById(R.id.rv_movie);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);

        showRecycleCardView(view);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.setMovie();
        movieViewModel.getMovie().observe(this, getMovie);
    }

    private final Observer<ArrayList<MovieItem>> getMovie = new Observer<ArrayList<MovieItem>>() {
        @Override
        public void onChanged(ArrayList<MovieItem> movieData) {
            if (movieData != null) {
                movieAdapter.setMovieData(movieData);
                progressBar.setVisibility(View.GONE);
                ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) ->
                        showSelectedData(movieData.get(position)));
            }
        }
    };

    private void showSelectedData(MovieItem movieData) {
        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieData);
        startActivity(intent);
    }

    private void showRecycleCardView(View view) {
        movieAdapter = new MovieAdapter();
        movieAdapter.notifyDataSetChanged();
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, view.isInLayout()));
        rvMovie.setAdapter(movieAdapter);
    }

    private void searchRecylerView(String s){
        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));

        MovieSearchViewModel movieSearchViewModel = ViewModelProviders.of(this).get(MovieSearchViewModel.class);
        movieSearchViewModel.getSearchMovie().observe(this, getMovieSearch);
        movieSearchViewModel.setMovieName(s);
        movieSearchViewModel.setSearchMovie();
        movieAdapter = new MovieAdapter();
        movieAdapter.notifyDataSetChanged();

        rvMovie.setAdapter(movieAdapter);
    }

    private final Observer<ArrayList<MovieItem>> getMovieSearch = new Observer<ArrayList<MovieItem>>() {
        @Override
        public void onChanged(ArrayList<MovieItem> movies) {
            if(movies != null){
                movieAdapter.setMovieData(movies);
                progressBar.setVisibility(View.GONE);
                ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) ->
                        showSelectedData(movies.get(position)));
            }
        }

    };
}
