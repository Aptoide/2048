package com.appcoins.eskills2048;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class BaseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
  }
}