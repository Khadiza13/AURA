package com.example.splash;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class mens extends AppCompatActivity {

    private EditText editLastPeriod, editCycleLength;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mens);

        editLastPeriod = findViewById(R.id.editLastPeriod);
        editCycleLength = findViewById(R.id.editCycleLength);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Set the showDatePickerDialog() function for editLastPeriod.setOnClickListener
        editLastPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // Set the showPredictedDateDialog() function for btnPredict.setOnClickListener
        Button btnPredict = findViewById(R.id.btnPredict);
        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predictNextPeriod();
            }
        });

        // Set the showSavedDateDialog() function for btnNextDate.setOnClickListener
        Button btnNextDate = findViewById(R.id.btnNextDate);
        btnNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSavedDateDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                editLastPeriod.setText(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void predictNextPeriod() {
        String lastPeriodStr = editLastPeriod.getText().toString();
        String cycleLengthStr = editCycleLength.getText().toString();

        if (lastPeriodStr.isEmpty() || cycleLengthStr.isEmpty()) {
            Toast.makeText(this, "Please enter both last period date and cycle length", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date lastPeriodDate = dateFormat.parse(lastPeriodStr);
            int cycleLength = Integer.parseInt(cycleLengthStr);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(lastPeriodDate);
            calendar.add(Calendar.DAY_OF_YEAR, cycleLength);

            Date nextPeriodDate = calendar.getTime();

            showPredictedDateDialog(calendar);

            // Save the predicted date to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("PREDICTED_DATE", new SimpleDateFormat("yyyy-MM-dd").format(nextPeriodDate));
            editor.apply();

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPredictedDateDialog(final Calendar predictedDate) {
        int year = predictedDate.get(Calendar.YEAR);
        int month = predictedDate.get(Calendar.MONTH);
        int day = predictedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Save the selected predicted date
                Calendar selectedPredictedDate = Calendar.getInstance();
                selectedPredictedDate.set(year, monthOfYear, dayOfMonth);

                // You can perform additional actions or leave it empty
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showSavedDateDialog() {
        // Retrieve the saved predicted date from SharedPreferences
        String savedDateStr = sharedPreferences.getString("PREDICTED_DATE", "");

        if (!savedDateStr.isEmpty()) {
            // Show the saved date using a DatePickerDialog
            Calendar savedDate = Calendar.getInstance();
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(savedDateStr);
                savedDate.setTime(date);

                showPredictedDateDialog(savedDate);

                // Schedule a notification if the week of the saved date is approaching
                Calendar currentCalendar = Calendar.getInstance();
                long timeDifference = savedDate.getTimeInMillis() - currentCalendar.getTimeInMillis();
                long daysDifference = timeDifference / (24 * 60 * 60 * 1000);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "No saved date found", Toast.LENGTH_SHORT).show();
        }
    }
    private void scheduleNotification(Calendar scheduledDate) {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra("NOTIFICATION_MESSAGE", "Your notification message here");

        int uniqueId = (int) System.currentTimeMillis(); // Unique ID for pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            // Set the alarm to trigger 7 days before the saved date
            long triggerTime = scheduledDate.getTimeInMillis() - (7 * 24 * 60 * 60 * 1000);
            alarmManager.set(AlarmManager.RTC, triggerTime, pendingIntent);
        }
    }





}
