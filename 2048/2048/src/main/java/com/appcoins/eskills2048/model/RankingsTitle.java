package com.appcoins.eskills2048.model;

import com.appcoins.eskills2048.R;

public class RankingsTitle implements RankingsItem{

    private final String title;

    public RankingsTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return R.layout.rankings_title;
    }
}
