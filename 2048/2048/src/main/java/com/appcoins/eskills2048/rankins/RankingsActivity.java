package com.appcoins.eskills2048.rankins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.R;

import java.io.Serializable;

import io.reactivex.disposables.CompositeDisposable;

public class RankingsActivity extends AppCompatActivity {

  private static final String WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY";
  private static final String MATCH_ENVIRONMENT = "MATCH_ENVIRONMENT";
  private final CompositeDisposable disposables = new CompositeDisposable();

  static public Intent create(Context context, String walletAddress) {
    Intent intent = new Intent(context, RankingsActivity.class);
    Serializable matchEnvironment = ((Activity) context).getIntent().getSerializableExtra("MATCH_ENVIRONMENT");
    intent.putExtra(WALLET_ADDRESS_KEY, walletAddress);
    intent.putExtra(MATCH_ENVIRONMENT, matchEnvironment);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rankings);
    String userWalletAddress = getIntent().getExtras()
        .getString(WALLET_ADDRESS_KEY);
    Serializable matchEnvironment = getIntent().getSerializableExtra(MATCH_ENVIRONMENT);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_container, RankingsFragment.newInstance(userWalletAddress, matchEnvironment))
          .commit();
    }
  }

  @Override protected void onDestroy() {
    disposables.clear();
    super.onDestroy();
  }

}