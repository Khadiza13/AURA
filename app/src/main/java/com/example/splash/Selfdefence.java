package com.example.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;

public class Selfdefence extends AppCompatActivity {

    ImageButton imageButton,imageButton2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfdefence);

        ImageSlider imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> slidemodals = new ArrayList<>();

        slidemodals.add(new SlideModel(R.drawable.image5, ScaleTypes.FIT));
        slidemodals.add(new SlideModel(R.drawable.image3, ScaleTypes.FIT));
        slidemodals.add(new SlideModel(R.drawable.image2, ScaleTypes.FIT));


        imageSlider.setImageList(slidemodals,ScaleTypes.FIT);

        imageButton = (ImageButton) findViewById(R.id.imgtip);
        imageButton2 = (ImageButton) findViewById(R.id.imgself);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Selfdefence.this, tips.class);
                startActivity(intent);
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Selfdefence.this,Defensevideos.class);
                startActivity(intent);
            }
        });
    }
}
