package com.lusifer.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.lusifer.popularmovies.Model.MoviePojo;
import com.lusifer.popularmovies.Model.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> imageUrlList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridRecyclerAdapter mAdapter;
    private ArrayList<Result> resultArrayList = new ArrayList<>();
    private Call<MoviePojo> moviePojoCall;
    private RestAPIClient restClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        prepareMovieData();
        mAdapter = new GridRecyclerAdapter(imageUrlList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        restClient = new RestAPIClient();
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String unitType = sharedPrefs.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_popular));

        moviePojoCall = restClient.getMovieService().getMovies(unitType,getString(R.string.api_key));

        moviePojoCall.enqueue(new Callback<MoviePojo>() {
            @Override
            public void onResponse(Call<MoviePojo> call, Response<MoviePojo> response) {
                    resultArrayList.clear();
                for (int i = 0 ; i < response.body().getResults().size() ; i++) {
                    resultArrayList.add(response.body().getResults().get(i));
                }
                prepareMovieData();
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MoviePojo> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Result result = resultArrayList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(getString(R.string.extra_title), result.getTitle());
                intent.putExtra(getString(R.string.extra_overview), result.getOverview());
                intent.putExtra(getString(R.string.extra_imageurl), result.getPosterPath());
                intent.putExtra(getString(R.string.extra_rating), result.getVoteAverage());
                intent.putExtra(getString(R.string.extra_releasedate),result.getReleaseDate());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        imageUrlList.clear();
        mAdapter.notifyDataSetChanged();


        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String unitType = sharedPrefs.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_popular));

        moviePojoCall = restClient.getMovieService().getMovies(unitType,getString(R.string.api_key));

        moviePojoCall.enqueue(new Callback<MoviePojo>() {
            @Override
            public void onResponse(Call<MoviePojo> call, Response<MoviePojo> response) {
                resultArrayList.clear();
                for (int i = 0 ; i < response.body().getResults().size() ; i++) {
                    resultArrayList.add(response.body().getResults().get(i));
                }
                prepareMovieData();
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<MoviePojo> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_setting:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareMovieData() {
        imageUrlList.clear();
        for (int i = 0 ; i < resultArrayList.size() ; i++) {
            imageUrlList.add("http://image.tmdb.org/t/p/w500" + resultArrayList.get(i).getPosterPath());
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MainActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}
