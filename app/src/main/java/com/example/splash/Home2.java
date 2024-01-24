package com.example.splash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class Home2 extends AppCompatActivity {
    ImageButton loc,audio,nearby,sos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        loc = findViewById(R.id.imageButton3);
        audio = findViewById(R.id.imageButton4);
        nearby = findViewById(R.id.imageButton5);
        sos = findViewById(R.id.imageButton2);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home2.this, LocationActivity.class);
                startActivity(intent);
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home2.this, Demo.class);
                startActivity(intent);
            }
        });
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home2.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home2.this, SOS.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();

        if(id==R.id.security)
        {
            Intent intent=new Intent(Home2.this, Home2.class);
            startActivity(intent);
            return true;
        }
        else
        if(id==R.id.comm)
        {
            Intent intent=new Intent(Home2.this, Community.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}