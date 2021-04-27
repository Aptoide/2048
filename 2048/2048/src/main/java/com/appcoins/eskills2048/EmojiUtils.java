package com.appcoins.eskills2048;

public class EmojiUtils {
  public static String getEmojiByUnicode(int unicode){
    return new String(Character.toChars(unicode));
  }
}
