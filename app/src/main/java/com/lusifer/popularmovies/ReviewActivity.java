package com.lusifer.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.lusifer.popularmovies.Model.MovieResult;
import com.lusifer.popularmovies.Model.ReviewResult;
import com.lusifer.popularmovies.Model.ReviewsDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private List<ReviewResult> reviewResultList;
    private RestAPIClient restClient;
    private Call<ReviewsDetail> reviewsDetailCall;
    private ReviewAdapter adapter;
    MovieResult detail;
    int page=1;
    int total_no;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        bar=(ProgressBar)findViewById(R.id.pbshow);
        bar.setVisibility(View.VISIBLE);
        detail = (MovieResult) intent.getParcelableExtra("DetailMovie");
        reviewResultList=new ArrayList<>();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rvReview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new ReviewAdapter(reviewResultList);
        recyclerView.setAdapter(adapter);
        restClient = new RestAPIClient();
        getReview(page);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewResultList.get(position).getUrl()));
                startActivity(browserIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if(page<total_no) {
                    bar.setVisibility(View.VISIBLE);
                    getReview(++page);
                }
            }
        });
    }

    public void getReview(int page) {
        reviewsDetailCall = restClient.getMovieService().getReviews(detail.getId(),page,getString(R.string.api_key));
        reviewsDetailCall.enqueue(new retrofit2.Callback<ReviewsDetail>() {
            @Override
            public void onResponse(Call<ReviewsDetail> call, Response<ReviewsDetail> response) {
                total_no=response.body().getTotalPages();

                for (int i = 0; i < response.body().getTotalResults(); i++) {
                    reviewResultList.add(response.body().getReviewResults().get(i));
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ReviewsDetail> call, Throwable t) {

            }
        });
    }
}
