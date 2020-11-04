package com.example.daisoinmyhouse;

import android.view.View;

public interface SearchItemClickListener {
    public void onItemClick(SearchAdapter.ViewHolder holder, View view, int position);
}