package com.example.flixster.models;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flixster.MovieTrailerActivity;
import com.example.flixster.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends AppCompatActivity {

    // movie displayed
    Movie movie;

    // view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;

    // API Connection variables
    // Base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // Parameter name for API Key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieListActivity";
    // Instance fields
    AsyncHttpClient client;
    String keyVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // initialize the view objects
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);

        // unrap parcel with movie passed by intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));




        Log.d("MovieDetailsActivity", String.format("Showing details for '%s", movie.getTitle()));
        //set title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText((movie.getOverview()));
        //calculating vote average
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }

    // Obtain confirmation from API
    public void getVideos(View v){
        // create url
        String url = API_BASE_URL + String.format("/movie/%s/videos", movie.getId());
        // setting request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        client = new AsyncHttpClient();
        // execute a Get request for JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load results into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    JSONObject result = results.getJSONObject(0);
                    keyVal = result.getString("key");

                    if (keyVal != null) {;
                        //create intent for activity
                        Intent intent = new Intent(getApplicationContext(), MovieTrailerActivity.class);
                        // serialize the movie using parceler
                        intent.putExtra("trailerId", keyVal);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });

    }

    // handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        // always log the error
        Log.e(TAG, message, error);
        // alert user to avoid silent errors
        if (alertUser) {
            // show a long toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
