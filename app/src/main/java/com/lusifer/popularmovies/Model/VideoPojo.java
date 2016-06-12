package com.lusifer.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VideoPojo implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideoResult> results = new ArrayList<VideoResult>();

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public List<VideoResult> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<VideoResult> results) {
        this.results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeList(this.results);
    }

    public VideoPojo() {
    }

    protected VideoPojo(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.results = new ArrayList<VideoResult>();
        in.readList(this.results, VideoResult.class.getClassLoader());
    }

    public static final Parcelable.Creator<VideoPojo> CREATOR = new Parcelable.Creator<VideoPojo>() {
        @Override
        public VideoPojo createFromParcel(Parcel source) {
            return new VideoPojo(source);
        }

        @Override
        public VideoPojo[] newArray(int size) {
            return new VideoPojo[size];
        }
    };
}
