package com.example.splash;

import static com.example.splash.R.color.aura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Animation topAnim ;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences= getSharedPreferences(Login.PREFS_NAME,0);
                boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);
                if(hasLoggedIn){
                    Intent intent=new Intent(MainActivity.this, Home2.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent=new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    finish();

                }

            }
        },4500);

        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        image= findViewById(R.id.imageView);
        image.setAnimation(topAnim);


    }
}