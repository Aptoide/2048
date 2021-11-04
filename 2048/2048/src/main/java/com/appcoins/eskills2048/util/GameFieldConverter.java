package com.appcoins.eskills2048.util;

import com.appcoins.eskills2048.Tile;
import com.google.gson.Gson;

public class GameFieldConverter {
    private final Gson gson;

    public GameFieldConverter(Gson gson) {
        this.gson = gson;
    }

    public String fromField(Tile[][] field) {
        return gson.toJson(field);
    }

    public Tile[][] toField(String field) {
        return gson.fromJson(field, Tile[][].class);
    }
}
