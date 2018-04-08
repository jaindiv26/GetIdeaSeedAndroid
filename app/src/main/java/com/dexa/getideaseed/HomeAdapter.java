package com.dexa.getideaseed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dev on 03/03/18.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.NumberViewHolder> implements Filterable {

    private Context context;
    private ArrayList<ModelExplorer> originalArrayList;
    private HomeAdapterClickListener homeAdapterClickListener;
    private ArrayList<ModelExplorer> filteredArrayList = new ArrayList<>();
    private ArrayList<ModelExplorer> backupList = new ArrayList<>();
    private HashMap<String,String> hashMap = new HashMap<>();
    private String searchQuery;

    public HomeAdapter(Context context, ArrayList<ModelExplorer> arrayList, HomeAdapterClickListener homeAdapterClickListener) {
        this.context = context;
        originalArrayList = arrayList;
        this.homeAdapterClickListener =homeAdapterClickListener;
        filteredArrayList = arrayList;
    }

    public void updateBackupList(List<ModelExplorer> updatedList){
        backupList.clear();
        backupList.addAll(updatedList);
    }

    @Override public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_posted_idea_item, parent, false);
        HomeAdapter.NumberViewHolder viewHolder = new HomeAdapter.NumberViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(NumberViewHolder holder, final int position) {
        final ModelExplorer modelExplorer;
        modelExplorer = filteredArrayList.get(position);
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
        holder.tvIdeaOriginality.setText(String.valueOf(modelExplorer.getOrginality()));
        holder.tvIdeaDifficulty.setText(String .valueOf(modelExplorer.getDifficulty()));
        if(modelExplorer.getPrivate()){
            holder.cbUserProjectVisibility.setChecked(false);
            holder.cbUserProjectVisibility.setText("  Private");
        }
        else {
            holder.cbUserProjectVisibility.setChecked(true);
            holder.cbUserProjectVisibility.setText("  Public");

        }
    }

    @Override public int getItemCount() {
        if(filteredArrayList != null){
            if(filteredArrayList.size() == 0){
                return  0;
            }
            else
                return filteredArrayList.size();
        }
        else
            return 0;
    }

    @Override public Filter getFilter() {
        return new Filter() {
            @Override protected FilterResults performFiltering(CharSequence constraint) {
                searchQuery = constraint.toString();
                searchQuery = searchQuery.trim();
                if(backupList.size() <= originalArrayList.size()){
                    backupList.clear();
                    backupList.addAll(originalArrayList);
                }
                else {
                    originalArrayList.clear();
                    originalArrayList.addAll(backupList);
                }
                ArrayList<ModelExplorer> filteredList = new ArrayList<>();
                int difficultyCount=0,originalityCount=0,progressCount=0,lightBulbCount=0;

                difficultyCount=0;
                originalityCount=0;
                progressCount=0;
                lightBulbCount=0;

                if(hashMap==null){
                    difficultyCount=0;
                    originalityCount=0;
                    progressCount=0;
                    lightBulbCount=0;
                }
                else {
                    if(hashMap.containsKey("difficulty")){
                        difficultyCount = Integer.parseInt(hashMap.get("difficulty"));
                    }
                    if(hashMap.containsKey("originality")){
                        originalityCount = Integer.parseInt(hashMap.get("originality"));
                    }
                    if(hashMap.containsKey("progress")){
                        progressCount = Integer.parseInt(hashMap.get("progress"));
                    }
                    if(hashMap.containsKey("lightBulbs")){
                        lightBulbCount = Integer.parseInt(hashMap.get("lightBulbs"));
                    }
                }


                if(searchQuery.length()>=2) {
                    for (ModelExplorer modelExplorer : originalArrayList) {
                        if (modelExplorer.getTitle().toLowerCase().contains(searchQuery.toLowerCase()) || modelExplorer.getDescription().toLowerCase().contains(searchQuery.toLowerCase())) {
                            filteredList.add(modelExplorer);
                        }
                    }
                }
                else{
                    if(difficultyCount ==0 && originalityCount ==0 && progressCount ==0 && lightBulbCount==0){
                        filteredList.addAll(originalArrayList);
                    }
                    else {
                        for (int i = 0; i< originalArrayList.size(); i++){
                            ModelExplorer modelExplorer = originalArrayList.get(i);
                            if(hashMap.containsKey("difficulty")){
                                difficultyCount = Integer.parseInt(hashMap.get("difficulty"));
                            }
                            if(hashMap.containsKey("originality")){
                                originalityCount = Integer.parseInt(hashMap.get("originality"));
                            }
                            if(hashMap.containsKey("progress")){
                                progressCount = Integer.parseInt(hashMap.get("progress"));
                            }
                            if(hashMap.containsKey("lightBulbs")){
                                lightBulbCount = Integer.parseInt(hashMap.get("lightBulbs"));
                            }
                            if(difficultyCount == 0){
                                difficultyCount = modelExplorer.getDifficulty();
                            }
                            if(originalityCount == 0){
                                originalityCount = modelExplorer.getOrginality();
                            }
                            if(progressCount == 0){
                                progressCount = modelExplorer.getProgress();
                            }
                            if(lightBulbCount == 0){
                                lightBulbCount = modelExplorer.getLightbulbs();
                            }
                            if(modelExplorer.getDifficulty()==difficultyCount && modelExplorer.getProgress()==progressCount
                                    && modelExplorer.getOrginality()==originalityCount && modelExplorer.getLightbulbs()==lightBulbCount){
                                filteredList.add(modelExplorer);
                            }
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredArrayList.clear();
                if(results.values != null){
                    filteredArrayList.addAll((ArrayList<ModelExplorer>) results.values);
                }
                if(homeAdapterClickListener != null){
                    homeAdapterClickListener.onNoResultFound(filteredArrayList.isEmpty(),backupList.isEmpty());
                }
                notifyDataSetChanged();
            }
        };
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserProjectName,tvUserProjectDescription,tvIdeaOriginality,tvIdeaDifficulty;
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
            tvIdeaDifficulty = itemView.findViewById(R.id.tvIdeaDifficulty);
            tvIdeaOriginality = itemView.findViewById(R.id.tvIdeaOriginality);
        }
    }

    public void setFilterHashMap(HashMap<String,String> hashMap){
        this.hashMap = hashMap;
    }
}
