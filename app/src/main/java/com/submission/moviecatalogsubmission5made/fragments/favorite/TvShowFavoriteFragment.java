package com.submission.moviecatalogsubmission5made.fragments.favorite;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.submission.moviecatalogsubmission5made.R;
import com.submission.moviecatalogsubmission5made.adapter.TvShowFavoriteAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV;

public class TvShowFavoriteFragment extends Fragment {

    private ProgressBar progressBar;
    private TvShowFavoriteAdapter tvShowFavoriteAdapter;
    private Cursor cursor;
    private TextView textViewNotFound;

    public TvShowFavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBarTvShow);
        textViewNotFound = view.findViewById(R.id.textViewNotFound);

        tvShowFavoriteAdapter = new TvShowFavoriteAdapter(getContext());
        tvShowFavoriteAdapter.setCursor(cursor);
        tvShowFavoriteAdapter.notifyDataSetChanged();

        RecyclerView rvTvFavorite = view.findViewById(R.id.rv_fav_tv);
        rvTvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTvFavorite.setAdapter(tvShowFavoriteAdapter);
        rvTvFavorite.setHasFixedSize(true);

        new LoadNoteAsync().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadNoteAsync().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadNoteAsync extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            textViewNotFound.setVisibility(View.GONE);
        }
        @Override
        protected Cursor doInBackground(Void... voids) {
            return Objects.requireNonNull(getContext()).getContentResolver().query(CONTENT_URI_TV, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor favorite) {
            super.onPostExecute(favorite);
            progressBar.setVisibility(View.GONE);

            if(favorite.getCount()<=0){
                textViewNotFound.setVisibility(View.VISIBLE);
            } else {
                textViewNotFound.setVisibility(View.GONE);
            }

            cursor = favorite;
            tvShowFavoriteAdapter.setCursor(cursor);
            tvShowFavoriteAdapter.notifyDataSetChanged();

        }

    }

}
