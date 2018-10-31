package com.dexa.getideaseed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dev on 27/02/18.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.NumberViewHolder> {

    private Context context;
    private ArrayList<ModelFeedback> modelFeedbackArrayList;
    private ClickListener clickListener;


    public FeedbackAdapter(Context context, ArrayList<ModelFeedback> arrayList,ClickListener clickListener) {
        this.context = context;
        this.modelFeedbackArrayList = arrayList;
        this.clickListener = clickListener;
    }

    @Override public FeedbackAdapter.NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_feedback_item, parent, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(final FeedbackAdapter.NumberViewHolder holder, int position) {
        final ModelFeedback modelFeedback;
        modelFeedback = modelFeedbackArrayList.get(position);
        holder.editComment.setVisibility(View.GONE);
        holder.comments.setVisibility(View.VISIBLE);
        holder.editButton.setText("Edit");
        holder.editButton.setVisibility(View.GONE);

        holder.username.setText(modelFeedback.getFrom());
        holder.comments.setText(modelFeedback.getMessage());
        if(holder.username.getText().equals(PrefManager.getInstance().getString("username"))) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.editComment.setText(modelFeedback.getMessage());
        }

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (holder.FLAG==0){
                    holder.comments.setVisibility(View.GONE);
                    holder.editComment.setVisibility(View.VISIBLE);
                    holder.editButton.setText("Done");
                    holder.FLAG=1;
                }
                else if(holder.FLAG==1){
                    holder.comments.setVisibility(View.VISIBLE);
                    holder.editComment.setVisibility(View.GONE);
                    holder.editButton.setText("Edit");
                    holder.editComment.setSelection(holder.editComment.length());
                    holder.editComment.requestFocus();
                    if(clickListener != null){
                        clickListener.onClick(modelFeedback,holder.editComment.getText().toString());
                    }
                    holder.FLAG=0;
                }
            }
        });
    }

    @Override public int getItemCount() {
        return modelFeedbackArrayList.size();
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {
        private TextView username,comments;
        EditText editComment;
        private Button editButton;
        int FLAG = 0;

        public NumberViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tvGuestname);
            comments = itemView.findViewById(R.id.tvGuestComment);
            editComment = itemView.findViewById(R.id.etEditComment);
            editButton = itemView.findViewById(R.id.btEditComment);
        }
    }
}
