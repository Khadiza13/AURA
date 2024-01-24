package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class tips extends AppCompatActivity {

    RecyclerView recyclerView;
    List<DataClass> dataList;
    MyAdapter adapter;
    DataClass androidData;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(tips.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        dataList = new ArrayList<>();
        androidData = new DataClass("Home", R.string.home, "Java", R.drawable.home);
        dataList.add(androidData);
        androidData = new DataClass("Workplace", R.string.workplace, "Kotlin", R.drawable.workplace);
        dataList.add(androidData);
        androidData = new DataClass("Public Transport", R.string.pub, "Java", R.drawable.transport);
        dataList.add(androidData);
        androidData = new DataClass("Travelling", R.string.travelling, "Kotlin", R.drawable.travel);
        dataList.add(androidData);
        androidData = new DataClass("Social Gathering", R.string.social, "Java", R.drawable.social);
        dataList.add(androidData);
        androidData = new DataClass("Educational Institution", R.string.education, "Java", R.drawable.education);
        dataList.add(androidData);
        androidData = new DataClass("Online Platform", R.string.online, "Java", R.drawable.online);
        dataList.add(androidData);
        androidData = new DataClass("Hotline Services", R.string.emergency, "Java", R.drawable.emrgncy);
        dataList.add(androidData);
        adapter = new MyAdapter(tips.this, dataList);
        recyclerView.setAdapter(adapter);
    }
    private void searchList(String text){
        List<DataClass> dataSearchList = new ArrayList<>();
        for (DataClass data : dataList){
            if (data.getDataTitle().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data);
            }
        }
        if (dataSearchList.isEmpty()){
            Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setSearchList(dataSearchList);
        }
    }
}
