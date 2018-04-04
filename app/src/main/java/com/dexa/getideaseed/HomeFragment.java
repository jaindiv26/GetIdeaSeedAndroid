package com.dexa.getideaseed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dev on 21/01/18.
 */

public class HomeFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private ImageView ivSearchIcon;
    private TextView tvSearchResult;
    public HomeAdapter homeAdapter;
    private ArrayList<ModelExplorer> serverArrayList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressDialog;
    private FloatingActionButton fabNewIdea;
    private HomeAdapterClickListener homeAdapterClickListener;
    private RelativeLayout relativeLayout;
    private RelativeLayout rlEmptyView;
    int x = 0;

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
        if(isInternetOn(context)){
            fetchData();
        }
        else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
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
                deleteIdea(modelExplorer);
            }

            @Override public void onFeedback(ModelExplorer modelExplorer) {
                Intent i = new Intent(context, FeedbackActivity.class);
                i.putExtra("feedbackUserData",modelExplorer);
                startActivity(i);
            }

            @Override public void onNoResultFound(boolean result) {
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            relativeLayout.setVisibility(View.VISIBLE);
                            ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_search_white_24dp));
                            tvSearchResult.setText("No Results");
                            rlEmptyView.setClickable(false);
                        }
                    });

                }
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override public void run() {
                            ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.tool));
                            tvSearchResult.setText("Plant A Seed");
                            relativeLayout.setVisibility(View.GONE);
                        }
                    });
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
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.touchListener(1);
            }
        });

        //3. Other UI related things, including recycler view adapter
        relativeLayout.setVisibility(View.VISIBLE);
        ivSearchIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.tool));
        tvSearchResult.setText("Plant A Seed");
        serverArrayList = new ArrayList<>();
        homeAdapter = new HomeAdapter(context,serverArrayList,homeAdapterClickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(homeAdapter);
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
                        try {
                            JSONObject resultJSONObject = jsonArray.getJSONObject(i);
                            serverArrayList.add(setter(resultJSONObject));
                        }catch (Exception e){

                        }
                    }
                    homeAdapter.updateData(serverArrayList);
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
                    homeAdapter.notifyItemRemoved(index);
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
                    if(serverArrayList.size() ==0){
                        relativeLayout.setVisibility(View.VISIBLE);
                        fabNewIdea.setVisibility(View.GONE);
                    }
                    else{
                        relativeLayout.setVisibility(View.GONE);
                        fabNewIdea.setVisibility(View.VISIBLE);
                    }
                }
        }
        if (requestCode == 1010){
            ModelExplorer modelExplorer;
            if(data != null){
                modelExplorer = data.getParcelableExtra("modelExplorerEditValue");
                serverArrayList.set(x,modelExplorer);
                homeAdapter.notifyItemChanged(x);

                if(serverArrayList.size() ==0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    fabNewIdea.setVisibility(View.GONE);
                }
                else{
                    relativeLayout.setVisibility(View.GONE);
                    fabNewIdea.setVisibility(View.VISIBLE);
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

    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            Log.v("log", "Internet is working");
            // txt_status.setText("Internet is working");
            return true;
        } else {
            Log.v("log", "No internet access");
            Toast.makeText(context,"No internet access",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void homeFragmentSearch(String query,HashMap<String,String> hashMap){
        homeAdapter.setFilterHashMap(hashMap);
        homeAdapter.getFilter().filter(query);
    }
}