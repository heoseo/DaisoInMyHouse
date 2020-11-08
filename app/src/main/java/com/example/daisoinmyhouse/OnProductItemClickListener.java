package com.example.daisoinmyhouse;

import android.view.View;

public interface OnProductItemClickListener {
    public void onItemClick(ItemAdapter.ViewHolder holder, View view, int position);
    public void onItemWantClick(ItemWantAdapter.ViewHolder holder, View view, int position);
}
