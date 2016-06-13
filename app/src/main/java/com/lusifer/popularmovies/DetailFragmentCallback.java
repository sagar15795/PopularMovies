package com.lusifer.popularmovies;

import com.lusifer.popularmovies.Model.MovieResult;

public interface DetailFragmentCallback {
    /**
     * DetailFragmentCallback for when an item has been selected.
     * @param movie the MovieItem selected.
     */
    void onItemSelected(MovieResult movie);
}