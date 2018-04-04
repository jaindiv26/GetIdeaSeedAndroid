package com.dexa.getideaseed;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dev on 19/01/18.
 */

public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerAdapter.NumberViewHolder> implements Filterable {

    private Context context;
    private ArrayList<ModelExplorer> originalArrayList;
    private ArrayList<ModelExplorer> filteredArrayList = new ArrayList<>();;
    private ClickListener clickListener;
    private HashMap<String,String> hashMap = new HashMap<>();

    public ExplorerAdapter(Context context, ArrayList<ModelExplorer> arrayList, ClickListener clickListener) {
        this.context = context;
        originalArrayList = arrayList;
        this.clickListener = clickListener;
    }

    public void updateExplorerData(List<ModelExplorer> updatedList){
        filteredArrayList.clear();
        filteredArrayList.addAll(updatedList);
        notifyDataSetChanged();
    }

    @Override public NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_explorer_item, parent, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override public void onBindViewHolder(final NumberViewHolder holder, int position) {
        final ModelExplorer modelExplorer;
        modelExplorer = filteredArrayList.get(position);
        holder.noOfBulbs = modelExplorer.getLightbulbs();
        holder.tvUserName.setText(modelExplorer.getUserName());
        holder.tvProjectTitle.setText((modelExplorer.getTitle()));
        holder.tvProjectDescription.setText(modelExplorer.getDescription());
        holder.tvNoOfBulbs.setText(Integer.toString(modelExplorer.getLightbulbs()));
        holder.pbOriginality.setProgress(modelExplorer.getOrginality());
        holder.pbDifficulty.setProgress(modelExplorer.getDifficulty());
        holder.feedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(modelExplorer);
                }
            }
        });
        holder.ivLightBulbClickListener.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(modelExplorer.isHasLiked()){
                    holder.noOfBulbs = holder.noOfBulbs - 1;
                    holder.tvNoOfBulbs.setText(String.valueOf(holder.noOfBulbs));
                    holder.ivLightBulbClickListener.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.likebulb_unselected));
                }
                else{
                    holder.ivLightBulbClickListener.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.likebulb_selected));
                    holder.noOfBulbs = holder.noOfBulbs + 1;
                    holder.tvNoOfBulbs.setText(String.valueOf(holder.noOfBulbs));
                }
                if (clickListener != null) {
                    clickListener.onLightBulbClicked(modelExplorer,modelExplorer.getLightbulbs());
                }
            }
        });

        if(modelExplorer.isHasLiked()){
            holder.ivLightBulbClickListener.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.likebulb_selected));
        }
        else if(!modelExplorer.isHasLiked()){
            holder.ivLightBulbClickListener.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.likebulb_unselected));
        }

        int progress = modelExplorer.getProgress();
        if (progress == 1) {
            holder.ivProjectProgress.setImageResource(R.drawable.just_planted_lightbulb_img);
        }
        if (progress == 2) {
            holder.ivProjectProgress.setImageResource(R.drawable.in_progress_lightbulb_img);
        }
        if (progress == 3) {
            holder.ivProjectProgress.setImageResource(R.drawable.finnished_project_img);
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
                String charString = constraint.toString();
                charString = charString.trim();
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

                if(charString.length()>2) {
                    for (ModelExplorer modelExplorer : originalArrayList) {
                        if (modelExplorer.getTitle().toLowerCase().contains(charString)) {
                            filteredList.add(modelExplorer);
                        }
                    }
                    if(filteredList.size()==0){
                        if (clickListener != null) {
                            clickListener.onResultFound(true);
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
                            if(modelExplorer.getDifficulty()>=difficultyCount && modelExplorer.getProgress()>=progressCount && modelExplorer.getOrginality()>=originalityCount && modelExplorer.getLightbulbs()>=lightBulbCount){
                                filteredList.add(modelExplorer);
                            }
                        }
                    }
                    if(filteredList.size()>0){
                        if (clickListener != null) {
                            clickListener.onResultFound(false);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override protected void publishResults(CharSequence constraint, FilterResults results) {filteredArrayList.clear();
                if(results.values == null){
                    updateExplorerData(new ArrayList<ModelExplorer>());
                }
                else{
                    updateExplorerData((List<ModelExplorer>) results.values);
                }
            }
        };
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserName, tvProjectTitle, tvProjectDescription, tvNoOfBulbs;
        private ProgressBar pbOriginality, pbDifficulty;
        private Button feedBackButton;
        private ImageView ivProjectProgress, ivLightBulbClickListener;
        private int noOfBulbs = 0;

        public NumberViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUsername);
            tvProjectTitle = itemView.findViewById(R.id.tvProjectTitle);
            tvProjectDescription = itemView.findViewById(R.id.tvProjectDescription);
            tvNoOfBulbs = itemView.findViewById(R.id.tvNoOfBulb);
            pbOriginality = itemView.findViewById(R.id.pbOriginality);
            pbDifficulty = itemView.findViewById(R.id.pbDifficulty);
            feedBackButton = itemView.findViewById(R.id.btFeedback);
            ivProjectProgress = itemView.findViewById(R.id.ivWorkProgress);
            ivLightBulbClickListener = itemView.findViewById(R.id.ivLightBulbLiked);
        }
    }

    public void getData(HashMap<String,String> hashMap){
        this.hashMap = hashMap;
    }
}
