package com.example.flixster;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie {


    // track values which come from API
    private String title;
    private String overview;
    private String posterPath;

    // initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }
}