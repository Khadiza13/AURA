package com.example.splash;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PredictionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        TextView txtNextPeriod = findViewById(R.id.txtNextPeriod);

        // Get the predicted next period date from the intent
        String nextPeriodDate = getIntent().getStringExtra("NEXT_PERIOD_DATE");

        // Display the predicted next period date
        txtNextPeriod.setText("Predicted Next Period Date: " + nextPeriodDate);
    }
}


