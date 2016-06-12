package com.lusifer.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lusifer.popularmovies.Model.FavAdapter;
import com.lusifer.popularmovies.Model.MovieResultSugar;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private List<MovieResultSugar> favResultList;

    private FavAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        favResultList = new ArrayList<>();
        getFav();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvReview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FavAdapter(this,favResultList);
        recyclerView.setAdapter(adapter);


    }


    public void getFav() {
        List<MovieResultSugar> listAll=MovieResultSugar.find(MovieResultSugar.class,null);
        favResultList.clear();
        for (int i = 0; i < listAll.size(); i++) {
            favResultList.add(listAll.get(i));
        }
    }
}
