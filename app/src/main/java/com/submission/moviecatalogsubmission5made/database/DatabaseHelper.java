package com.submission.moviecatalogsubmission5made.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.BACKDROP;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.KEY_ID;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.POPULARITY;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.POSTER;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.RELEASE_DATE;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.TABLE_NAME;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.TITLE;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.MovieColumns.VOTE;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.BACKDROP_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.KEY_ID_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.OVERVIEW_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.POPULARITY_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.POSTER_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.RELEASE_DATE_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.TABLE_NAME_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.TITLE_TV;
import static com.submission.moviecatalogsubmission5made.database.DatabaseContract.TvShowColumns.VOTE_TV;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbmoviecatalogue";

    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MOVIE="create table " + TABLE_NAME + " ( " +
                KEY_ID + " integer primary key autoincrement, " +
                TITLE + " text not null, " +
                OVERVIEW + " text not null, " +
                POSTER + " text not null, " +
                BACKDROP + " text not null, " +
                POPULARITY + " text not null, " +
                RELEASE_DATE + " text not null, " +
                VOTE + " text not null " +
                " ); " ;
        db.execSQL(CREATE_TABLE_MOVIE);

        String CREATE_TABLE_TV ="create table " + TABLE_NAME_TV + " ( " +
                KEY_ID_TV + " integer primary key autoincrement, " +
                TITLE_TV + " text not null, " +
                OVERVIEW_TV + " text not null, " +
                POSTER_TV + " text not null, " +
                BACKDROP_TV + " text not null, " +
                POPULARITY_TV + " text not null, " +
                RELEASE_DATE_TV + " text not null, " +
                VOTE_TV + " text not null " +
                " ); " ;
        db.execSQL(CREATE_TABLE_TV);
    }

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_TV);
        onCreate(db);
    }
}
