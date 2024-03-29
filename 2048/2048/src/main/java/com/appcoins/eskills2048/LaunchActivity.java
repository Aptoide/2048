package com.appcoins.eskills2048;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.databinding.ActivityLaunchBinding;
import com.appcoins.eskills2048.model.LocalGameStatus;
import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.usecase.GetGameStatusLocallyUseCase;
import com.appcoins.eskills2048.util.DeviceScreenManager;
import com.appcoins.eskills2048.util.KeyboardUtils;
import com.appcoins.eskills2048.util.UserDataStorage;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;

@AndroidEntryPoint public class LaunchActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 123;
  private static final int RESULT_OK = 0;
  private static final int RESULT_USER_CANCELED = 1;
  private static final int RESULT_ERROR = 6;
  private static final String PREFERENCES_USER_NAME = "PREFERENCES_USER_NAME";

  public static final String USER_ID = "USER_ID";
  public static final String WALLET_ADDRESS = "WALLET_ADDRESS";
  public static final String MATCH_ENVIRONMENT = "MATCH_ENVIRONMENT";
  public static final String SESSION = "SESSION";
  public static final String LOCAL_GAME_STATUS = "LOCAL_GAME_STATUS";

  private static final String ENTRY_PRICE_DUEL = "1 USD";
  private static final String ENTRY_PRICE_MULTIPLAYER = "4 USD";
  private static final String ENTRY_SANDBOX = "0 USD";

  private static final String PLAY_APP_VIEW_URL = "market://details?id=%s";

  private final String userId = "string_user_id";
  private MatchDetails.Environment matchEnvironment;

  private ActivityLaunchBinding binding;

  @Inject UserDataStorage userDataStorage;
  @Inject GetGameStatusLocallyUseCase getGameStatusLocallyUseCase;
  @Inject Gson gson;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityLaunchBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    checkFirstRun();
    LocalGameStatus localGameStatus = getGameStatusLocallyUseCase.getGameStatus();
    if (localGameStatus != null) {
      Toast.makeText(this, "Restoring your game...", Toast.LENGTH_LONG)
          .show();
      resumeGame(localGameStatus);
    }

    binding.loadingLayout.getRoot()
        .setVisibility(View.GONE);
    binding.startNewGameLayout.getRoot()
        .setVisibility(View.VISIBLE);

    binding.startNewGameLayout.newGameButton.setOnClickListener(view -> {
      binding.createTicketLayout.gameTypeLayout.radioButtonDuel.setText(R.string.game_type_duel);
      binding.createTicketLayout.gameTypeLayout.radioButtonMultiplayer.setText(
          R.string.game_type_multiplayer);
      showCreateTicket();
    });
    binding.startNewGameLayout.sandboxGameButton.setOnClickListener(view -> launchSandboxGame());
    FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    Bundle bundle = new Bundle();
    bundle.putLong("current_time", System.currentTimeMillis());
    firebaseAnalytics.logEvent("app_started", bundle);
  }

  private void checkFirstRun() {
    boolean isFirstRun = getSharedPreferences("PREFERENCE", 0).getBoolean("isFirstRun", true);
    if (isFirstRun) {
      new ApkOriginVerification(this);
      getSharedPreferences("PREFERENCE", 0).edit()
          .putBoolean("isFirstRun", false)
          .apply();
    }
  }

  private void resumeGame(LocalGameStatus localGameStatus) {
    Intent intent =
        MainActivity.newIntent(this, userId, localGameStatus.getWalletAddress(), MatchDetails.Environment.LIVE,
            localGameStatus.getSession(), localGameStatus);
    startActivity(intent);
    finish();
  }

  @Override public void onBackPressed() {
    binding.startNewGameLayout.getRoot()
        .setVisibility(View.VISIBLE);
    binding.createTicketLayout.getRoot()
        .setVisibility(View.GONE);
    binding.canceledTicketLayout.getRoot()
        .setVisibility(View.GONE);
    binding.installWalletLayout.getRoot()
        .setVisibility(View.GONE);
  }

  private void showCreateTicket() {
    binding.startNewGameLayout.getRoot()
        .setVisibility(View.GONE);
    binding.createTicketLayout.getRoot()
        .setVisibility(View.VISIBLE);
    binding.createTicketLayout.userName.setText(userDataStorage.getString(PREFERENCES_USER_NAME));
    setGamePrice();

    binding.createTicketLayout.findRoomButton.setOnClickListener(view -> {
      String userName = binding.createTicketLayout.userName.getText()
          .toString();
      userDataStorage.putString(PREFERENCES_USER_NAME, userName);
      KeyboardUtils.hideKeyboard(view);
      DeviceScreenManager.keepAwake(getWindow());

      launchEskillsFlow(userName, Objects.requireNonNull(getMatchDetails()));
    });
  }

  private void setGamePrice() {
    if (binding.createTicketLayout.gameTypeLayout.radioGroup.getCheckedRadioButtonId()
        == binding.createTicketLayout.gameTypeLayout.radioButtonDuel.getId()) {
      binding.createTicketLayout.createTicketHeader.fiatPrice.setText(ENTRY_PRICE_DUEL);
    } else {
      binding.createTicketLayout.createTicketHeader.fiatPrice.setText(ENTRY_PRICE_MULTIPLAYER);
    }
    binding.createTicketLayout.gameTypeLayout.radioGroup.setOnCheckedChangeListener(
        (group, checkedId) -> {
          if (checkedId == binding.createTicketLayout.gameTypeLayout.radioButtonDuel.getId()) {
            binding.createTicketLayout.createTicketHeader.fiatPrice.setText(ENTRY_PRICE_DUEL);
          } else if (checkedId
              == binding.createTicketLayout.gameTypeLayout.radioButtonMultiplayer.getId()) {
            binding.createTicketLayout.createTicketHeader.fiatPrice.setText(
                ENTRY_PRICE_MULTIPLAYER);
          }
        });
  }

  private MatchDetails getMatchDetails() {
    int checkedId = binding.createTicketLayout.gameTypeLayout.radioGroup.getCheckedRadioButtonId();
    if (checkedId == binding.createTicketLayout.gameTypeLayout.radioButtonDuel.getId()) {
      return new MatchDetails("1v1", 1f, "USD", 2, 3600);
    } else if (checkedId
        == binding.createTicketLayout.gameTypeLayout.radioButtonMultiplayer.getId()) {
      return new MatchDetails("multiplayer", 4f, "USD", 3, 3600);
    }
    return null;
  }

  private void launchSandboxGame() {
    Intent intent =
        MainActivity.newIntent(this, userId, "", MatchDetails.Environment.SANDBOX, "", null);
    startActivity(intent);
    finish();
  }

  private void launchEskillsFlow(String userName, MatchDetails matchDetails) {
    String url = BuildConfig.BASE_HOST_PAYMENT
        + "/transaction/eskills?"
        + "value="
        + matchDetails.getValue()
        + "&currency="
        + matchDetails.getCurrency()
        + "&product="
        + matchDetails.getProduct()
        + "&user_id="
        + userId
        + "&user_name="
        + userName
        + "&domain="
        + getPackageName()
        + "&environment="
        + MatchDetails.Environment.LIVE.name()
        + "&metadata="
        + buildMetaData()
        + "&number_of_users="
        + matchDetails.getNumberOfUsers()
        + "&timeout="
        + matchDetails.getTimeout();

    Intent intent = buildTargetIntent(url);
    try {
      startActivityForResult(intent, REQUEST_CODE);
    } catch (Exception e) {
      e.printStackTrace();
      showInstallWalletDialog();
      binding.installWalletLayout.installButton.setOnClickListener(view -> {
        String market = String.format(PLAY_APP_VIEW_URL, BuildConfig.WALLET_PACKAGE_NAME);
        intent.setData(Uri.parse(market));
        intent.setPackage(null);
        startActivity(intent);
        showCreateTicketLayout();
      });
    }
  }

  private String buildMetaData() {
    Map<String, String> metadata = new HashMap<>();
    metadata.put("metaKey", "metaValue");
    return gson.toJson(metadata);
  }

  /**
   * This method generates the intent with the provided One Step URL to target the
   * AppCoins Wallet.
   *
   * @param url The url that generated by following the One Step payment rules
   *
   * @return The intent used to call the wallet
   */
  private Intent buildTargetIntent(String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));
    intent.setPackage(BuildConfig.WALLET_PACKAGE_NAME);
    return intent;
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE) {
      switch (resultCode) {
        case RESULT_OK:
          if (data != null) {
            Intent intent =
                MainActivity.newIntent(this, userId, data.getStringExtra(WALLET_ADDRESS),
                    MatchDetails.Environment.LIVE, data.getStringExtra(SESSION), null);
            startActivity(intent);
            finish();
          } else {
            showCancelDialog();
          }
          break;
        case RESULT_USER_CANCELED:
        case RESULT_ERROR:
          showCancelDialog();
          break;
      }
    }
  }

  private void showCancelDialog() {
    binding.createTicketLayout.getRoot()
        .setVisibility(View.GONE);
    binding.canceledTicketLayout.getRoot()
        .setVisibility(View.VISIBLE);
    binding.canceledTicketLayout.closeButton.setOnClickListener(view -> {
      binding.canceledTicketLayout.getRoot()
          .setVisibility(View.GONE);
      binding.createTicketLayout.getRoot()
          .setVisibility(View.VISIBLE);
    });
  }

  private void showInstallWalletDialog() {
    binding.createTicketLayout.getRoot()
        .setVisibility(View.GONE);
    binding.canceledTicketLayout.getRoot()
        .setVisibility(View.GONE);
    binding.installWalletLayout.getRoot()
        .setVisibility(View.VISIBLE);
  }

  private void showCreateTicketLayout(){
    binding.installWalletLayout.getRoot().setVisibility(View.GONE);
    binding.canceledTicketLayout.getRoot()
        .setVisibility(View.GONE);
    binding.createTicketLayout.getRoot()
        .setVisibility(View.VISIBLE);
  }
}
