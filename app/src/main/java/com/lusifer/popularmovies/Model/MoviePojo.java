package com.lusifer.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MoviePojo implements Parcelable {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<MovieResult> movieResults = new ArrayList<MovieResult>();
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    /**
     * @return The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<MovieResult> getMovieResults() {
        return movieResults;
    }

    /**
     * @param movieResults The results
     */
    public void setMovieResults(List<MovieResult> movieResults) {
        this.movieResults = movieResults;
    }

    /**
     * @return The totalResults
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The total_results
     */
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * @return The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeTypedList(movieResults);
        dest.writeValue(this.totalResults);
        dest.writeValue(this.totalPages);
    }

    public MoviePojo() {}

    protected MoviePojo(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.movieResults = in.createTypedArrayList(MovieResult.CREATOR);
        this.totalResults = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalPages = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<MoviePojo> CREATOR = new Parcelable.Creator<MoviePojo>() {
        @Override
        public MoviePojo createFromParcel(Parcel source) {return new MoviePojo(source);}

        @Override
        public MoviePojo[] newArray(int size) {return new MoviePojo[size];}
    };
}