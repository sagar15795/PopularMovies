package com.lusifer.popularmovies;

import com.lusifer.popularmovies.Model.MovieResult;
import com.lusifer.popularmovies.Model.MovieResultSugar;
import com.lusifer.popularmovies.Model.VideoPojo;
import com.lusifer.popularmovies.Model.VideoResult;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Call<VideoPojo> videoPojoCall;
    private RestAPIClient restClient;
    private int id;
    private List<VideoResult> videoResults;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ProgressBar bar;
    MovieResult detail;

    public static DetailFragment newInstance(MovieResult movieResult) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("DetailMovie", movieResult);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            detail = getArguments().getParcelable(getString(R.string.extra_detail));
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.blank, container, false);;
        if(detail!=null) {
         rootView= inflater.inflate(R.layout.fragment_detail, container, false);

            videoResults = new ArrayList<>();


            bar = (ProgressBar) rootView.findViewById(R.id.pbTrailer);
            bar.setVisibility(View.VISIBLE);

            Select Query = Select.from(MovieResultSugar.class)
                    .where(Condition.prop("id_Movie_Result").eq(detail.getId()));
            long numberQuery = Query.count();
            final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fav);

            if (numberQuery == 0) {
                fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_favorite_black_24dp);
            }

            collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

            String title = detail.getTitle();
            collapsingToolbarLayout.setTitle(title);
            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
            TextView tvOverview = (TextView) rootView.findViewById(R.id.tvOverviewDetail);
            tvOverview.setText(detail.getOverview());
            TextView tvRelease = (TextView) rootView.findViewById(R.id.tvReleaseDate);
            tvRelease.setText(detail.getReleaseDate());
            final ImageView headerImage = (ImageView) rootView.findViewById(R.id.ivDetail);
            Picasso.with(getContext()).setLoggingEnabled(true);
            Picasso.with(getContext()).load(getString(R.string.image_baseurl) + detail.getPosterPath()).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(headerImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getContext())
                                    .load(getString(R.string.image_baseurl) + detail.getPosterPath())
                                    .into(headerImage);
                        }
                    });
            TextView ratingText = (TextView) rootView.findViewById(R.id.tvRating);
            Float voting_avg = detail.getVoteAverage();
            String rating = String.valueOf(voting_avg) + "/10";
            ((RatingBar) rootView.findViewById(R.id.ratingBar)).setRating(voting_avg);
            ratingText.setText(rating);
            id = detail.getId();
            recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new RecyclerAdapter(getContext(), videoResults);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setFocusable(false);
            recyclerView.setAdapter(adapter);
            restClient = new RestAPIClient();
            getTrailer();
            try {
                FloatingActionButton review = (FloatingActionButton) rootView.findViewById(R.id
                        .review);
                review.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ReviewActivity.class);
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

                        MovieResultSugar.deleteAll(MovieResultSugar.class, "id_Movie_Result = ?",
                                detail.getId() + "");
                        fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }

                }
            });
        }
        return rootView;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!videoPojoCall.isCanceled()){
            videoPojoCall.cancel();
        }
    }
}
