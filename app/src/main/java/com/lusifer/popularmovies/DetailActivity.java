package com.lusifer.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lusifer.popularmovies.Model.MovieResult;
import com.lusifer.popularmovies.Model.MovieResultSugar;
import com.lusifer.popularmovies.Model.VideoPojo;
import com.lusifer.popularmovies.Model.VideoResult;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    Intent intent;
    private Call<VideoPojo> videoPojoCall;
    private RestAPIClient restClient;
    private int id;
    private List<VideoResult> videoResults;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        videoResults = new ArrayList<>();
        final MovieResult detail = (MovieResult) intent.getParcelableExtra("DetailMovie");
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        bar = (ProgressBar) findViewById(R.id.pbTrailer);
        bar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        Select Query = Select.from(MovieResultSugar.class)
                .where(Condition.prop("id_Movie_Result").eq(detail.getId()));
        long numberQuery = Query.count();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fav);

        if (numberQuery == 0) {
            fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            fab.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        String title = detail.getTitle();
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        TextView tvOverview = (TextView) findViewById(R.id.tvOverviewDetail);
        tvOverview.setText(detail.getOverview());
        TextView tvRelease = (TextView) findViewById(R.id.tvReleaseDate);
        tvRelease.setText(detail.getReleaseDate());
        final ImageView headerImage = (ImageView) findViewById(R.id.ivDetail);
        Picasso.with(this).setLoggingEnabled(true);
        Picasso.with(this).load(getString(R.string.image_baseurl) + detail.getPosterPath()).networkPolicy(NetworkPolicy.OFFLINE)
                .into(headerImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getBaseContext())
                                .load(getString(R.string.image_baseurl) + detail.getPosterPath())
                                .into(headerImage);
                    }
                });
        TextView ratingText = (TextView) findViewById(R.id.tvRating);
        Float voting_avg = detail.getVoteAverage();
        String rating = String.valueOf(voting_avg) + "/10";
        ((RatingBar) findViewById(R.id.ratingBar)).setRating(voting_avg);
        ratingText.setText(rating);
        id = detail.getId();
        recyclerView = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RecyclerAdapter(this, videoResults);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(adapter);
        restClient = new RestAPIClient();
        getTrailer();
        try {
            FloatingActionButton review = (FloatingActionButton) findViewById(R.id.review);
            review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
                    intent.putExtra(getString(R.string.extra_detail), detail);
                    startActivity(intent);

                }
            });

        } catch (NullPointerException e) {
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select Query = Select.from(MovieResultSugar.class)
                        .where(Condition.prop("id_Movie_Result").eq(detail.getId())).limit("1");
                long numberQuery = Query.count();

                if (numberQuery == 0) {
                    MovieResultSugar movieResult = new MovieResultSugar(detail.getPosterPath(), detail.getAdult(), detail.getOverview(), detail.getReleaseDate(), detail.getGenreIds(), detail.getId(), detail.getOriginalTitle(), detail.getOriginalLanguage(), detail.getTitle(), detail.getBackdropPath(), detail.getPopularity(), detail.getVoteCount(), detail.getVideo(), detail.getVoteAverage());
                    movieResult.save();
                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                } else {
//                    List<MovieResultSugar> listQuery = Query.list();
//                    if (listQuery.size() != 0) {
//                        MovieResultSugar movieResult = MovieResultSugar.findById(MovieResultSugar.class, listQuery.get(0).getId());
//                        movieResult.delete();
//                    }
                    MovieResultSugar.deleteAll(MovieResultSugar.class, "id_Movie_Result = ?", detail.getId()+"");
                    fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }

            }
        });
    }

    public void getTrailer() {
        videoPojoCall = restClient.getMovieService().getTrailers(id, getString(R.string.api_key));
        videoPojoCall.enqueue(new retrofit2.Callback<VideoPojo>() {
            @Override
            public void onResponse(Call<VideoPojo> call, Response<VideoPojo> response) {
                videoResults.clear();
                for (int i = 0; i < response.body().getResults().size(); i++) {
                    videoResults.add(response.body().getResults().get(i));
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<VideoPojo> call, Throwable t) {

            }
        });
    }
}
