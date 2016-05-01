package com.lusifer.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> {

    private ArrayList<String> imageUrl;
    private Context context;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int i) {

        View rowView = LayoutInflater.from (parentViewGroup.getContext())
            .inflate(R.layout.grid_basic_item, parentViewGroup, false);

        return new ViewHolder (rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(imageUrl.get(position)).networkPolicy(NetworkPolicy.OFFLINE)
                .into(viewHolder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        // Try again online if cache failed
                        Picasso.with(context)
                                .load(imageUrl.get(position))
                                .into(viewHolder.imageView);
                    }
                });

    }
    public GridRecyclerAdapter(ArrayList<String> imageUrl,Context context){
        this.imageUrl=imageUrl;
        this.context=context;
    }

    @Override
    public int getItemCount() {

        return imageUrl.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(
                R.id.ivGridItem);
        }
    }

}
