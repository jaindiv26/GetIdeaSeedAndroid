package com.dexa.getideaseed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dev on 19/01/18.
 */

public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerAdapter.NumberViewHolder>{

    private Context context;
    private ArrayList<ModelExplorer> modelExplorerArrayList;
    private ClickListener clickListener;

    public ExplorerAdapter(Context context, ArrayList<ModelExplorer> arrayList,ClickListener clickListener) {
        this.context = context;
        modelExplorerArrayList =arrayList;
        this.clickListener = clickListener;
    }


    @Override public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_explorer_item, parent, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(NumberViewHolder holder, int position) {
        final ModelExplorer modelExplorer;
        modelExplorer = modelExplorerArrayList.get(position);
        holder.tvUserName.setText(modelExplorer.getUserName());
        holder.tvProjectTitle.setText((modelExplorer.getTitle()));
        holder.tvProjectDescription.setText(modelExplorer.getDescription());
        holder.tvNoOfBulbs.setText(Integer.toString(modelExplorer.getLightbulbs()));
        holder.pbOrginality.setProgress(modelExplorer.getOrginality());
        holder.pbDifficulty.setProgress(modelExplorer.getDifficulty());
        holder.feedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onClick(modelExplorer);
                }
            }
        });
        int progress = modelExplorer.getProgress();
        if(progress == 1){
            holder.projectProgress.setImageResource(R.drawable.just_planted_lightbulb_img);
        }
        if(progress == 2){
            holder.projectProgress.setImageResource(R.drawable.in_progress_lightbulb_img);
        }
        if(progress == 3){
            holder.projectProgress.setImageResource(R.drawable.finnished_project_img);
        }

    }

    @Override public int getItemCount() {
        return modelExplorerArrayList.size();
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserName,tvProjectTitle,tvProjectDescription,tvNoOfBulbs;
        private ProgressBar pbOrginality,pbDifficulty;
        private Button feedBackButton;
        private ImageView projectProgress;

        public NumberViewHolder(View itemView) {
            super(itemView);
            tvUserName=itemView.findViewById(R.id.tvUsername);
            tvProjectTitle=itemView.findViewById(R.id.tvProjectTitle);
            tvProjectDescription=itemView.findViewById(R.id.tvProjectDescription);
            tvNoOfBulbs=itemView.findViewById(R.id.tvNoOfBulb);
            pbOrginality = itemView.findViewById(R.id.pbOriginality);
            pbDifficulty = itemView.findViewById(R.id.pbDifficulty);
            feedBackButton = itemView.findViewById(R.id.btFeedback);
            projectProgress = itemView.findViewById(R.id.ivWorkProgress);
        }
    }
}
