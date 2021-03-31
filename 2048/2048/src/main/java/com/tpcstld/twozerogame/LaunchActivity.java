package com.tpcstld.twozerogame;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;

public class LaunchActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 123;
  private static final int RESULT_OK = 1;

  public static final String USER_ID = "USER_ID";
  public static final String ROOM_ID = "ROOM_ID";
  public static final String WALLET_ADDRESS = "WALLET_ADDRESS";
  public static final String JWT = "JWT";

  private final String userId = "string_user_id";

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent i = buildWalletIntent();
    startActivityForResult(i, REQUEST_CODE);
  }

  private Intent buildWalletIntent() {
    Intent i = new Intent();
    i.putExtra(USER_ID, userId);
    i.setClassName("com.appcoins.wallet.dev", "cm.aptoide.skills.SkillsActivity");
    return i;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      startActivity(buildMainActivityIntent(data));
      finish();
    }
  }

  @NotNull private Intent buildMainActivityIntent(Intent data) {
    Intent intent = new Intent(this, MainActivity.class);

    intent.putExtra(ROOM_ID, data.getStringExtra(ROOM_ID));
    intent.putExtra(USER_ID, userId);
    intent.putExtra(WALLET_ADDRESS, data.getStringExtra(WALLET_ADDRESS));
    intent.putExtra(JWT, data.getStringExtra(JWT));

    return intent;
  }
}
