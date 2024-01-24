package com.example.splash;
import static android.app.Service.START_STICKY;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SOS extends AppCompatActivity {

    // References for UI elements
    TextView textView, textView2, textView3;
    ImageButton imageButton4, imageButton5, imageButton6;
    Button button, deleteButton,saveButton;
    PowerManager.WakeLock wakeLock;
    private final Handler handler = new Handler();
    private static final long DELAY_BEFORE_NEXT_CALL = 5000; // Adjust the delay as needed


    // Array to store contact numbers
    String[] cnumber = new String[3];

    // Constants
    static final int PICK_CONTACT = 1;
    public static final int POWER_BUTTON_CLICK_THRESHOLD = 3;
    private SharedPreferences sharedPreferences;
    public int powerButtonClickCount = 0;

    // onCreate method
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        // Initialize UI elements
        textView = findViewById(R.id.textView4);
        textView2 = findViewById(R.id.textView7);
        textView3 = findViewById(R.id.textView6);

        imageButton4 = findViewById(R.id.imageButton4);
        imageButton5 = findViewById(R.id.imageButton9);
        imageButton6 = findViewById(R.id.imageButton6);

        button = findViewById(R.id.button2);
        deleteButton = findViewById(R.id.dltbtn);
        saveButton = findViewById(R.id.saveButton);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyContacts", MODE_PRIVATE);

        // Request permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    1);
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    2);
        }

        // Set onClickListener for the button to pick a contact
        button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, PICK_CONTACT);
        });

        // Set onClickListeners for the ImageButtons to initiate calls
        imageButton4.setOnClickListener(v -> makeCall(0));
        imageButton5.setOnClickListener(v -> makeCall(1));
        imageButton6.setOnClickListener(v -> makeCall(2));
        loadSavedContacts();
        saveButton.setOnClickListener(v -> {
            saveContactsToSharedPreferences();
        });

// Set onClickListener for the deleteButton
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearContact(0);
                    }
                });
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearContact(1);
                    }
                });
                textView3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearContact(2);
                    }
                });
            }
        });



        IntentFilter screenOnFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenOnReceiver, screenOnFilter);
    }

    // BroadcastReceiver to handle power button events
    private BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                // The screen is turned on
                powerButtonClickCount++;

                if (powerButtonClickCount == POWER_BUTTON_CLICK_THRESHOLD) {
                    // Make a call when the power button is clicked three times
                    makeCall(0); // Change the index according to your requirement

                    powerButtonClickCount = 0; // Reset the count after making the call
                }
            }
        }
    };


    // onActivityResult method
    @SuppressLint("Range")
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
                    if (c != null && c.moveToFirst()) {
                        int hasPhoneColumnIndex = c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                        if (hasPhoneColumnIndex >= 0) {
                            String hasPhone = c.getString(hasPhoneColumnIndex);
                            if (hasPhone != null && hasPhone.equalsIgnoreCase("1")) {
                                String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                                Cursor phones = getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                        null, null);
                                if (phones != null && phones.moveToFirst()) {
                                    String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                    int index = findNextEmptyIndex();
                                    if (index != -1) {
                                        cnumber[index] = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim();
                                        updateTextView(index, cnumber[index], name);
                                    } else {
                                        Toast.makeText(this, "All slots are filled! Please delete a contact.", Toast.LENGTH_SHORT).show();
                                    }
                                    phones.close();
                                }
                            }
                        }
                        if (c != null) {
                            c.close();
                        }
                    }
                }
                break;
        }

    }

    // findNextEmptyIndex method
    private int findNextEmptyIndex() {
        for (int i = 0; i < cnumber.length; i++) {
            if (cnumber[i] == null || cnumber[i].isEmpty()) {
                return i;
            }
        }
        return -1;  // Return -1 if all slots are filled
    }

    // updateTextView method
    private void updateTextView(int index, String phoneNumber, String name) {
        switch (index) {
            case 0:
                textView.setText(name + "\n" + phoneNumber);
                break;
            case 1:
                textView2.setText(name + "\n" + phoneNumber);
                break;
            case 2:
                textView3.setText(name + "\n" + phoneNumber);
                break;
        }
        saveContactToSharedPreferences(index, phoneNumber, name);
    }

    // makeCall method
    private void makeCall(int index) {
        String savedPhoneNumber = sharedPreferences.getString("phoneNumber" + index, "");

        if (savedPhoneNumber != null && !savedPhoneNumber.trim().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + savedPhoneNumber.trim()));

            // Check if the call was already answered for this index
            boolean callAnswered = sharedPreferences.getBoolean("callAnswered" + index, false);

            if (!callAnswered) {
                // Start the call
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                startActivity(intent);

                // Delay before attempting the next call
                handler.postDelayed(() -> {
                    int nextIndex = index + 2;
                    if (nextIndex < cnumber.length) {
                        makeCall(nextIndex);
                    }
                }, DELAY_BEFORE_NEXT_CALL);
            }
        } else {
            Toast.makeText(this, "No valid number available.", Toast.LENGTH_SHORT).show();
        }
    }

    // clearContact method
    private void clearContact(int index) {
        switch (index) {
            case 0:
                textView.setText("Number1");
                break;
            case 1:
                textView2.setText("Number2");
                break;
            case 2:
                textView3.setText("Number3");
                break;
        }
        cnumber[index] = "";  // Clear the corresponding phone number
    }

    // saveContactsToSharedPreferences method
    public void saveContactsToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < cnumber.length; i++) {
            String phoneNumber = cnumber[i];
            editor.putString("phoneNumber" + i, phoneNumber);
        }

        editor.apply();  // Apply changes
        Toast.makeText(SOS.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
    }

    // saveContactToSharedPreferences method
    public void saveContactToSharedPreferences(int index, String phoneNumber, String name) {
        if (sharedPreferences.getString("name" + index, "").isEmpty()
                && sharedPreferences.getString("phoneNumber" + index, "").isEmpty()) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name" + index, name);
            editor.putString("phoneNumber" + index, phoneNumber);
            editor.apply();  // Commit changes
        }
    }

    // loadSavedContacts method
    private void loadSavedContacts() {
        for (int i = 0; i < 3; i++) {
            String name = sharedPreferences.getString("name" + i, "");
            String phoneNumber = sharedPreferences.getString("phoneNumber" + i, "");
            if (!name.isEmpty() && !phoneNumber.isEmpty()) {
                updateTextView(i, phoneNumber, name);
            }
        }
    }

    // onDestroy method
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(screenOnReceiver);
        IntentFilter screenOnFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenOnReceiver, screenOnFilter);

    }

}