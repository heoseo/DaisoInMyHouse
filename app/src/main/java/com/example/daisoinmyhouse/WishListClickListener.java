package com.example.daisoinmyhouse;

import android.view.View;

public interface WishListClickListener {
    public void onItemClick(WishListAdapter.ViewHolder holder, View view, int position);
}