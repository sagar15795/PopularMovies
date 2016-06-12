package com.lusifer.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reviewResultList=new ArrayList<>();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rvReview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter=new ReviewAdapter(reviewResultList);
        recyclerView.setAdapter(adapter);
        restClient = new RestAPIClient();
        getTrailer();
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
    }

    public void getTrailer() {
        reviewsDetailCall = restClient.getMovieService().getReviews(293660,1,getString(R.string.api_key));
        reviewsDetailCall.enqueue(new retrofit2.Callback<ReviewsDetail>() {
            @Override
            public void onResponse(Call<ReviewsDetail> call, Response<ReviewsDetail> response) {
                reviewResultList.clear();
                for (int i = 0; i < response.body().getTotalResults(); i++) {
                    reviewResultList.add(response.body().getReviewResults().get(i));
                }
                adapter.notifyDataSetChanged();
//                bar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ReviewsDetail> call, Throwable t) {

            }
        });
    }
}
