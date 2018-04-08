package com.dexa.getideaseed;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dev on 20/01/18.
 */

public class ExplorerFragment extends Fragment{

    private Context context;
    private RecyclerView recyclerView;
    private ExplorerAdapter explorerAdapter;
    private ArrayList<ModelExplorer> explorerServerArrayList = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ApiManagerListener apiManagerListener;
    private RelativeLayout relativeLayout;
    private String searchQuery;
    private HashMap<String, String> hashMapFilterApplied = new HashMap<>();

    public static ExplorerFragment newInstance(Bundle bundle) {
        ExplorerFragment instance = new ExplorerFragment();
        instance.setArguments(bundle);
        return instance;
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                                                 @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);
        initComponents(view);
        if(isInternetOn(context)){
            fetchData();
        }
        else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        return view;
    }

    private void initComponents(View view) {
        recyclerView = view.findViewById(R.id.rvExplorer);
        mSwipeRefreshLayout = view.findViewById(R.id.srExplorer);
        relativeLayout = view.findViewById(R.id.rvOnNoResultFound);

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

        ClickListener clickListener = new ClickListener() {
            @Override public void onClick(ModelExplorer modelExplorer) {
                if(!PrefManager.getInstance().getBoolean("loggedIn")){
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.showLoginSignUpDialog();
                }
                else {
                    Intent i = new Intent(context, FeedbackActivity.class);
                    i.putExtra("feedbackUserData",modelExplorer);
                    context.startActivity(i);
                }
            }

            @Override public void onClick(ModelFeedback modelFeedback,String comment) {

            }

            @Override public void onResultFound(boolean result) {
                if(result){
                    relativeLayout.setVisibility(View.VISIBLE);
                }
                else {
                    relativeLayout.setVisibility(View.GONE);
                }
            }

            @Override public void onLightBulbClicked(ModelExplorer modelExplorer, int numberOfBulbs) {

                lightBulbButton(modelExplorer,numberOfBulbs);
            }

        };
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0 && dx==0){
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.touchListener(1);
                }
            }
        });
        relativeLayout.setVisibility(View.GONE);
        explorerAdapter = new ExplorerAdapter(context, explorerServerArrayList,clickListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(explorerAdapter);
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fetchData(){
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context,R.color.lightGreen));

        ApiManagerListener apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                String str = response;
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    explorerServerArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject resultJSONObject = jsonArray.getJSONObject(i);
                        explorerServerArrayList.add(setter(resultJSONObject));
                    }
                    explorerAdapter.updateBackupList(explorerServerArrayList);
                    explorerFragmentSearch(searchQuery, hashMapFilterApplied);
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

        String url ="https://www.getideaseed.com/api/ideas/public";
        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.getRequest(context,url);
    }

    private ModelExplorer setter(JSONObject js) throws JSONException {
        //Setting value in the modelExplorer from passed JSON Object
        ModelExplorer modelExplorer = new ModelExplorer();
        modelExplorer.setUniqueId(js.optString("_id"));
        modelExplorer.setTitle(js.optString("title"));
        modelExplorer.setDescription(js.optString("description"));
        modelExplorer.setUserName(js.optString("userName"));
        modelExplorer.setUserID(js.optString("userId"));
        modelExplorer.setPrivate(js.optBoolean("isPrivate"));
        modelExplorer.setPublic(js.optBoolean("isPublic"));
        // For getting value from JSON array isLightbulbedBy
        JSONArray jsonArray = js.getJSONArray("isLightbulbedBy");
        ArrayList<String> isLightBulbByArrayList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            String arrayElement = jsonArray.getString(i);
            isLightBulbByArrayList.add(arrayElement);    //Setting value at index i into isLightBulbByArrayList
        }
        modelExplorer.setIsLightbulbedBy(isLightBulbByArrayList);

        for(int i=0;i<isLightBulbByArrayList.size();i++){
            if(modelExplorer.getIsLightbulbedBy().get(i).equals(PrefManager.getInstance().getString("userID"))){
                modelExplorer.setHasLiked(true);
                break;
            }
        }
        modelExplorer.setLightbulbs(js.optInt("lightbulbs"));
        modelExplorer.setProgress(js.optInt("progress"));
        modelExplorer.setDifficulty(js.optInt("difficulty"));
        modelExplorer.setOrginality(js.optInt("originality"));
        return modelExplorer;
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(context == null){
            return;
        }
        else{
            if (isVisibleToUser){
                if(isInternetOn(context)){
                    fetchData();
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        }

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

    public void explorerFragmentSearch(String query,HashMap<String,String> hashMap){
        searchQuery = query;
        hashMapFilterApplied =hashMap;
        explorerAdapter.getFilter().filter(query);
        explorerAdapter.getData(hashMap);
    }

    private void lightBulbButton(final ModelExplorer modelExplorer, final int numberOfBulbs){
        apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                if (response != null) {
                    String string = response;
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        String message = jsonObject.optString("message");
                        if(message.equals("lightbulb incremented")){
                            modelExplorer.setHasLiked(true);
                        }
                        else {
                            modelExplorer.setHasLiked(false);
                        }
                        modelExplorer.setLightbulbs(numberOfBulbs);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override public void onError(VolleyError error) {
            }

            @Override public void statusCode(int statusCode) {
            }
        };
        String url = "https://www.getideaseed.com/api/idea/lightbulb";

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("ideaId",modelExplorer.getUniqueId());
        hashMap.put("ideaName",modelExplorer.getTitle());
        hashMap.put("lightbulbGiverId",PrefManager.getInstance().getString("userID"));
        hashMap.put("lightbulbReceiverId",modelExplorer.getUserID());
        hashMap.put("lightbulbs", String.valueOf(numberOfBulbs));
        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.postRequest(context,url,hashMap);
    }
}
