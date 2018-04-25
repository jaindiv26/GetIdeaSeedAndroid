package com.dexa.getideaseed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dev on 19/01/18.
 */

public class ExplorerAdapter extends RecyclerView.Adapter<ExplorerAdapter.NumberViewHolder> implements Filterable {

    private Context context;
    private ArrayList<ModelExplorer> originalArrayList;
    private ArrayList<ModelExplorer> filteredArrayList = new ArrayList<>();
    private ArrayList<ModelExplorer> backupList = new ArrayList<>();
    private ClickListener clickListener;
    private HashMap<String,String> hashMap = new HashMap<>();
    private String searchQuery;

    public ExplorerAdapter(Context context, ArrayList<ModelExplorer> arrayList, ClickListener clickListener) {
        this.context = context;
        originalArrayList = arrayList;
        this.clickListener = clickListener;
        filteredArrayList = arrayList;
    }

    public void updateBackupList(List<ModelExplorer> updatedList){
        backupList.clear();
        backupList.addAll(updatedList);
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
        highLightSearchQuery(holder,modelExplorer);
        holder.tvNoOfBulbs.setText(Integer.toString(modelExplorer.getLightbulbs()));
        if(modelExplorer.getLightbulbs()>=0){
            holder.tvNoOfBulbs.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvNoOfBulbs.setVisibility(View.INVISIBLE);
        }
        holder.pbOriginality.setProgress(modelExplorer.getOrginality());
        holder.pbDifficulty.setProgress(modelExplorer.getDifficulty());
        holder.feedBackButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(modelExplorer);
                }
            }
        });
        holder.cbLightBulbClickListener.setOnCheckedChangeListener(null);
        if(modelExplorer.isHasLiked()){
            holder.cbLightBulbClickListener.setChecked(true);
            holder.ivLightBulbLiked.setImageResource(R.drawable.likebulb_selected);
        }
        else {
            holder.cbLightBulbClickListener.setChecked(false);
            holder.ivLightBulbLiked.setImageResource(R.drawable.likebulb_unselected);
        }

        holder.cbLightBulbClickListener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(PrefManager.getInstance().getBoolean("loggedIn")){
                    if(isInternetOn(context)){
                        if(isChecked){
                            holder.noOfBulbs = holder.noOfBulbs + 1;
                            holder.tvNoOfBulbs.setText(String.valueOf(holder.noOfBulbs));
                            holder.ivLightBulbLiked.setImageResource(R.drawable.likebulb_selected);
                        }
                        else {
                            holder.noOfBulbs = holder.noOfBulbs - 1;
                            holder.tvNoOfBulbs.setText(String.valueOf(holder.noOfBulbs));
                            holder.ivLightBulbLiked.setImageResource(R.drawable.likebulb_unselected);
                        }

                        if (clickListener != null) {
                            clickListener.onLightBulbClicked(modelExplorer);
                        }
                    }
                    else {
                        Toast.makeText(context,
                                "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.showLoginSignUpDialog();
                }
            }
        });
        holder.tvProgressOriginality.setText(String.valueOf(modelExplorer.getOrginality()));
        holder.tvProgressDifficulty.setText(String.valueOf(modelExplorer.getDifficulty()));
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
                if(constraint !=null){
                    searchQuery = constraint.toString();
                    searchQuery = searchQuery.trim();
                }
                else {
                    searchQuery = "";
                }
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
                        if (modelExplorer.getTitle().toLowerCase().contains(searchQuery.toLowerCase())  || modelExplorer.getDescription().toLowerCase().contains(searchQuery.toLowerCase())) {
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
                                    && modelExplorer.getOrginality()==originalityCount){
                                filteredList.add(modelExplorer);
                            }
                            if(lightBulbCount==1 && modelExplorer.getLightbulbs()>10){
                                filteredList.remove(modelExplorer);
                            }
                            if(lightBulbCount==2){
                                if(modelExplorer.getLightbulbs()>=30){
                                    filteredList.remove(modelExplorer);
                                }
                                if(modelExplorer.getLightbulbs()<=10){
                                    filteredList.remove(modelExplorer);
                                }
                            }
                            if(lightBulbCount==3 && modelExplorer.getLightbulbs()<30){
                                filteredList.remove(modelExplorer);
                            }
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override protected void publishResults(CharSequence constraint, FilterResults results) {filteredArrayList.clear();
                filteredArrayList.clear();
                if(results.values != null){
                    filteredArrayList.addAll((ArrayList<ModelExplorer>) results.values);
                }
                if(clickListener != null){
                    clickListener.onNoResultFound(filteredArrayList.isEmpty(),backupList.isEmpty());
                }
                notifyDataSetChanged();
            }
        };
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserName, tvProjectTitle, tvProjectDescription, tvNoOfBulbs,tvProgressOriginality,tvProgressDifficulty;
        private ProgressBar pbOriginality, pbDifficulty;
        private Button feedBackButton;
        private ImageView ivProjectProgress, ivLightBulbLiked;
        private CheckBox cbLightBulbClickListener;
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
            cbLightBulbClickListener = itemView.findViewById(R.id.cbLightBulbLiked);
            tvProgressOriginality = itemView.findViewById(R.id.tvProgressOriginality);
            tvProgressDifficulty = itemView.findViewById(R.id.tvProgressDifficulty);
            ivLightBulbLiked = itemView.findViewById(R.id.ivLightBulbLiked);
        }
    }

    public void getData(HashMap<String,String> hashMap){
        this.hashMap = hashMap;
    }

    private void highLightSearchQuery(final NumberViewHolder holder,ModelExplorer modelExplorer){
        SpannableString str = new SpannableString(modelExplorer.getTitle());
        if(searchQuery !=null && searchQuery != ""){
            String input = searchQuery;
            int indexOfKeyword = str.toString().indexOf(input);
            if(indexOfKeyword >=0 && indexOfKeyword < str.length()){
                str.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorPrimary)),indexOfKeyword, indexOfKeyword+input.length(),0);
            }
            holder.tvProjectTitle.setText(str);
        }
        else {
            holder.tvProjectTitle.setText((modelExplorer.getTitle()));
        }

        SpannableString string = new SpannableString(modelExplorer.getDescription());
        if(searchQuery !=null && searchQuery != ""){
            String query = searchQuery;
            int indexOfKeyword = string.toString().indexOf(query);
            if(indexOfKeyword >=0 && indexOfKeyword < string.length()){
                string.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.colorPrimary)),indexOfKeyword, indexOfKeyword+query.length(),0);
            }
            holder.tvProjectDescription.setText(string);
        }
        else {
            holder.tvProjectDescription.setText(modelExplorer.getDescription());
        }
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}