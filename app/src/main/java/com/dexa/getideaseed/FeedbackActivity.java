package com.dexa.getideaseed;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dev on 26/02/18.
 */

public class FeedbackActivity extends AppCompatActivity {

    private TextView tvIdeaUsername,tvIdeaTitle,tvIdeaDescription,tvNoOfBulbs,tvGuestUsername,tvDifficultyProgress,tvOriginalityProgress;
    private EditText etGuestComment;
    private Button btSubmit;
    private ProgressBar pbOriginality,pbDifficulty;
    private Context context;
    private RecyclerView recyclerView;
    private String uniqueID;
    private LinearLayoutManager linearLayoutManager;
    private FeedbackAdapter feedbackAdapter;
    private ModelExplorer modelExplorer;
    private ApiManagerListener apiManagerListener;
    private ArrayList<ModelFeedback> modelFeedbackArrayList = new ArrayList<>();
    private ClickListener clickListener;
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        context = FeedbackActivity.this;
        initComponents();
        if (getIntent() != null) {
            modelExplorer = getIntent().getParcelableExtra("feedbackUserData");
            tvIdeaUsername.setText(modelExplorer.getUserName());
            tvIdeaTitle.setText(modelExplorer.getTitle());
            tvIdeaDescription.setText(modelExplorer.getDescription());
            pbOriginality.setProgress(modelExplorer.getOrginality());
            pbDifficulty.setProgress(modelExplorer.getDifficulty());
            tvNoOfBulbs.setText(Integer.toString(modelExplorer.getLightbulbs()));
            uniqueID = modelExplorer.getUniqueId();
            tvOriginalityProgress.setText(String.valueOf(modelExplorer.getOrginality()));
            tvDifficultyProgress.setText(String.valueOf(modelExplorer.getDifficulty()));
        }
        fetchData();
    }

    private void initComponents(){
        tvIdeaUsername = findViewById(R.id.tvFeedbackUseName);
        tvIdeaTitle = findViewById(R.id.tvFeedbackProjectTitle);
        tvIdeaDescription = findViewById(R.id.tvFeedbackProjectDescription);
        tvNoOfBulbs = findViewById(R.id.tvFeedbackNoOfBulb);
        pbOriginality = findViewById(R.id.pbFeedbackOriginality);
        pbDifficulty = findViewById(R.id.pbFeedbackDifficulty);
        recyclerView = findViewById(R.id.rvFeddbackComments);
        recyclerView.setNestedScrollingEnabled(false);
        tvGuestUsername = findViewById(R.id.tvGuestUsername);
        etGuestComment = findViewById(R.id.etGuestComment);
        btSubmit = findViewById(R.id.btSubmitButton);
        mToolbar =findViewById(R.id.feedbackToolbar);
        tvDifficultyProgress = findViewById(R.id.tvFeedbackDifficultyProgress);
        tvOriginalityProgress = findViewById(R.id.tvFeedbackOriginalityProgress);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(TextUtils.isEmpty(etGuestComment.getText().toString())){
                    Toast.makeText(context,"Write some feedback",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    apiManagerListener = new ApiManagerListener() {
                        @Override public void onSuccess(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                modelFeedbackArrayList.add(modelFeedbackGuestUser(jsonObject));
                                feedbackAdapter.notifyItemInserted(modelFeedbackArrayList.size()-1);
                                etGuestComment.setText("");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override public void onError(VolleyError error) {
                            int a = 5;
                        }

                        @Override public void statusCode(int statusCode) {
                            int a = 8;
                        }
                    };
                    String url = "https://www.getideaseed.com/api/idea/feedback";
                    HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("message", etGuestComment.getText().toString());
                        hashMap.put("from",tvGuestUsername.getText().toString());
                        hashMap.put("fromUserId",PrefManager.getInstance().getString("userID"));
                        hashMap.put("ideaId",uniqueID);
                    ApiManager apiManager = new ApiManager(apiManagerListener);
                    apiManager.postRequest(context,url,hashMap);
                }
            }
        });
        clickListener = new ClickListener() {
            @Override public void onClick(ModelExplorer modelExplorer) {
            }

            @Override public void onClick(ModelFeedback modelFeedback,String comment) {
                if(modelFeedback.getMessage().equals(comment)){
                    for(int i=0;i<modelFeedbackArrayList.size();i++){
                        if(modelFeedback.getFeedbackId().equals(modelFeedbackArrayList.get(i).getFeedbackId())){
                            feedbackAdapter.notifyItemChanged(i,modelFeedback);
                            break;
                        }
                    }
                }
                else {
                    editComment(modelFeedback,comment);
                }
            }

            @Override public void onResultFound(boolean result) {

            }

            @Override public void onLightBulbClicked(ModelExplorer modelExplorer, int numberOfBulbs) {

            }
        };
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        linearLayoutManager = new LinearLayoutManager(context);
        feedbackAdapter = new FeedbackAdapter(context, modelFeedbackArrayList,clickListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(feedbackAdapter);
    }

    private void fetchData(){
        String username = PrefManager.getInstance().getString("username");
        tvGuestUsername.setText(username);
        apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject resultJSONObject = jsonArray.getJSONObject(i);
                        modelFeedbackArrayList.add(modelFeedbackGuestUser(resultJSONObject));
                    }

                    linearLayoutManager = new LinearLayoutManager(context);
                    feedbackAdapter = new FeedbackAdapter(context, modelFeedbackArrayList,clickListener);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(feedbackAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override public void onError(VolleyError error) {

            }

            @Override public void statusCode(int statusCode) {
            }
        };
        String url ="https://www.getideaseed.com/api/idea/feedback/"+uniqueID;
        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.getRequest(context,url);
    }

    private ModelFeedback modelFeedbackGuestUser (JSONObject js) {
        ModelFeedback modelFeedback = new ModelFeedback();
        modelFeedback.setFeedbackId(js.optString("_id"));
        modelFeedback.setUserId(js.optString("ideaId"));
        modelFeedback.setMessage(js.optString("message"));
        modelFeedback.setFrom(js.optString("from"));
        modelFeedback.setFromUserId(js.optString("fromUserId"));
        return modelFeedback;
    }

    public void editComment(final ModelFeedback modelFeedback, final String comment){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                progressDialog.dismiss();
                modelFeedback.setMessage(comment);
                for(int i=0;i<modelFeedbackArrayList.size();i++){
                    if(modelFeedback.getFeedbackId().equals(modelFeedbackArrayList.get(i).getFeedbackId())){
                        feedbackAdapter.notifyItemChanged(i,modelFeedback);
                        break;
                    }
                }
                Toast.makeText(context,"Comment Edited",
                        Toast.LENGTH_LONG).show();

            }

            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();
            }

            @Override public void statusCode(int statusCode) {
                int x=5;
            }
        };

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("message",comment.trim());
        String url = "https://www.getideaseed.com/api/idea/feedback/"+modelFeedback.getFeedbackId();

        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.putRequest(context,url,hashMap);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
