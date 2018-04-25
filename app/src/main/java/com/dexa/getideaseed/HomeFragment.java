package com.dexa.getideaseed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dev on 21/01/18.
 */

public class HomeFragment extends BaseFragment {

    private Context context;
    private RecyclerView recyclerView;
    private ImageView ivSearchIcon;
    private TextView tvSearchResult;
    public HomeAdapter homeAdapter;
    private ArrayList<ModelExplorer> serverArrayList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;
    private FloatingActionButton fabNewIdea;
    private HomeAdapterClickListener homeAdapterClickListener;
    private RelativeLayout relativeLayout;
    private RelativeLayout rlEmptyView;
    int x = 0;
    private String searchQuery;
    private HashMap<String, String> hashMapFilterApplied = new HashMap<>();

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment instance = new HomeFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponents(view);
        if(isInternetOn(context)){
            fetchData();
        }
        else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initComponents(View view) {
        //1.All fbi
        recyclerView = view.findViewById(R.id.rvNewActivity);
        mSwipeRefreshLayout = view.findViewById(R.id.srSwipeRefresh);
        fabNewIdea = view.findViewById(R.id.fabNewIdea);
        relativeLayout = view.findViewById(R.id.rvPlantASeed);
        rlEmptyView = view.findViewById(R.id.rlEmptyView);
        ivSearchIcon = view.findViewById(R.id.ivDefaultImage);
        tvSearchResult = view.findViewById(R.id.tvPlantASeed);
        //2. All listeners

        // For edit Idea into Adapter
        homeAdapterClickListener = new HomeAdapterClickListener() {
            @Override public void onEdit(ModelExplorer modelExplorer,int position) {
                //This is how you send data from fragment to parent activity
                //1. Create public method in activity
                //2. Call the public method as its shown below
                //((MainActivity)context).editIdea(modelExplorer);
                Intent intent = new Intent(context, NewIdeaActivity.class);
                intent.putExtra("editUserData",modelExplorer);
                startActivityForResult(intent,1010);
                x = position;
            }

            @Override public void onDelete(ModelExplorer modelExplorer) {
                if(isInternetOn(context)){
                    deleteIdea(modelExplorer);
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override public void onFeedback(ModelExplorer modelExplorer) {
                Intent i = new Intent(context, FeedbackActivity.class);
                i.putExtra("feedbackUserData",modelExplorer);
                startActivity(i);
            }

            @Override public void onNoResultFound(boolean result,boolean backUpListIsEmpty) {
                if(!backUpListIsEmpty){
                    if (result){
                        relativeLayout.setVisibility(View.VISIBLE);
                        ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_search_white_24dp));
                        tvSearchResult.setText("No Results");
                        fabNewIdea.hide();
                        rlEmptyView.setClickable(false);
                    }
                    else {
                        ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.tool));
                        tvSearchResult.setText("Plant A Seed");
                        fabNewIdea.show();
                        relativeLayout.setVisibility(View.GONE);
                    }
                }
            }
        };

        rlEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(context, NewIdeaActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                if(isInternetOn(context)){
                    fetchData();
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        fabNewIdea.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(context, NewIdeaActivity.class);
                startActivityForResult(intent, 1);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0 && dx==0){
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.touchListener(1);
                }
            }
        });

        //3. Other UI related things, including recycler view adapter
        relativeLayout.setVisibility(View.VISIBLE);
        ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.tool));
        tvSearchResult.setText("Plant A Seed");
        homeAdapter = new HomeAdapter(context,serverArrayList,homeAdapterClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(homeAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context,R.color.lightGreen));
    }

    public void fetchData() {
        //1. Show progress UI (swipe/loader)
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context,R.color.lightGreen));
        //2. Prepare listener
        ApiManagerListener apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    serverArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject resultJSONObject = jsonArray.getJSONObject(i);
                        serverArrayList.add(setter(resultJSONObject));
                    }
