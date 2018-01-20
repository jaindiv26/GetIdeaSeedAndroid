package com.dexa.getideaseed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Dev on 19/01/18.
 */

public class SeedViewRecycler extends RecyclerView.Adapter<SeedViewRecycler.NumberViewHolder> {

    private Context context;
    private int numList=10;

    public SeedViewRecycler(Context context, int numList) {
        this.context = context;
        this.numList = numList;
    }


    @Override public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.seed_view, parent, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(NumberViewHolder holder, int position) {

    }

    @Override public int getItemCount() {
        return numList;
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {
        public NumberViewHolder(View itemView) {
            super(itemView);
        }
    }
}
