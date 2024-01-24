package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class doctor2 extends AppCompatActivity
{
    ImageView img;
    TextView tv1,tv2,tv3;
    Button btnSendMessage,btnCall;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor2);

        img=(ImageView)findViewById(R.id.desc_img);
        tv1=(TextView)findViewById(R.id.desc_header);
        tv2=(TextView)findViewById(R.id.desc_desc);
        tv3=(TextView)findViewById(R.id.desc_phn);

        img.setImageResource(getIntent().getIntExtra("imagename",0));
        tv1.setText(getIntent().getStringExtra("header"));
        tv2.setText(getIntent().getStringExtra("desc"));
        tv3.setText(getIntent().getStringExtra("phoneNumber"));
        btnSendMessage = findViewById(R.id.button);
        btnCall = findViewById(R.id.button1);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the phone number from the TextView
                String phoneNumber = tv3.getText().toString();

                // Create an Intent to send a message
                Uri smsUri = Uri.parse("smsto:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsUri);
                intent.putExtra("sms_body", "Hello, I want to talk to you.");
                startActivity(intent);
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the phone number from the TextView
                String phoneNumber = tv3.getText().toString();

                // Create an Intent to make a phone call
                Uri phoneUri = Uri.parse("tel:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_DIAL, phoneUri);
                startActivity(intent);
            }
        });
    }
}
