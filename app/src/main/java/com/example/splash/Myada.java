package com.example.splash;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.time.LocalTime;
import java.util.ArrayList;

public class Myada extends RecyclerView.Adapter<myviewholder> {
    private ArrayList<Model> data;
    private Context context;

    public Myada(ArrayList<Model> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {
        final Model temp = data.get(position);

        holder.t1.setText(data.get(position).getHeader());
        holder.t2.setText(data.get(position).getDesc());
        Glide.with(context).load(data.get(position).getImgname()).into(holder.img);

        holder.t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, doctor2.class);
                intent.putExtra("imagename", temp.getImgname());
                intent.putExtra("header", temp.getHeader());
                intent.putExtra("desc", temp.getDesc());
                intent.putExtra("phoneNumber", temp.getPhoneNumber());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // Add this method to set the data in your adapter
    public void setData(ArrayList<Model> newDataList) {
        data.clear();
        data.addAll(newDataList);
        notifyDataSetChanged();
    }
}