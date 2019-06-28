package com.example.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;
import com.example.flixster.models.MovieDetailsActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    // This class is going to wrap the list of movies

    // List of movies
    ArrayList<Movie> movies;
    // configuration needed for image urls
    Config config;
    // context for rendering
    Context context;

    // initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    // Creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // get context create inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // return a new ViewHolder
        return new ViewHolder(movieView);
    }

    // associates a created or inflated view with a data element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get movie data
        Movie movie = movies.get(position);
        // populate the view
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // determine current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // initialize imageUrl
        String imageUrl = null;

        if (isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            //load the backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        // get the correct placeholder and imageview for orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;


        //load image using glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            itemView.setOnClickListener(this);
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }

        @Override
        public void onClick(View view) {
            // item position
            int position = getAdapterPosition();
            // make sure the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, wont work if static
                Movie movie = movies.get(position);
                //create intent for activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show activity
                context.startActivity(intent);
            }

        }
    }
}
