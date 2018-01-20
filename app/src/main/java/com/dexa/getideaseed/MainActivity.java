package com.dexa.getideaseed;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    RecyclerView seedList;
    ImageView homeImage,exploreImage;
    TextView homeText,exploreText,homeBar,exploreBar;
    Context context;
    private SeedViewRecycler recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=MainActivity.this;

        seedList = findViewById(R.id.rvSeedFeed);
        homeImage = findViewById(R.id.ivHome);
        exploreImage = findViewById(R.id.ivExplore);
        homeText = findViewById(R.id.tvHomeButton);
        exploreText = findViewById(R.id.tvExploreButton);
        homeBar = findViewById(R.id.tvHomeHighlight);
        exploreBar = findViewById(R.id.tvExploreHighlight);

        homeBar.setVisibility(View.GONE);
        homeText.setTextColor(getResources().getColor(R.color.darkGrey));
        exploreText.setTextColor(getResources().getColor(R.color.lightGreen));
        homeImage.setColorFilter(getResources().getColor(R.color.darkGrey));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        seedList.setLayoutManager(layoutManager);
        seedList.setHasFixedSize(true);
        recyclerAdapter = new SeedViewRecycler(context,5);
        seedList.setAdapter(recyclerAdapter);

        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                homeBar.setVisibility(View.VISIBLE);
                exploreBar.setVisibility(View.INVISIBLE);
                exploreText.setTextColor(getResources().getColor(R.color.darkGrey));
                homeText.setTextColor(getResources().getColor(R.color.lightGreen));
                exploreImage.setColorFilter(getResources().getColor(R.color.darkGrey));
            }
        });

        exploreImage.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                exploreBar.setVisibility(View.VISIBLE);
                homeBar.setVisibility(View.INVISIBLE);
                homeText.setTextColor(getResources().getColor(R.color.darkGrey));
                exploreText.setTextColor(getResources().getColor(R.color.lightGreen));
                homeImage.setColorFilter(getResources().getColor(R.color.darkGrey));
            }
        });


    }
}
