package com.submission.moviecatalogsubmission5made.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.submission.moviecatalogsubmission5made.R;
import com.submission.moviecatalogsubmission5made.models.MovieItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final ArrayList<MovieItem> mData = new ArrayList<>();

    public void setMovieData(ArrayList<MovieItem> itemData) {
        mData.clear();
        mData.addAll(itemData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_film, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imgMovie;
        final TextView tvName, tvRelease, tvDescription;
        final Button btnShare;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
            tvRelease = itemView.findViewById(R.id.tv_item_release);
            tvDescription = itemView.findViewById(R.id.tv_description);
            btnShare = itemView.findViewById(R.id.btn_set_share);
        }

        void bind(MovieItem movieItem) {
            tvName.setText(movieItem.getTitle());
            tvRelease.setText(movieItem.getRelease());
            tvDescription.setText(movieItem.getOverview());
            Glide.with(itemView).load(movieItem.getPoster())
                    .into(imgMovie);
            btnShare.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, movieItem.getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, movieItem.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, movieItem.getTitle() + "\n\n" + movieItem.getOverview());
                btnShare.getContext().startActivity(Intent.createChooser(intent,
                        btnShare.getResources().getString(R.string.share)));
            });
        }
    }

}
