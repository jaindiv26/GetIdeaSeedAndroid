package com.dexa.getideaseed;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Dev on 09/03/18.
 */

public class DefaultActivity extends AppCompatActivity {

    private Context context;
    private ImageView imageView;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmen_default);
        context = DefaultActivity.this;

        imageView = findViewById(R.id.ivPlantASeedImage);
        Glide.with(context)
                .load(R.drawable.animated_add_button)
                .asGif()
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

            }
        });
    }

}
