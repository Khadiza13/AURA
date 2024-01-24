package com.example.splash;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class doctor extends AppCompatActivity {
    RecyclerView rcv;
    Myada adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        setTitle("Doctor's List");

        rcv = findViewById(R.id.recview);
        adapter = new Myada(new ArrayList<>(), getApplicationContext());
        rcv.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rcv.setLayoutManager(gridLayoutManager);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Images");

        // Retrieve data from Firebase
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Model> doctorList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Model doctor = snapshot.getValue(Model.class);
                    if (doctor != null) {
                        doctorList.add(doctor);
                    }
                }
                adapter.setData(doctorList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors here
                if (databaseError != null) {
                    String errorMessage = databaseError.getMessage();

                    Toast.makeText(doctor.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("FirebaseError", errorMessage);
                }
            }
        });
    }
}