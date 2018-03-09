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

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Dev on 25/02/18.
 */

public class LoginActivity extends AppCompatActivity {

    private Button loginButton,signUpButton;
    private EditText userName,password;
    private ImageView imageView;
    private Context context;
    private ProgressDialog progressDialog;
    private ApiManagerListener apiManagerListener;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        initComponents();

        Glide.with(context)
                .load(R.drawable.animated_logo2)
                .asGif()
                .into(imageView);

        if (PrefManager.getInstance().getBoolean("loggedIn")) {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }

    public void initComponents(){
        userName = findViewById(R.id.etUserName);
        password = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btLogin);
        signUpButton = findViewById(R.id.btSignUp);
        imageView = findViewById(R.id.ivAnimatedGIF);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(TextUtils.isEmpty(userName.getText().toString())){
                    Toast.makeText(context,"Please enter Username",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(password.getText().toString())){
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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

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

    private ModelUser modelUser (JSONObject js) throws JSONException{
        ModelUser modelUser = new ModelUser();
        modelUser.setUserId(js.optString("_id"));
        modelUser.setUserSalt(js.optString("salt"));
        modelUser.setUserHash(js.optString("hash"));
        modelUser.setUserName(js.optString("username"));
        modelUser.setEmail(js.optString("email"));
        modelUser.set__v(js.optInt("__v"));
        modelUser.setUserActive(js.optBoolean("active"));
        modelUser.setUserRegisteredDate(js.optString("registeredDate"));
        modelUser.setEarlyAdopter(js.optBoolean("isEarlyAdopter"));
        return modelUser;
    }

    private void fetchData(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        apiManagerListener = new ApiManagerListener() {
            @Override public void onSuccess(String response) {
                if (!response.equals(null)) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject userObj = jsonObject.getJSONObject("user");
                        ModelLoginData  modelLoginData = new ModelLoginData();
                        modelLoginData.setModelUser(modelUser(userObj));

                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        PrefManager.getInstance().setBoolean("loggedIn",true);
                        PrefManager.getInstance().setString("username",modelLoginData.getModelUser().getUserName());
                        PrefManager.getInstance().setString("userID",modelLoginData.getModelUser().getUserId());
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();

                if(error instanceof NoConnectionError){
                    Toast.makeText(LoginActivity.this,
                            "No Internet access", Toast.LENGTH_LONG).show();
                }
                if(error instanceof AuthFailureError){
                    Toast.makeText(LoginActivity.this,
                            "Incorrect user credentials", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginActivity.this,
                            "Something went bad", Toast.LENGTH_LONG).show();
                }
            }

            @Override public void statusCode(int statusCode) {
                if(statusCode == 401){
                    Toast.makeText(LoginActivity.this,
                            "Invalid credentials. Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        };
        String url = "https://www.getideaseed.com/api/login";

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("username",userName.getText().toString());
        hashMap.put("password",password.getText().toString());

        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.postRequest(context,url,hashMap);
    }


}
