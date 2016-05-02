package com.lusifer.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout ;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getIntent();
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(intent.getStringExtra(getString(R.string.extra_title)));
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        TextView tvOverview=(TextView)findViewById(R.id.tvOverviewDetail);
        tvOverview.setText(intent.getStringExtra(getString(R.string.extra_overview)));
        TextView tvRelease=(TextView)findViewById(R.id.tvReleaseDate);
        tvRelease.setText(intent.getStringExtra(getString(R.string.extra_releasedate)));
        final ImageView headerImage=(ImageView)findViewById(R.id.ivDetail);
        Picasso.with(this).setLoggingEnabled(true);
        Picasso.with(this).load(getString(R.string.image_baseurl)+intent.getStringExtra(getString(R.string.extra_imageurl))).networkPolicy(NetworkPolicy.OFFLINE)
                .into(headerImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }
                    @Override
                    public void onError() {
                        Picasso.with(getBaseContext())
                                .load(getString(R.string.image_baseurl)+intent.getStringExtra(getString(R.string.extra_imageurl)))
                                .into(headerImage);
                    }
                });
        TextView ratingText = (TextView) findViewById(R.id.tvRating);
        Float voting_avg=intent.getFloatExtra(getString(R.string.extra_rating),0);
        String rating=String.valueOf(voting_avg)+"/10";
        ((RatingBar) findViewById(R.id.ratingBar)).setRating(voting_avg);
        ratingText.setText(rating);
    }

}
