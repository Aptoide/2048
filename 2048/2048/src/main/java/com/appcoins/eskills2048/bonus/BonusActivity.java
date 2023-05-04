package com.appcoins.eskills2048.bonus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.R;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;

@AndroidEntryPoint public class BonusActivity extends AppCompatActivity {

  private static final String SKU_KEY = "SKU_KEY";
  private final CompositeDisposable disposables = new CompositeDisposable();

  static public Intent create(Context context, String sku) {
    Intent intent = new Intent(context, BonusActivity.class);
    intent.putExtra(SKU_KEY, sku);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rankings);
    String sku = getIntent().getExtras()
        .getString(SKU_KEY);
    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_container, BonusFragment.newInstance(sku))
          .commit();
    }
  }

  @Override protected void onDestroy() {
    disposables.clear();
    super.onDestroy();
  }
}