package com.appcoins.eskills2048.util;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static android.content.Context.MODE_PRIVATE;

@Singleton public class UserDataStorage {
  private final Context context;
  private final String preferencesName;

  @Inject public UserDataStorage(@ApplicationContext Context context,
      @Named("preferencesName") String preferencesName) {
    this.context = context;
    this.preferencesName = preferencesName;
  }

  public void remove(String key) {
    SharedPreferences.Editor editor = getEditor();
    editor.remove(key);
    editor.apply();
  }

  public void putString(String key, String value) {
    SharedPreferences.Editor editor = getEditor();
    editor.putString(key, value);
    editor.apply();
  }

  public void putLong(String key, Long value) {
    SharedPreferences.Editor editor = getEditor();
    editor.putLong(key, value);
    editor.apply();
  }

  public void putBoolean(String key, Boolean value) {
    SharedPreferences.Editor editor = getEditor();
    editor.putBoolean(key, value);
    editor.apply();
  }

  public String getString(String key) {
    SharedPreferences sharedPreferences = getSharedPreferences();
    return sharedPreferences.getString(key, "");
  }

  public Long getLong(String key) {
    SharedPreferences sharedPreferences = getSharedPreferences();
    return sharedPreferences.getLong(key, -1);
  }

  public Boolean getBoolean(String key) {
    SharedPreferences sharedPreferences = getSharedPreferences();
    return sharedPreferences.getBoolean(key, true);
  }

  private SharedPreferences.Editor getEditor() {
    SharedPreferences sharedPreferences = getSharedPreferences();
    return sharedPreferences.edit();
  }

  private SharedPreferences getSharedPreferences() {
    return context.getSharedPreferences(preferencesName, MODE_PRIVATE);
  }
}
