package com.dexa.getideaseed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dev on 03/03/18.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.NumberViewHolder>  {

    private Context context;
    private ArrayList<ModelExplorer> modelExplorerArrayList;
    private HomeAdapterClickListener homeAdapterClickListener;

    public HomeAdapter(Context context, ArrayList<ModelExplorer> arrayList, HomeAdapterClickListener homeAdapterClickListener) {
        this.context = context;
        modelExplorerArrayList = arrayList;
        this.homeAdapterClickListener =homeAdapterClickListener;
    }

    @Override public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_posted_idea_item, parent, false);
        HomeAdapter.NumberViewHolder viewHolder = new HomeAdapter.NumberViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(NumberViewHolder holder, final int position) {
        final ModelExplorer modelExplorer;
        modelExplorer = modelExplorerArrayList.get(position);
        holder.tvUserProjectName.setText(modelExplorer.getTitle());
        holder.tvUserProjectDescription.setText(modelExplorer.getDescription());
        holder.pbUserProjectOriginality.setProgress(modelExplorer.getOrginality());
        holder.pbUserProjectDifficulty.setProgress(modelExplorer.getDifficulty());
        holder.btUserProjectDelete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(homeAdapterClickListener != null){
                    homeAdapterClickListener.onDelete(modelExplorer);
                }
            }
        });
        holder.btUserProjectFeedback.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(homeAdapterClickListener != null){
                    homeAdapterClickListener.onFeedback(modelExplorer);
                }
            }
        });
        holder.btUserProjectEdit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(homeAdapterClickListener != null){
                    homeAdapterClickListener.onEdit(modelExplorer,position);
                }
            }
        });

        int progress = modelExplorer.getProgress();
        if(progress == 1){
            holder.ivIdeaProgress.setImageResource(R.drawable.just_planted_lightbulb_img);
        }
        if(progress == 2){
            holder.ivIdeaProgress.setImageResource(R.drawable.in_progress_lightbulb_img);
        }
        if(progress == 3){
            holder.ivIdeaProgress.setImageResource(R.drawable.finnished_project_img);
        }

        holder.cbUserProjectVisibility.setClickable(false);
    }

    @Override public int getItemCount() {
        return modelExplorerArrayList.size();
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserProjectName,tvUserProjectDescription;
        private ProgressBar pbUserProjectOriginality,pbUserProjectDifficulty;
        private CheckBox cbUserProjectVisibility;
        private Button btUserProjectEdit,btUserProjectFeedback,btUserProjectDelete;
        private ImageView ivIdeaProgress;
        public NumberViewHolder(View itemView) {
            super(itemView);
            tvUserProjectName = itemView.findViewById(R.id.tvUserProjectName);
            tvUserProjectDescription = itemView.findViewById(R.id.tvUserProjectDescription);
            pbUserProjectOriginality = itemView.findViewById(R.id.pbUserProjectOriginality);
            pbUserProjectDifficulty = itemView.findViewById(R.id.pbUserProjectDifficulty);
            cbUserProjectVisibility = itemView.findViewById(R.id.cbUserProjectVisibility);
            btUserProjectEdit = itemView.findViewById(R.id.btUserProjectEdit);
            btUserProjectFeedback = itemView.findViewById(R.id.btUserProjectFeedback);
            btUserProjectDelete = itemView.findViewById(R.id.btUserProjectDelete);
            ivIdeaProgress = itemView.findViewById(R.id.ivIdeaProgress);
        }
    }
}
