package com.appcoins.eskills2048.util;

import com.appcoins.eskills2048.Tile;
import com.google.gson.Gson;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton public class GameFieldConverter {
  private final Gson gson;

  @Inject public GameFieldConverter(Gson gson) {
    this.gson = gson;
  }

  public String fromField(Tile[][] field) {
    return gson.toJson(field);
  }

  public Tile[][] toField(String field) {
    return gson.fromJson(field, Tile[][].class);
  }
}
