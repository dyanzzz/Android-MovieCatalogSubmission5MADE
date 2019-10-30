package com.submission.moviecatalogsubmission5made.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.submission.moviecatalogsubmission5made.R;
import com.submission.moviecatalogsubmission5made.details.DetailMovieActivity;
import com.submission.moviecatalogsubmission5made.models.MovieItem;
import com.bumptech.glide.Glide;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.CardViewViewHolder> {

    private final Context context;
    private Cursor cursor;

    public MovieFavoriteAdapter(Context context) {
        this.context = context;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_film, viewGroup, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder cardViewViewHolder, int i) {

        MovieItem movieItem = getItem(i);

        cardViewViewHolder.tvName.setText(movieItem.getTitle());
        cardViewViewHolder.tvRelease.setText(movieItem.getRelease());
        cardViewViewHolder.tvDescription.setText(movieItem.getOverview());
        Glide.with(context).load(movieItem.getPoster())
                .into(cardViewViewHolder.imgMovie);

        cardViewViewHolder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movieItem);
            context.startActivity(intent);
        });

        cardViewViewHolder.btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, movieItem.getTitle());
            intent.putExtra(Intent.EXTRA_SUBJECT, movieItem.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, movieItem.getTitle() + "\n\n" + movieItem.getOverview());
            cardViewViewHolder.btnShare.getContext().startActivity(Intent.createChooser(intent,
                    cardViewViewHolder.btnShare.getResources().getString(R.string.share)));
        });

        Log.d("TITLE", ""+movieItem.getTitle());

    }

    private MovieItem getItem(int i) {
        if (!cursor.moveToPosition(i)) {
            throw new IllegalStateException("Position invalid");
        }
        return new MovieItem(cursor);
    }

    @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;
        return cursor.getCount();
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {

        final ImageView imgMovie;
        final TextView tvName, tvRelease, tvDescription;
        final Button btnShare;

        CardViewViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMovie = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvRelease = itemView.findViewById(R.id.tv_item_release);
            tvDescription = itemView.findViewById(R.id.tv_description);
            btnShare = itemView.findViewById(R.id.btn_set_share);
        }
    }
}
