package com.appcoins.eskills2048.rankins;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.model.MatchDetails;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;

@AndroidEntryPoint public class RankingsActivity extends AppCompatActivity {

  private static final String WALLET_ADDRESS_KEY = "WALLET_ADDRESS_KEY";
  private static final String MATCH_ENVIRONMENT = "MATCH_ENVIRONMENT";
  private static final String SKU_KEY = "SKU_KEY";
  private final CompositeDisposable disposables = new CompositeDisposable();

  static public Intent create(Context context, String walletAddress, String sku,
      MatchDetails.Environment matchEnvironment) {
    Intent intent = new Intent(context, RankingsActivity.class);
    intent.putExtra(WALLET_ADDRESS_KEY, walletAddress);
    intent.putExtra(MATCH_ENVIRONMENT, matchEnvironment);
    intent.putExtra(SKU_KEY, sku);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rankings);
    String userWalletAddress = getIntent().getExtras()
        .getString(WALLET_ADDRESS_KEY);
    String sku = getIntent().getExtras()
        .getString(SKU_KEY);
    MatchDetails.Environment matchEnvironment =
        (MatchDetails.Environment) getIntent().getSerializableExtra(MATCH_ENVIRONMENT);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_container,
              RankingsFragment.newInstance(userWalletAddress, sku,  matchEnvironment))
          .commit();
    }
  }

  @Override protected void onDestroy() {
    disposables.clear();
    super.onDestroy();
  }
}