package com.submission.moviecatalogsubmission5made.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.submission.moviecatalogsubmission5made.BuildConfig;
import com.submission.moviecatalogsubmission5made.models.MovieItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<MovieItem>> listMovie = new MutableLiveData<>();

    public void setMovie() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieItem> listMovieItem = new ArrayList<>();
        String API_KEY = BuildConfig.API_KEY;
        String url = BuildConfig.BASE_URL + "discover/movie?api_key=" + API_KEY + "&language=en-US&page=1";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray listMv = responseObject.getJSONArray("results");

                    for (int i = 0; i < listMv.length(); i++) {
                        JSONObject movie = listMv.getJSONObject(i);
                        MovieItem movieItem = new MovieItem(movie);
                        listMovieItem.add(movieItem);
                    }
                    listMovie.postValue(listMovieItem);
                } catch (Exception e) {
                    Log.d("Exception", Objects.requireNonNull(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Failure", Objects.requireNonNull(error.getMessage()));
            }
        });
    }

    public LiveData<ArrayList<MovieItem>> getMovie() {
        return listMovie;
    }
}
