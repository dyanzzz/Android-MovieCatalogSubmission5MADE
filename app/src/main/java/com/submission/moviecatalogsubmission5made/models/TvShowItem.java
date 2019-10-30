package com.submission.moviecatalogsubmission5made.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.submission.moviecatalogsubmission5made.BuildConfig;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.Objects;

import static android.provider.BaseColumns._ID;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.BACKDROP_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.OVERVIEW_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.POPULARITY_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.POSTER_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.RELEASE_DATE_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.TITLE_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.VOTE_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.getColumnInt;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.getColumnString;

public class TvShowItem implements Parcelable {

    private int id;
    private String poster, name, firstAirDate, voteAverage;
    private String popularity;
    private String overview, backdrop;

    public int getId() {
        return id;
    }

    public String getPoster() {
        return poster;
    }

    public String getName() {
        return name;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public TvShowItem (JSONObject object) {

        try {
            this.id = object.getInt("id");
            String image = object.getString("poster_path");
            String backdrop = object.getString("backdrop_path");
            this.poster = BuildConfig.BASE_URL_IMG + image;
            this.backdrop = BuildConfig.BASE_URL_IMG + backdrop;
            this.name = object.getString("name");
            this.firstAirDate = object.getString("first_air_date");
            this.voteAverage = object.getString("vote_average");
            this.popularity = object.getString("popularity");
            this.overview = object.getString("overview");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error Data", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.poster);
        dest.writeString(this.backdrop);
        dest.writeString(this.name);
        dest.writeString(this.firstAirDate);
        dest.writeString(this.voteAverage);
        dest.writeString(this.popularity);
        dest.writeString(this.overview);
    }

    private TvShowItem(Parcel in) {
        this.id = in.readInt();
        this.poster = in.readString();
        this.backdrop = in.readString();
        this.name = in.readString();
        this.firstAirDate = in.readString();
        this.voteAverage = in.readString();
        this.popularity = in.readString();
        this.overview = in.readString();
    }

    public static final Creator<TvShowItem> CREATOR = new Creator<TvShowItem>() {
        @Override
        public TvShowItem createFromParcel(Parcel source) {
            return new TvShowItem(source);
        }

        @Override
        public TvShowItem[] newArray(int size) {
            return new TvShowItem[size];
        }
    };

    @NotNull
    @Override
    public String toString() {
        return "MovieItem{" +
                "id = '" + id + "" +
                ",poster_path = '" + poster + "" +
                ",backdrop_path = '" + backdrop + "" +
                ",name = '" + name + "" +
                ",first_air_date = '" + firstAirDate + "" +
                ",vote_average = '" + voteAverage + "" +
                ",popularity = '" + popularity + "" +
                ",overview = '" + overview + "" +
                "}";
    }

    public TvShowItem (Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.poster = getColumnString(cursor, POSTER_TV);
        this.backdrop = getColumnString(cursor, BACKDROP_TV);
        this.name = getColumnString(cursor, TITLE_TV);
        this.firstAirDate = getColumnString(cursor, RELEASE_DATE_TV);
        this.voteAverage = getColumnString(cursor, VOTE_TV);
        this.popularity = getColumnString(cursor, POPULARITY_TV);
        this.overview = getColumnString(cursor, OVERVIEW_TV);
    }
}
