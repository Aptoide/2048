package com.appcoins.eskills2048.model;

public class RankingsTitle implements RankingsItem{

    private String title;

    public RankingsTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
