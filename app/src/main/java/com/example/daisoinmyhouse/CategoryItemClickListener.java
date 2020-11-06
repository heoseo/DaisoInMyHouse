package com.example.daisoinmyhouse;

import android.view.View;

public interface CategoryItemClickListener {
    public void onItemClick(CategoryItemAdapter.ViewHolder holder, View view, int position);
}