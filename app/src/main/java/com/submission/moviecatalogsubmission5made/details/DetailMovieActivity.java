package com.submission.moviecatalogsubmission5made.details;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.submission.moviecatalogsubmission5made.R;
import com.submission.moviecatalogsubmission5made.database.MovieHelper;
import com.submission.moviecatalogsubmission5made.models.MovieItem;
import com.submission.moviecatalogsubmission5made.widget.ImageBannerWidget;
import com.bumptech.glide.Glide;

import java.util.Objects;

import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.BACKDROP;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.KEY_ID;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.POPULARITY;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.POSTER;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.TITLE;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.VOTE;

public class DetailMovieActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";
    private Boolean isFavorite = false;

    private FloatingActionButton btnLike;
    private ProgressBar progressBar;
    private MovieHelper movieHelper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        MovieItem item = getIntent().getParcelableExtra(EXTRA_MOVIE);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(Objects.requireNonNull(item).getTitle());
        }

        progressBar = findViewById(R.id.loading_detail);
        ImageView blurImage = findViewById(R.id.blur_image);
        ImageView poster = findViewById(R.id.poster_image);
        TextView title = findViewById(R.id.tv_name);
        TextView release = findViewById(R.id.tv_release);
        TextView tvReleaseTv = findViewById(R.id.tv_release_tv);
        TextView tvVoteTv = findViewById(R.id.tv_vote_average_tv);
        TextView tvPopularityTv = findViewById(R.id.tv_popularity_tv);
        TextView voteAverage = findViewById(R.id.tv_vote_average);
        TextView popularity = findViewById(R.id.tv_popularity);
        TextView overview = findViewById(R.id.tv_description);

        btnLike = findViewById(R.id.btn_like);

        title.setText(Objects.requireNonNull(item).getTitle());
        release.setText(item.getRelease());
        voteAverage.setText(item.getVoteAverage());
        popularity.setText(item.getPopularity());
        overview.setText(item.getOverview());
        Glide.with(this).load(item.getBackdrop())
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(blurImage);
        Glide.with(this).load(item.getPoster())
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(poster);
        tvReleaseTv.setVisibility(View.VISIBLE);
        tvVoteTv.setVisibility(View.VISIBLE);
        tvPopularityTv.setVisibility(View.VISIBLE);

        btnLike.setVisibility(View.VISIBLE);
        hideLoading();
        loadDataSQLite();
        btnLike.setOnClickListener((v) -> {
            if (isFavorite)
                FavoriteRemove();
            else
                FavoriteSave();

            isFavorite = !isFavorite;
            setFavorite();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieHelper != null)
            movieHelper.close();
    }

    private void setFavorite(){
        int icon;
        if (isFavorite){
            icon = R.drawable.ic_favorite_black_24dp;
        } else {
            icon = R.drawable.ic_favorite_border_black_24dp;
        }

        btnLike.setImageDrawable(
                ContextCompat.getDrawable(getApplicationContext(), icon)
        );
    }

    private void loadDataSQLite(){
        MovieItem movie = getIntent().getParcelableExtra("movie");
        movieHelper = new MovieHelper(this);
        movieHelper.open();

        Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI + "/" + Objects.requireNonNull(movie).getId()),null,
                null,
                null,
                null);

        if (cursor != null){
            if (cursor.moveToFirst())isFavorite=true;
            cursor.close();
        }
        setFavorite();
    }

    private void FavoriteSave(){
        MovieItem movie = getIntent().getParcelableExtra("movie");
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Objects.requireNonNull(movie).getId());
        values.put(TITLE, movie.getTitle());
        values.put(POSTER, movie.getPoster());
        values.put(BACKDROP, movie.getBackdrop());
        values.put(OVERVIEW, movie.getOverview());
        values.put(POPULARITY, movie.getPopularity());
        values.put(VOTE, movie.getVoteAverage());
        values.put(RELEASE_DATE, movie.getRelease());
        getContentResolver().insert(CONTENT_URI, values);
        updateWidget(this);

        Toast.makeText(DetailMovieActivity.this, "Add to favorite", Toast.LENGTH_SHORT).show();
    }

    private void FavoriteRemove(){
        MovieItem movie = getIntent().getParcelableExtra("movie");
        getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + Objects.requireNonNull(movie).getId() ),null,
                null);
        updateWidget(this);

        Toast.makeText(DetailMovieActivity.this, "Remove from favorite", Toast.LENGTH_SHORT).show();
    }

    private static void updateWidget(Context context){
        Intent intent = new Intent(context, ImageBannerWidget.class);
        intent.setAction(ImageBannerWidget.UPDATE_WIDGET);
        context.sendBroadcast(intent);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
