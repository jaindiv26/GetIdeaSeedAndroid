package com.dexa.getideaseed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dev on 24/02/18.
 */

public class NewIdeaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView difficultyProgress,originalityProgress;
    private EditText projectTitle,projectDescription;
    private SeekBar projectDifficulty, projectOriginality;
    private Button projectCancel,projectSave;
    private CheckBox projectVisibility;
    private Context context;
    private Spinner spinner;
    private ProgressDialog progressDialog;
    private ApiManagerListener apiManagerListener;
    private ModelExplorer modelExplorer = new ModelExplorer();
    private Toolbar mToolbar;
    private String ideaId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_idea);
        context = NewIdeaActivity.this;

        initComponents();
        setDataFromIntent();
    }

    public void initComponents(){
        projectTitle = findViewById(R.id.etTitle);
        projectDescription = findViewById(R.id.etDescription);
        projectDifficulty = findViewById(R.id.sbDifficulty);
        projectOriginality = findViewById(R.id.sbOriginality);
        projectVisibility = findViewById(R.id.cbVisibility);
        projectCancel = findViewById(R.id.btCancel);
        projectSave = findViewById(R.id.btSave);
        spinner = findViewById(R.id.spStatus);
        mToolbar =  findViewById(R.id.toolbar);
        difficultyProgress = findViewById(R.id.tvDifficultyProgress);
        originalityProgress = findViewById(R.id.tvOriginalityProgress);

        projectVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    projectVisibility.setText("  Public");
                }
                else {
                    projectVisibility.setText("  Private");
                }
            }
        });

        projectSave.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(TextUtils.isEmpty(projectTitle.getText().toString())){
                    Toast.makeText(context,"Please enter title",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(projectDescription.getText().toString())){
                    Toast.makeText(context,"Please enter description",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(isInternetOn(context)){
                    if(getIntent().getParcelableExtra("editUserData") ==null){
                        fetchData();
                    }
                    else {
                        editDataInServer();
                    }
                }
                else{
                    Toast.makeText(context,
                            "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        projectCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                finish();
            }
        });

        projectDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                difficultyProgress.setText(String.valueOf(progress));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        projectOriginality.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                originalityProgress.setText(String.valueOf(progress));
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        spinner.setOnItemSelectedListener(this);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("What's your idea?");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> categories = new ArrayList<String>();
        categories.add("Not Yet Started ");
        categories.add("In Progress ");
        categories.add("Done! ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void fetchData(){
        showLoader();
        apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(context,HomeFragment.class);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        intent.putExtra("modelExplorerValue",modelExplorerSetter(jsonObject));
                        setResult(1, intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError){
                    Toast.makeText(context,
                            "No Internet access", Toast.LENGTH_SHORT).show();
                }
                if(error instanceof AuthFailureError){
                    Toast.makeText(context,
                            "Incorrect user credentials", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,
                            "Something went bad", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void statusCode(int statusCode) {
                if(statusCode == 401){
                    Toast.makeText(context,
                            "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        HashMap<String,String> hashMap = new HashMap<>();
        hashMapParams(hashMap);
        String url = "https://www.getideaseed.com/api/ideas/create";
        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.postRequest(context,url,hashMap);
    }

    public void editDataInServer(){
        showLoader();
        apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(context,HomeFragment.class);
                    intent.putExtra("modelExplorerEditValue",modelExplorerEditValue());
                    setResult(1010, intent);
                    finish();
                }
            }

            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();
            }

            @Override public void statusCode(int statusCode) {

            }
        };

        HashMap<String,String> hashMap = new HashMap<>();
        hashMapParams(hashMap);
        String url = "https://www.getideaseed.com/api/idea/"+ideaId;
        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.putRequest(context,url,hashMap);
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {}

    public ModelExplorer modelExplorerSetter(JSONObject jsonObject){
        this.modelExplorer.setTitle(jsonObject.optString("title"));
        this.modelExplorer.setDescription(jsonObject.optString("description"));
        this.modelExplorer.setDifficulty(jsonObject.optInt("difficulty"));
        this.modelExplorer.setOrginality(jsonObject.optInt("originality"));
        this.modelExplorer.setProgress(jsonObject.optInt("progress"));
        this.modelExplorer.setPrivate(jsonObject.optBoolean("isPrivate"));
        this.modelExplorer.setPublic(jsonObject.optBoolean("isPublic"));
        this.modelExplorer.setUserName(PrefManager.getInstance().getString("username"));
        this.modelExplorer.setUserID(PrefManager.getInstance().getString("userID"));
        this.modelExplorer.setLightbulbs(jsonObject.optInt("lightbulbs"));
        this.modelExplorer.setUniqueId(jsonObject.optString("_id"));
        return this.modelExplorer;
    }

    public void hashMapParams(HashMap<String,String> hashMap){
        hashMap.put("description",projectDescription.getText().toString().trim());
        hashMap.put("difficulty",String.valueOf(projectDifficulty.getProgress()));
        if(projectVisibility.isChecked() == false){
            hashMap.put("isPrivate",String.valueOf(true));
            hashMap.put("isPublic", String.valueOf(false));
        }

        else{
            hashMap.put("isPublic",String.valueOf(true));
            hashMap.put("isPrivate",String.valueOf(false));
        }
        hashMap.put("lightbulbs","0");
        hashMap.put("originality",String.valueOf(projectOriginality.getProgress()));
        hashMap.put("progress",String.valueOf(spinner.getSelectedItemPosition()+1));
        hashMap.put("title",projectTitle.getText().toString().trim());
        hashMap.put("userId",PrefManager.getInstance().getString("userID"));
        hashMap.put("userName",PrefManager.getInstance().getString("username"));
        hashMap.put("_id",ideaId);
    }

    private ModelExplorer modelExplorerEditValue(){
        modelExplorer.setDescription(projectDescription.getText().toString());
        modelExplorer.setDifficulty(projectDifficulty.getProgress());
        if(projectVisibility.isChecked() == false){
            modelExplorer.setPrivate(true);
            modelExplorer.setPublic(false);
        }

        else{
            modelExplorer.setPrivate(false);
            modelExplorer.setPublic(true);
        }
        modelExplorer.setLightbulbs(0);
        modelExplorer.setOrginality(projectOriginality.getProgress());
        modelExplorer.setProgress(spinner.getSelectedItemPosition()+1);
        modelExplorer.setTitle(projectTitle.getText().toString());
        modelExplorer.setUserID(PrefManager.getInstance().getString("userID"));
        modelExplorer.setUserName(PrefManager.getInstance().getString("username"));
        modelExplorer.setUniqueId(ideaId);
        return modelExplorer;
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
            return false;
        }
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

    @Override public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void showLoader(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

    }

    private void setDataFromIntent(){
        if(getIntent().getParcelableExtra("editUserData") != null){
            modelExplorer = getIntent().getParcelableExtra("editUserData");
            projectDescription.setText(modelExplorer.getDescription());
            projectDifficulty.setProgress(modelExplorer.getDifficulty());
            difficultyProgress.setText(String.valueOf(modelExplorer.getDifficulty()));
            if(modelExplorer.getPrivate()){
                projectVisibility.setChecked(false);
                projectVisibility.setText("  Private");
            }
            else {
                projectVisibility.setChecked(true);
                projectVisibility.setText("  Public");
            }
            projectOriginality.setProgress(modelExplorer.getOrginality());
            projectSave.setText("Update");
            originalityProgress.setText(String.valueOf(modelExplorer.getOrginality()));
            spinner.setSelection(modelExplorer.getProgress()-1);
            projectTitle.setText(modelExplorer.getTitle());
            ideaId = modelExplorer.getUniqueId();
        }

    }
}
