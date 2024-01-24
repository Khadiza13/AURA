package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.ArrayList;

public class Defensevideos extends AppCompatActivity {

    VideoView videoView2;
    ListView listView2;
    ArrayList<String>videoList;
    ArrayAdapter adapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defensevideos);

        videoView2=(VideoView) findViewById(R.id.videoView2);
        listView2=(ListView) findViewById(R.id.listView2);
        videoList=new ArrayList<>();
        videoList.add("7 tips");
        videoList.add("Kerate Moves");
        videoList.add("Tricks Women should know");
        videoList.add("Self-defense");
        videoList.add("Yoga for mental peace");
        videoList.add("Cyber Security");

        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,videoList);
        listView2.setAdapter(adapter);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        videoView2.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vdo1));
                        break;
                    case 1:
                        videoView2.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video2));
                        break;
                    case 2:
                        videoView2.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vdo3));
                        break;
                    case 3:
                        videoView2.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vdo4));
                        break;
                    case 4:
                        videoView2.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vdo5));
                        break;
                    case 5:
                        videoView2.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vdo6));
                        break;
                    default:
                        break;
                }
                videoView2.setMediaController(new MediaController(Defensevideos.this));
                videoView2.requestFocus();
                videoView2.start();
            }
        });
    }
}