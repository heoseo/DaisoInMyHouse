package com.example.daisoinmyhouse;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface SearchItemClickListener {
    public void onItemClick(RecyclerViewAdapter.MyViewHolder holder, View view, int position);
}