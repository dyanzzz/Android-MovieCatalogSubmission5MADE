package com.submission.moviecatalogsubmission5made.details;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import com.submission.moviecatalogsubmission5made.database.TvHelper;
import com.submission.moviecatalogsubmission5made.models.TvShowItem;
import com.bumptech.glide.Glide;

import java.util.Objects;

import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.BACKDROP_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.CONTENT_URI_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.KEY_ID_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.OVERVIEW_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.POPULARITY_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.POSTER_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.RELEASE_DATE_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.TITLE_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.VOTE_TV;

public class DetailTvShowActivity extends AppCompatActivity {

    public static final String EXTRA_TVSHOW = "tvshow";
    private Boolean isFavorite = false;

    private FloatingActionButton btnLike;
    private ProgressBar progressBar;
    private TvHelper tvHelper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv_show);

        TvShowItem item = getIntent().getParcelableExtra(EXTRA_TVSHOW);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(Objects.requireNonNull(item).getName());
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

        title.setText(Objects.requireNonNull(item).getName());
        release.setText(item.getFirstAirDate());
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
        if (tvHelper != null)
            tvHelper.close();
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
        TvShowItem Isi = getIntent().getParcelableExtra("tvshow");
        tvHelper = new TvHelper(this);
        tvHelper.open();

        Cursor cursor = getContentResolver().query(Uri.parse(CONTENT_URI_TV + "/" + Objects.requireNonNull(Isi).getId()),null,
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
        TvShowItem Isi = getIntent().getParcelableExtra("tvshow");
        ContentValues values = new ContentValues();
        values.put(KEY_ID_TV, Objects.requireNonNull(Isi).getId());
        values.put(TITLE_TV, Isi.getName());
        values.put(POSTER_TV, Isi.getPoster());
        values.put(BACKDROP_TV, Isi.getBackdrop());
        values.put(OVERVIEW_TV, Isi.getOverview());
        values.put(POPULARITY_TV, Isi.getPopularity());
        values.put(VOTE_TV, Isi.getVoteAverage());
        values.put(RELEASE_DATE_TV, Isi.getFirstAirDate());
        getContentResolver().insert(CONTENT_URI_TV, values);

        Toast.makeText(DetailTvShowActivity.this, "Add to favorite", Toast.LENGTH_SHORT).show();
    }

    private void FavoriteRemove(){
        TvShowItem Isi = getIntent().getParcelableExtra("tvshow");
        getContentResolver().delete(Uri.parse(CONTENT_URI_TV + "/" + Objects.requireNonNull(Isi).getId() ),null,
                null);

        Toast.makeText(DetailTvShowActivity.this, "Remove from favorite", Toast.LENGTH_SHORT).show();
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }
}
