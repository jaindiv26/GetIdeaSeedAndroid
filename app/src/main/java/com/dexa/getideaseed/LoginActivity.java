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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SignUpEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Dev on 25/02/18.
 */

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, signUpButton;
    private TextView tvLogin;
    private EditText userName, password, email;
    private Context context;
    private ProgressDialog progressDialog;
    private ApiManagerListener apiManagerListener;
    private Toolbar mToolbar;
    private int FLAG = 0;
    private MenuItem menuItemGuestLogin;
    private boolean isGuestLoggedIn = false;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        initComponents();
        if (PrefManager.getInstance().getBoolean("loggedIn")){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        buildShortcut();
    }

    public void initComponents() {
        userName = findViewById(R.id.etUserName);
        password = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.btLogin);
        signUpButton = findViewById(R.id.btSignUp);
        email = findViewById(R.id.etNewUserEmailId);
        tvLogin = findViewById(R.id.tvLogin);
        mToolbar = findViewById(R.id.loginToolbar);

        email.setVisibility(View.GONE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        if (getIntent() != null) {
            if (getIntent().hasExtra("showLoginView")) {
                isGuestLoggedIn = getIntent().getBooleanExtra("showLoginView", false);
                email.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.VISIBLE);
                tvLogin.setText("Log In");
                loginButton.setText("Log In");
                FLAG = 0;
            }
            if (getIntent().hasExtra("showRegisterView")) {
                isGuestLoggedIn = getIntent().getBooleanExtra("showRegisterView", false);
                email.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.INVISIBLE);
                loginButton.setText("Sign Up");
                tvLogin.setText("Sign Up");
                FLAG = 0;
            }
            if (isGuestLoggedIn) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (FLAG == 0) {
                    if (TextUtils.isEmpty(userName.getText().toString())) {
                        Toast.makeText(context, "Please enter Username",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password.getText().toString())) {
                        Toast.makeText(context, "Please enter password",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (isInternetOn(context)) {
                        fetchData();
                    } else {
                        Toast.makeText(context,
                                "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else if (FLAG == 1) {
                    if (TextUtils.isEmpty(userName.getText().toString())) {
                        Toast.makeText(context, "Please enter Username",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (TextUtils.isEmpty(email.getText().toString())) {
                        Toast.makeText(context, "Please enter Email-Id",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password.getText().toString())) {
                        Toast.makeText(context, "Please enter password",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (isInternetOn(context)) {
                        signUpFetchData();
                    } else {
                        Toast.makeText(context,
                                "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (FLAG == 0) {
                        if (TextUtils.isEmpty(userName.getText().toString())) {
                            Toast.makeText(context, "Please enter Username",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if (TextUtils.isEmpty(password.getText().toString())) {
                            Toast.makeText(context, "Please enter password",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if (isInternetOn(context)) {
                            fetchData();
                        } else {
                            Toast.makeText(context,
                                    "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    } else if (FLAG == 1) {
                        if (TextUtils.isEmpty(userName.getText().toString())) {
                            Toast.makeText(context, "Please enter Username",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }
                        if (TextUtils.isEmpty(email.getText().toString())) {
                            Toast.makeText(context, "Please enter Email-Id",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }

                        if (TextUtils.isEmpty(password.getText().toString())) {
                            Toast.makeText(context, "Please enter password",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }

                        if (isInternetOn(context)) {
                            signUpFetchData();
                        } else {
                            Toast.makeText(context,
                                    "No Internet Connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                return false;
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                email.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.INVISIBLE);
                loginButton.setText("Sign Up");
                tvLogin.setText("Sign Up");
                FLAG = 1;
            }
        });
    }

    private void buildShortcut(){
            if(PrefManager.getInstance().getBoolean("loggedIn")){
                AppShortcutUtil appShortcutUtil = new AppShortcutUtil();
                appShortcutUtil.changeShortcutOnLoggedIn(context);
            }

            else {
                AppShortcutUtil appShortcutUtil = new AppShortcutUtil();
                appShortcutUtil.changeShortcutOnSignedOut(context);
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

    private ModelUser modelUser(JSONObject js) {
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

    private void fetchData() {
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
                        ModelLoginData modelLoginData = new ModelLoginData();
                        modelLoginData.setModelUser(modelUser(userObj));

                        progressDialog.dismiss();
                        if (!isGuestLoggedIn) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        Answers.getInstance().logLogin(new LoginEvent()
                                .putCustomAttribute("username", modelLoginData.getModelUser().getUserName())
                                .putCustomAttribute("userID", modelLoginData.getModelUser().getUserId()));
                        PrefManager.getInstance().setBoolean("loggedIn", true);
                        PrefManager.getInstance().setString("username", modelLoginData.getModelUser().getUserName());
                        PrefManager.getInstance().setString("userID", modelLoginData.getModelUser().getUserId());
                        buildShortcut();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();

                if (error instanceof NoConnectionError) {
                    Toast.makeText(LoginActivity.this,
                            "No Internet access", Toast.LENGTH_LONG).show();
                }
                if (error instanceof AuthFailureError) {
                    Toast.makeText(LoginActivity.this,
                            "Incorrect user credentials", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Something went bad", Toast.LENGTH_LONG).show();
                }
            }

            @Override public void statusCode(int statusCode) {
                if (statusCode == 401) {
                    Toast.makeText(LoginActivity.this,
                            "Invalid credentials. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        };
        String url = "https://www.getideaseed.com/api/login";

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("username", userName.getText().toString().trim());
        hashMap.put("password", password.getText().toString().trim());

        ApiManager apiManager = new ApiManager(apiManagerListener);
        apiManager.postRequest(context, url, hashMap);
    }

    public void signUpFetchData() {
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
                    startActivity(intent);
                    Answers.getInstance().logSignUp(new SignUpEvent()
                            .putCustomAttribute("username", userName.getText().toString().trim())
                            .putCustomAttribute("email", email.getText().toString().trim()));
                    PrefManager.getInstance().setBoolean("loggedIn", true);
                    PrefManager.getInstance().setString("username", userName.getText().toString());
                    buildShortcut();
                    finish();
                }
            }

            @Override public void onError(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof NoConnectionError) {
                    Toast.makeText(context,
                            "No Internet access", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String response = new String(error.networkResponse.data, "utf-8");

                        if (TextUtils.isEmpty(response)) {
                            Toast.makeText(context, "Something went bad", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(context,
                                "Something went bad", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override public void statusCode(int statusCode) {
                if (statusCode == 403) {
                    Toast.makeText(context,
                            "Invalid credentials. Please try again.", Toast.LENGTH_LONG).show();
                }

            }
        };

        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", userName.getText().toString().trim());
            hashMap.put("password", password.getText().toString().trim());
            hashMap.put("email", email.getText().toString().trim());

            String url = "https://www.getideaseed.com/api/register";

            ApiManager apiManager = new ApiManager(apiManagerListener);
            apiManager.postRequest(context, url, hashMap);
        } catch (Exception e) {
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                if (FLAG == 0) {
                    finish();
                } else if (FLAG == 1) {
                    email.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    signUpButton.setVisibility(View.VISIBLE);
                    tvLogin.setText("Log In");
                    loginButton.setText("Log In");
                    if (isGuestLoggedIn) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    } else {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    FLAG = 0;
                }
                return true;
            }

            case R.id.guestLogin: {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                PrefManager.getInstance().setBoolean("loggedIn", false);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        menuItemGuestLogin = menu.findItem(R.id.guestLogin);
        if (isGuestLoggedIn) {
            menuItemGuestLogin.setVisible(false);
        } else {
            menuItemGuestLogin.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override public void onBackPressed() {
        if (FLAG == 1) {
            email.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.VISIBLE);
            tvLogin.setText("Log In");
            loginButton.setText("Log In");
            FLAG = 0;
        } else if (FLAG == 0) {
            finish();
            super.onBackPressed();
        }
    }
}