//                    if(serverArrayList.size() ==0){
//                        relativeLayout.setVisibility(View.VISIBLE);
//                        fabNewIdea.hide();
//                        rlEmptyView.setClickable(true);
//                    }
//                    else{
//                        relativeLayout.setVisibility(View.GONE);
//                        fabNewIdea.show();
//                    }
                    homeAdapter.updateBackupList(serverArrayList);
                    homeFragmentSearch(searchQuery, hashMapFilterApplied);
                    mSwipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override public void onError(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override public void statusCode(int statusCode) {

            }
        };
        //3. Prepare data to be sent if any
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username",PrefManager.getInstance().getString("username"));
        //4. Call api
        String url = "https://www.getideaseed.com/api/ideas/current-user";
        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.postRequest(context, url, hashMap);
        //5. Dismiss loader/swipe in success as well as error
    }

    private void deleteIdea(final ModelExplorer modelExplorer) {
        showLoader();
        ApiManagerListener apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                int index=-1;
                for(int i = 0; i<= serverArrayList.size(); i++){
                    if(serverArrayList.get(i).getUniqueId().equals(modelExplorer.getUniqueId())){
                        index = i;
                        break;
                    }
                }
                // 1. Update the array list at position i.e add,insert,remove.
                if(index>=0){
                    serverArrayList.remove(index);
                    // 2. Notify adapter at position.
                    if(serverArrayList.size() ==0){
                        relativeLayout.setVisibility(View.VISIBLE);
                        fabNewIdea.hide();
                        rlEmptyView.setClickable(true);
                    }
                    else{
                        relativeLayout.setVisibility(View.GONE);
                        fabNewIdea.show();
                    }
                    homeAdapter.notifyItemRemoved(index);
                    homeAdapter.updateBackupList(serverArrayList);
                    homeFragmentSearch(searchQuery, hashMapFilterApplied);
                }
                progressDialog.dismiss();
            }
            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();
            }
            @Override public void statusCode(int statusCode) {
            }
        };

        String url = "https://www.getideaseed.com/api/idea/" + modelExplorer.getUniqueId();
        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.deleteRequest(context, url);
    }

    private ModelExplorer setter(JSONObject js) {
        //Setting value in the modelExplorer from passed JSON Object
        ModelExplorer modelExplorer = new ModelExplorer();
        modelExplorer.setUniqueId(js.optString("_id"));
        modelExplorer.setTitle(js.optString("title"));
        modelExplorer.setDescription(js.optString("description"));
        modelExplorer.setUserName(js.optString("userName"));
        modelExplorer.setUserID(js.optString("userId"));
        modelExplorer.setPrivate(js.optBoolean("isPrivate"));
        modelExplorer.setPublic(js.optBoolean("isPublic"));
        modelExplorer.setLightbulbs(js.optInt("lightbulbs"));
        modelExplorer.setProgress(js.optInt("progress"));
        modelExplorer.setDifficulty(js.optInt("difficulty"));
        modelExplorer.setOrginality(js.optInt("originality"));
        return modelExplorer;
    }

    private void showLoader(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
                ModelExplorer modelExplorer;
                if(data !=null){
                    modelExplorer = data.getParcelableExtra("modelExplorerValue");
                    serverArrayList.add(modelExplorer);
                    homeAdapter.notifyItemInserted(serverArrayList.size());
                    homeAdapter.updateBackupList(serverArrayList);
                    homeFragmentSearch(searchQuery, hashMapFilterApplied);
                    if(serverArrayList.size() ==0){
                        relativeLayout.setVisibility(View.VISIBLE);
                        fabNewIdea.hide();
                        rlEmptyView.setClickable(true);
                    }
                    else{
                        relativeLayout.setVisibility(View.GONE);
                        fabNewIdea.show();
                    }
                }
        }
        if (requestCode == 1010){
            ModelExplorer modelExplorer;
            if(data != null){
                modelExplorer = data.getParcelableExtra("modelExplorerEditValue");
                serverArrayList.set(x,modelExplorer);
                homeAdapter.updateBackupList(serverArrayList);
                homeFragmentSearch(searchQuery, hashMapFilterApplied);
                if(serverArrayList.size() ==0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    fabNewIdea.hide();
                    rlEmptyView.setClickable(true);
                }
                else{
                    relativeLayout.setVisibility(View.GONE);
                    fabNewIdea.show();
                }
            }
        }
    }

    @Override public void onResume() {
        super.onResume();
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void homeFragmentSearch(String query,HashMap<String,String> hashMap){
        searchQuery = query;
        hashMapFilterApplied =hashMap;
        homeAdapter.setFilterHashMap(hashMap);
        homeAdapter.getFilter().filter(query);
    }
}