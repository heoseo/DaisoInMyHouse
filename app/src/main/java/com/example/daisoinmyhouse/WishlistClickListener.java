package com.example.daisoinmyhouse;

import android.view.View;

public interface WishlistClickListener {
    public void onItemClick(WishlistAdapter.ViewHolder holder, View view, int position);
}