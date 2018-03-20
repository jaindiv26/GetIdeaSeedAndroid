package com.dexa.getideaseed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private ImageView homeImage,exploreImage;
    private TextView homeBar,exploreBar;
    private Context context;
    private ViewPager viewPager;
    private FragmentPagerAdapter adapterViewPager;
    private ArrayList<ModelExplorer> list = new ArrayList<>();
    private Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=MainActivity.this;

        initComponents();

        onTabsClicked();
    }

    private void onTabsClicked(){
        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                homeBar.setVisibility(View.VISIBLE);
                exploreBar.setVisibility(View.INVISIBLE);
                exploreImage.setColorFilter(getResources().getColor(R.color.darkGrey));
                viewPager.setCurrentItem(0);
            }
        });

        exploreImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                exploreBar.setVisibility(View.VISIBLE);
                homeBar.setVisibility(View.INVISIBLE);
                homeImage.setColorFilter(getResources().getColor(R.color.darkGrey));
                viewPager.setCurrentItem(1);
            }
        });
    }

    private void initComponents(){
        homeImage = findViewById(R.id.ivHome);
        exploreImage = findViewById(R.id.ivExplore);
        homeBar = findViewById(R.id.tvHomeHighlight);
        exploreBar = findViewById(R.id.tvExploreHighlight);
        viewPager = findViewById(R.id.pager);
        mainToolbar = findViewById(R.id.maintoolbar);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override public void onPageSelected(int position) {

                switch (position){
                    case 0:{
                        homeBar.setVisibility(View.VISIBLE);
                        exploreBar.setVisibility(View.INVISIBLE);
                        exploreImage.setColorFilter(getResources().getColor(R.color.darkGrey));
                        break;
                    }

                    case 1:{
                        exploreBar.setVisibility(View.VISIBLE);
                        homeBar.setVisibility(View.INVISIBLE);
                        homeImage.setColorFilter(getResources().getColor(R.color.darkGrey));
                        break;
                    }
                }
            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setIcon(R.drawable.ic_lightbulb_outline_white_24dp);
        getSupportActionBar().setTitle(" Welcome "+ PrefManager.getInstance().getString("username"));
        exploreBar.setVisibility(View.GONE);
        exploreImage.setColorFilter(getResources().getColor(R.color.darkGrey));

        adapterViewPager = new MainPagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(0);
        volleyImplement();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menuItemLogOut:{
                PrefManager.getInstance().setBoolean("loggedIn",false);
                startActivity(new Intent(context,LoginActivity.class));
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void volleyImplement(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.getideaseed.com/api/ideas/public";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = response;
                        try {
                            JSONArray jsonArray = new JSONArray(str);

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject resultJSONObject = jsonArray.getJSONObject(i);
                                list.add(setter(resultJSONObject));
                            }
                            adapterViewPager = new MainPagerAdapter(getSupportFragmentManager(),list);
                            viewPager.setAdapter(adapterViewPager);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,
                        "That didn't work!", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
        modelExplorer.setLightbulbs(js.optInt("lightbulbs"));
        modelExplorer.setProgress(js.optInt("progress"));
        modelExplorer.setDifficulty(js.optInt("difficulty"));
        modelExplorer.setOrginality(js.optInt("originality"));
        return modelExplorer;
    }
}
