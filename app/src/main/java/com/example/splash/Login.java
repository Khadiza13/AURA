package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {
    public static String PREFS_NAME="MyPrefsFile";
    private Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register=findViewById(R.id.button);
        register.setBackgroundColor(getResources().getColor(R.color.white));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences= getSharedPreferences(Login.PREFS_NAME,0);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn",true);
                editor.commit();
                startActivity(new Intent(Login.this,Home2.class));
                finish();
            }
        });
    }
}