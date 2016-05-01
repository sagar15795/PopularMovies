package com.lusifer.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> imageUrlList = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        prepareMovieData();
        mAdapter = new GridRecyclerAdapter(imageUrlList, this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


    }

    private void prepareMovieData() {
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");
        imageUrlList.add("https://futurestud.io/blog/content/images/2015/05/picasso-indicators--1-.png");

    }
}
