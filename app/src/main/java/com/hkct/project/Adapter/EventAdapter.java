package com.hkct.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hkct.project.Model.Model;
import com.hkct.project.R;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{
        private ArrayList<Model> mList;
        private Context context;

        public EventAdapter(Context context , ArrayList<Model> mList){

            this.context = context;
            this.mList = mList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.each_event , parent ,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.m_image);
            }
        }
    }

