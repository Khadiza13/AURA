package com.example.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    public static String PREFS_NAME="MyPrefsFile";
    public static  final String TAG ="TAG";
    private Button register;
    EditText fn,email,phn,pass;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userId;
    ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fn = findViewById(R.id.fullname);
        email = findViewById(R.id.emailtext);
        phn = findViewById(R.id.editTextPhone);
        pass = findViewById(R.id.pass);
        progressBar = findViewById(R.id.progressbar);
        register=findViewById(R.id.button);
        register.setBackgroundColor(getResources().getColor(R.color.white));
        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        if(fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(Login.this,Home2.class));
            finish();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = fn.getText().toString();
                final String textmail = email.getText().toString();
                final String phone = phn.getText().toString();
                String password = pass.getText().toString();

                if(TextUtils.isEmpty(name))
                {
                    fn.setError("Full Name is requied.");
                    return;
                }
                if(TextUtils.isEmpty(textmail))
                {
                    email.setError("Email is requied.");
                    return;
                }
                if(TextUtils.isEmpty(phone))
                {
                    phn.setError("Phone Number is requied.");
                    return;
                }
                if(phone.length()<11 || phone.length()>11)
                {
                    phn.setError("Invalid phone number.");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    pass.setError("Please fill the password field.");
                    return;
                }
                if(password.length()<8)
                {
                    pass.setError("Password length must contain at least 8 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(textmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(Login.this,"Registration Successful",Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Onfailure: Email not sent " + e.getMessage());

                                }
                            });
                            Toast.makeText(Login.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                            userId = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("user").document(userId);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fName",name);
                            user.put("email",textmail);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"Onsucces: User registration successful " + userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Onfailure: " + e.toString());
                                }
                            });

                            SharedPreferences sharedPreferences= getSharedPreferences(Login.PREFS_NAME,0);
                            SharedPreferences.Editor editor= sharedPreferences.edit();
                            editor.putBoolean("hasLoggedIn",true);
                            editor.commit();
                            startActivity(new Intent(Login.this,Home2.class));
                            finish();
                        }
                        else {
                            Toast.makeText(Login.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}