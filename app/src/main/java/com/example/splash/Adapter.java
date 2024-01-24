package com.example.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    ArrayList<Constants> arrayList;
    Context context;
    public Adapter(ArrayList<Constants> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_design,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_title.setText(arrayList.get(position).title);
        holder.tv_des.setText(arrayList.get(position).description);

        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayList.get(position).isVisible)
                {
                    holder.tv_des.setVisibility(View.GONE);
                    holder.rl_des_line.setVisibility(View.GONE);
                    holder.rl_title_line.setVisibility(View.VISIBLE);
                    arrayList.get(position).isVisible=false;
                }
                else
                {
                    holder.tv_des.setVisibility(View.VISIBLE);
                    holder.rl_des_line.setVisibility(View.VISIBLE);
                    holder.rl_title_line.setVisibility(View.GONE);
                    arrayList.get(position).isVisible=true;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_des;
        RelativeLayout rl_title_line;
        RelativeLayout rl_des_line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_des = itemView.findViewById(R.id.tv_des);
            rl_title_line = itemView.findViewById(R.id.rl_title_line);
            rl_des_line = itemView.findViewById(R.id.rl_des_line);
        }
    }
}
