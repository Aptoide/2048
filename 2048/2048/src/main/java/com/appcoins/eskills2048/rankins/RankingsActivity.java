package com.appcoins.eskills2048.rankins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.R;
import io.reactivex.disposables.CompositeDisposable;

public class RankingsActivity extends AppCompatActivity {

  private static final String WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY";
  private final CompositeDisposable disposables = new CompositeDisposable();

  static public Intent create(Context context, String walletAddress) {
    Intent intent = new Intent(context, RankingsActivity.class);
    intent.putExtra(WALLET_ADDRESS_KEY, walletAddress);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rankings);
    String userWalletAddress = getIntent().getExtras()
        .getString(WALLET_ADDRESS_KEY);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_container, RankingsFragment.newInstance(userWalletAddress))
          .commit();
    }
  }

  @Override protected void onDestroy() {
    disposables.clear();
    super.onDestroy();
  }

}