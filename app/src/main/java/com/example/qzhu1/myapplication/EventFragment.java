package com.example.qzhu1.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;

public class EventFragment extends Fragment {
    // Store instance variables
    private static final String ARG_MOVIE = "movie";
    private HashMap moive;
    ShareActionProvider mShareActionProvider;

    // newInstance constructor for creating fragment with arguments
    public static EventFragment newInstance(HashMap<String, ?> movie) {
        EventFragment fragmentFirst = new EventFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            moive = (HashMap<String, ?>) getArguments().getSerializable(ARG_MOVIE);
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        final TextView name = (TextView) view.findViewById(R.id.movietitle_year);
        final TextView description = (TextView) view.findViewById(R.id.description_stars);
        final ImageView image = (ImageView) view.findViewById(R.id.moiveimg);
        final RatingBar rating = (RatingBar) view.findViewById(R.id.rating);
        final TextView ratingnum = (TextView) view.findViewById(R.id.rating_num);

        name.setText((String) moive.get("name") + " (" + (String) moive.get("year") + ")");
        description.setText("Description: " + (String) moive.get("description") + "\n\nStars: " + (String) moive.get("stars"));
        image.setImageResource((Integer) moive.get("image"));
        Double ratingNumber = (Double) moive.get("rating");
        ratingnum.setText("Rating: " + ratingNumber);
        rating.setRating(ratingNumber.floatValue() / 2);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.findItem(R.id.menu_provider)==null)
            inflater.inflate(R.menu.actionbar_provider,menu);

        MenuItem shareItem = menu.findItem(R.id.menu_provider);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("text/plain");
        intentShare.putExtra(Intent.EXTRA_TEXT, (String) moive.get("name"));
        mShareActionProvider.setShareIntent(intentShare);

        super.onCreateOptionsMenu(menu, inflater);
    }
}