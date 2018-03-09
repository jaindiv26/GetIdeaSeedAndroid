package com.dexa.getideaseed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import java.util.HashMap;

/**
 * Created by Dev on 25/02/18.
 */

public class SignUpActivity extends AppCompatActivity {

    private EditText etUsername,etEmailId,etPassword;
    private Button btSignUp;
    private Context context;
    ProgressDialog progressDialog;
    private ApiManagerListener apiManagerListener;
    private ImageView imageView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        context = SignUpActivity.this;

        initComponenets();

        Glide.with(context)
                .load(R.drawable.animated_logo2)
                .asGif()
                .into(imageView);
    }

    public void initComponenets(){
        etUsername = findViewById(R.id.etNewUsername);
        etEmailId = findViewById(R.id.etEmailId);
        etPassword = findViewById(R.id.etNewPassword);
        btSignUp = findViewById(R.id.btSignUp);
        imageView = findViewById(R.id.ivAnimatedGIF2);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                if(TextUtils.isEmpty(etUsername.getText().toString())){
                    Toast.makeText(context,"Please enter Username",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    Toast.makeText(context,"Please enter password",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(etPassword.getText().toString())){
                    Toast.makeText(context,"Please enter password",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if(isInternetOn(context)){
                    fetchData();
                }

                else{
                    Toast.makeText(context,
                            "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void fetchData(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                if (!response.equals(null)) {
                    progressDialog.dismiss();
                    //startActivity(new Intent(context,MainActivity.class));
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    PrefManager.getInstance().setBoolean("loggedIn",true);
                    finish();
                }
            }

            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();
                if(error instanceof NoConnectionError){
                    Toast.makeText(context,
                            "No Internet access", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        String response = new String(error.networkResponse.data,"utf-8");

                        if(TextUtils.isEmpty(response)){
                            Toast.makeText(context,"Something went bad", Toast.LENGTH_LONG).show();
                        }

                        else{
                            Toast.makeText(context,response, Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e){
                        Toast.makeText(context,
                                "Something went bad", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override public void statusCode(int statusCode) {
                if(statusCode == 403){
                    Toast.makeText(context,
                            "Invalid credentials. Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        };

        try{
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("username",etUsername.getText().toString());
            hashMap.put("password",etPassword.getText().toString());
            hashMap.put("email",etEmailId.getText().toString());

            String url = "https://www.getideaseed.com/api/register";

            ApiManager apiManager = new ApiManager(apiManagerListener);
            apiManager.postRequest(context,url,hashMap);


        }
        catch (Exception e){

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
            return false;
        }
    }
}
