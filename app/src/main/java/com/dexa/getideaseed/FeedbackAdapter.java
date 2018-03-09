package com.dexa.getideaseed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dev on 27/02/18.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.NumberViewHolder> {

    private Context context;
    private ArrayList<ModelFeedback> modelFeedbackArrayList;

    public FeedbackAdapter(Context context, ArrayList<ModelFeedback> arrayList) {
        this.context = context;
        this.modelFeedbackArrayList = arrayList;
    }

    @Override public FeedbackAdapter.NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout__feedback_item, parent, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(FeedbackAdapter.NumberViewHolder holder, int position) {
        final ModelFeedback modelFeedback;
        modelFeedback = modelFeedbackArrayList.get(position);

        holder.username.setText(modelFeedback.getFrom());
        holder.comments.setText(modelFeedback.getMessage());
    }

    @Override public int getItemCount() {
        return modelFeedbackArrayList.size();
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {
        private TextView username,comments;

        public NumberViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvGuestname);
            comments = itemView.findViewById(R.id.tvGuestComment);
        }
    }
}
