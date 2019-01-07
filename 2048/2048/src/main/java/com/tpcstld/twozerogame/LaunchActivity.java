package com.tpcstld.twozerogame;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 123;
  private static final int RESULT_OK = 1;

  private static final String USER_ID = "USER_ID";

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent i = buildIntent();
    startActivityForResult(i, REQUEST_CODE);
  }

  private Intent buildIntent() {
    Intent i = new Intent();
    i.putExtra(USER_ID, "string_user_id");
    i.setClassName("com.appcoins.wallet.dev", "cm.aptoide.skills.SkillsActivity");
    return i;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
      startActivity(new Intent(this, MainActivity.class));
      finish();
    }
  }
}
