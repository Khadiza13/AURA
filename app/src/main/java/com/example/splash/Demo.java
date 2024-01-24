package com.example.splash;

import static com.example.splash.R.color.aura;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Demo extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;
    private static String file_path =null;
    private ImageView imageView = null;
    TextView txt  ;
    File file;
    private RecordButton recordButton = null;
    private MediaRecorder recorder = null;
    Date current_time;
    File audioFile;
    Long date;
    private PlayButton   playButton = null;
    private MediaPlayer   player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Record to the external cache directory for visibility

        fileName = getExternalCacheDir().getAbsolutePath();

        Long date=new Date().getTime();
        Date current_time = new Date(Long.valueOf(date));
        fileName +="/"+current_time+".3gp";


        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        ConstraintLayout constraintLayout = new ConstraintLayout(this);

        ConstraintLayout xmlLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_audiorecord, null);
        txt = xmlLayout.findViewById(R.id.textaudio);
        LinearLayout ll = new LinearLayout(this);
        recordButton = new RecordButton(this);
        LinearLayout.LayoutParams recordButtonLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0);
        recordButtonLayoutParams.setMargins(290, 1020, 0, 26);
        recordButton.setBackgroundResource(R.drawable.btn_record);
        recordButton.setWidth(450);
        recordButton.setHeight(120);
        recordButton.setAllCaps(false);
        recordButton.setTextColor(getColor(R.color.white));
        recordButton.setTextSize(25);
        ll.addView(recordButton, recordButtonLayoutParams);

        playButton = new PlayButton(this);
        LinearLayout.LayoutParams playButtonLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0);
        playButtonLayoutParams.setMargins(-450, 1190, 0, 26);
        playButton.setBackgroundResource(R.drawable.btn_record);
        playButton.setTextColor(getColor(R.color.white));
        playButton.setAllCaps(false);
        playButton.setWidth(450);
        playButton.setHeight(120);
        playButton.setTextSize(25);
        ll.addView(playButton, playButtonLayoutParams);
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        // Add the LinearLayout to the RelativeLayout
        constraintLayout.addView(ll);

        // Add the inflated XML layout to the RelativeLayout
        constraintLayout.addView(xmlLayout);

        // Set the content view to the RelativeLayout
        setContentView(constraintLayout);
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        Toast.makeText(this,"File is saved in"+ fileName,Toast.LENGTH_LONG).show();
    }

    class RecordButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {

                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                    txt.setText("It is recording...");
                } else {
                    setText("Start recording");
                    txt.setText("Record Your Audio ");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(getExternalCacheDir().getAbsolutePath());
                intent.setDataAndType(uri, "*/*");
                startActivity(intent);
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Open audio");
            setOnClickListener(clicker);
        }
    }




    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}