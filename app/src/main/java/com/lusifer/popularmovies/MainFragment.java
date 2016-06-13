package com.lusifer.popularmovies;

import com.lusifer.popularmovies.Model.MoviePojo;
import com.lusifer.popularmovies.Model.MovieResult;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private ArrayList<String> imageUrlList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridRecyclerAdapter mAdapter;
    private ArrayList<MovieResult> movieResultArrayList = new ArrayList<>();
    private Call<MoviePojo> moviePojoCall;
    private RestAPIClient restClient;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        prepareMovieData();
        mAdapter = new GridRecyclerAdapter(imageUrlList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        restClient = new RestAPIClient();

        getMovieDetail();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                MovieResult movieResult = movieResultArrayList.get(position);
                ((DetailFragmentCallback) getActivity()).onItemSelected(movieResult);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        imageUrlList.clear();
        mAdapter.notifyDataSetChanged();

        getMovieDetail();
    }

    private void getMovieDetail() {

        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String unitType = sharedPrefs.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_popular));

        moviePojoCall = restClient.getMovieService().getMovies(unitType, getString(R.string.api_key));

        moviePojoCall.enqueue(new Callback<MoviePojo>() {
            @Override
            public void onResponse(Call<MoviePojo> call, Response<MoviePojo> response) {
                movieResultArrayList.clear();
                for (int i = 0; i < response.body().getMovieResults().size(); i++) {
                    movieResultArrayList.add(response.body().getMovieResults().get(i));
                }
                prepareMovieData();
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MoviePojo> call, Throwable t) {

                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void prepareMovieData() {
        imageUrlList.clear();
        for (int i = 0; i < movieResultArrayList.size(); i++) {
            imageUrlList.add("http://image.tmdb.org/t/p/w500" + movieResultArrayList.get(i).getPosterPath());
        }
    }


}
