package com.appcoins.eskills2048.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class UserDataStorage {
  private final Context context;
  private final String preferencesName;

  public UserDataStorage(Context context, String preferencesName) {
    this.context = context;
    this.preferencesName = preferencesName;
  }

  public void put(String key, String value) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, value);
    editor.apply();
  }

  public String get(String key) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(preferencesName, MODE_PRIVATE);
    return sharedPreferences.getString(key, "");
  }
}
