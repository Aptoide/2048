package com.appcoins.eskills2048.util;

import android.view.Window;
import android.view.WindowManager;

public class DeviceScreenManager {
  public static void keepAwake(Window window) {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  public static void stopKeepAwake(Window window) {
    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }
}
