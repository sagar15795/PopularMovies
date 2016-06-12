package com.lusifer.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lusifer.popularmovies.Model.ReviewResult;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReviewResult> mResults;



    public ReviewAdapter( List<ReviewResult> results) {
        mResults = results;
    }

    public void setResults(List<ReviewResult> results) {
        mResults = results;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.review_item_layout, parent, false);

            vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            String author="By :-"+mResults.get(position).getAuthor();
            ((ViewHolder) holder).author.setText(author);
            ((ViewHolder) holder).content.setText(mResults.get(position).getContent());

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


        TextView content,author;

        public ViewHolder(View itemView) {
            super(itemView);
            content=(TextView)itemView.findViewById(R.id.tvContent);
            author=(TextView)itemView.findViewById(R.id.tvAuthor);
        }
    }
}