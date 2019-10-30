package com.submission.moviecatalogsubmission5made.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.submission.moviecatalogsubmission5made.BuildConfig;
import com.submission.moviecatalogsubmission5made.models.TvShowItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class TvShowSearchViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<TvShowItem>> listTvShow = new MutableLiveData<>();
    private String tvName;

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }

    public void setSearchTvShow() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShowItem> tvShowItems = new ArrayList<>();
        String API_KEY = BuildConfig.API_KEY;
        String url = BuildConfig.BASE_URL_SEARCH + "tv?api_key=" + API_KEY + "&language=en-US&query=" + tvName + "&page=1";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray listTv = responseObject.getJSONArray("results");

                    for (int i = 0; i < listTv.length(); i++) {
                        JSONObject tvShow = listTv.getJSONObject(i);
                        TvShowItem tvShowItem = new TvShowItem(tvShow);
                        tvShowItems.add(tvShowItem);
                    }
                    listTvShow.postValue(tvShowItems);
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

    public LiveData<ArrayList<TvShowItem>> getSearchTvShow() {
        return listTvShow;
    }
}
