package com.lusifer.popularmovies;

import com.lusifer.popularmovies.Model.MovieResult;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by lusifer on 12/6/16.
 */
public class MainActivity extends AppCompatActivity implements DetailFragmentCallback{


    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,new MainFragment())
                    .commit();
        if (findViewById(R.id.details) != null) {

            mTwoPane = true;

        } else {
            mTwoPane = false;
        }

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
            case R.id.action_fav:
                Intent intent1 = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(intent1);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(MovieResult movie) {
        if (mTwoPane) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details, DetailFragment.newInstance(movie))
                    .commit();
        } else {
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(getString(R.string.extra_detail), movie);
            startActivity(intent);
        }
    }
}