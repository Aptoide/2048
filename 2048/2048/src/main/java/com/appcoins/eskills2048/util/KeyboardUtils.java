package com.appcoins.eskills2048.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {
  public static void hideKeyboard(View view) {
    InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().
        getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }
}
