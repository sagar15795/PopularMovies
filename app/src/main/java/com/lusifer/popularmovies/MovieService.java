package com.lusifer.popularmovies;

import com.lusifer.popularmovies.Model.MoviePojo;
import com.lusifer.popularmovies.Model.VideoPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("/3/movie/{cat}")
    Call<MoviePojo> getMovies(@Path("cat") String cat ,@Query("api_key") String x);

    @GET("/3/movie/{id}/videos")
    Call<VideoPojo> getTrailers(@Path("id") int id , @Query("api_key") String x);



}