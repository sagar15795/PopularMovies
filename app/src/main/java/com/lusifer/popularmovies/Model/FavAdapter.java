package com.lusifer.popularmovies.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lusifer.popularmovies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lusifer on 12/6/16.
 */
public class FavAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private List<MovieResultSugar> mResults;


    public FavAdapter(Context context, List<MovieResultSugar> results) {
        mResults = results;
        this.context = context;
    }

    public void setResults(List<MovieResultSugar> results) {
        mResults = results;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fav_item_layout, parent, false);

        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            if (mResults.size() != 0) {
                Picasso.with(context).setLoggingEnabled(true);
                Picasso.with(context).load("http://image.tmdb.org/t/p/w500" + mResults.get(position).getPosterPath()).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(((ViewHolder) holder).image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                // Try again online if cache failed
                                Picasso.with(context)
                                        .load("http://image.tmdb.org/t/p/w500" + mResults.get(position).getPosterPath())
                                        .into(((ViewHolder) holder).image);
                            }
                        });
                ((ViewHolder) holder).name.setText(mResults.get(position).getTitle());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mResults.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ivIcon);
            name = (TextView) itemView.findViewById(R.id.tvName);
        }
    }
}