package com.appcoins.eskills2048;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.databinding.ActivityLaunchBinding;
import com.appcoins.eskills2048.model.MatchDetails;
import com.appcoins.eskills2048.util.DeviceScreenManager;
import com.appcoins.eskills2048.util.KeyboardUtils;
import com.appcoins.eskills2048.util.UserDataStorage;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class LaunchActivity extends AppCompatActivity {

  private static final int REQUEST_CODE = 123;
  private static final int RESULT_OK = 0;
  private static final int RESULT_USER_CANCELED = 1;
  private static final int RESULT_ERROR = 6;
  private static final String SHARED_PREFERENCES_NAME = "SKILL_SHARED_PREFERENCES";
  private static final String PREFERENCES_USER_NAME = "PREFERENCES_USER_NAME";

  public static final String USER_ID = "USER_ID";
  public static final String ROOM_ID = "ROOM_ID";
  public static final String WALLET_ADDRESS = "WALLET_ADDRESS";
  public static final String SESSION = "SESSION";

  private static final String ENTRY_PRICE_DUEL = "1 USD";
  private static final String ENTRY_PRICE_MULTIPLAYER = "3 USD";

  private final String userId = "string_user_id";

  private ActivityLaunchBinding binding;
  private UserDataStorage userDataStorage;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityLaunchBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    //<-----[DEBUG]
    Button goToPlayerStats;
    goToPlayerStats = findViewById(R.id.goToPlayerStats);
    goToPlayerStats.setOnClickListener(view -> {
      startActivity(RankingsActivity.create(this, "0x05512a1c8457380898181ef0f02e4c752200c6c5"));
    });
    //----->[DEBUG]

    userDataStorage = new UserDataStorage(this, SHARED_PREFERENCES_NAME);
    binding.startNewGameLayout.newGameButton.setOnClickListener(
        view -> showCreateTicket(MatchDetails.Environment.LIVE));
    binding.startNewGameLayout.sandboxGameButton.setOnClickListener(
        view -> showCreateTicket(MatchDetails.Environment.SANDBOX));
  }

  @Override
  public void onBackPressed() {
    binding.startNewGameLayout.startNewGameCard.setVisibility(View.VISIBLE);
    binding.createTicketLayout.createTicketCard.setVisibility(View.GONE);
  }

  private void showCreateTicket(MatchDetails.Environment environment) {
    binding.startNewGameLayout.startNewGameCard.setVisibility(View.GONE);
    binding.createTicketLayout.createTicketCard.setVisibility(View.VISIBLE);
    binding.createTicketLayout.userName.setText(userDataStorage.get(PREFERENCES_USER_NAME));
    setGamePrice();

    binding.createTicketLayout.findRoomButton.setOnClickListener(view -> {
      String userName = binding.createTicketLayout.userName.getText().toString();
      userDataStorage.put(PREFERENCES_USER_NAME, userName);
      KeyboardUtils.hideKeyboard(view);
      DeviceScreenManager.keepAwake(getWindow());

      launchEskillsFlow(userName, getMatchDetails(environment));
    });
  }

  private void setGamePrice() {
    binding.createTicketLayout.gameTypeLayout.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
      if (checkedId == binding.createTicketLayout.gameTypeLayout.radioButtonDuel.getId()) {
        binding.createTicketLayout.createTicketHeader.fiatPrice.setText(ENTRY_PRICE_DUEL);
      } else if (checkedId == binding.createTicketLayout.gameTypeLayout.radioButtonMultiplayer.getId()) {
        binding.createTicketLayout.createTicketHeader.fiatPrice.setText(ENTRY_PRICE_MULTIPLAYER);
      }
    });
  }

  private MatchDetails getMatchDetails(MatchDetails.Environment environment) {
    int checkedId = binding.createTicketLayout.gameTypeLayout.radioGroup.getCheckedRadioButtonId();
    if (checkedId == binding.createTicketLayout.gameTypeLayout.radioButtonDuel.getId()) {
      return new MatchDetails("1v1", 1f, "USD", environment, 2, 3600);
    } else if (checkedId == binding.createTicketLayout.gameTypeLayout.radioButtonMultiplayer.getId()) {
      return new MatchDetails("multiplayer", 3f, "USD", environment, 3, 3600);
    }
    return null;
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
        + matchDetails.getEnvironment()
        .name()
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
    }
  }

  private String buildMetaData() {
    Gson gson = new Gson();
    Map<String, String> metadata = new HashMap<>();
    metadata.put("metaKey", "metaValue");
    return gson.toJson(metadata);
  }

  /**
   * This method generates the intent with the provided One Step URL to target the
   * AppCoins Wallet.
   *
   * @param url The url that generated by following the One Step payment rules
   * @return The intent used to call the wallet
   */
  private Intent buildTargetIntent(String url) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(url));

    // Check if there is an application that can process the AppCoins Billing
    // flow
    PackageManager packageManager = getApplicationContext().getPackageManager();
    List<ResolveInfo> appsList = packageManager
        .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    for (ResolveInfo app : appsList) {
      if (app.activityInfo.packageName.equals("cm.aptoide.pt")) {
        // If there's aptoide installed always choose Aptoide as default to open
        // url
        intent.setPackage(app.activityInfo.packageName);
        break;
      } else if (app.activityInfo.packageName.equals(BuildConfig.WALLET_PACKAGE_NAME)) {
        // If Aptoide is not installed and wallet is installed then choose Wallet
        // as default to open url
        intent.setPackage(app.activityInfo.packageName);
      }
    }
    return intent;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE) {
      switch (resultCode) {
        case RESULT_OK:
          startActivity(buildMainActivityIntent(data));
          finish();
          break;
        case RESULT_USER_CANCELED:
        case RESULT_ERROR:
          showCancelDialog();
          break;
      }
    }
  }

  @NotNull
  private Intent buildMainActivityIntent(Intent data) {
    Intent intent = new Intent(this, MainActivity.class);

    intent.putExtra(ROOM_ID, data.getStringExtra(ROOM_ID));
    intent.putExtra(USER_ID, userId);
    intent.putExtra(WALLET_ADDRESS, data.getStringExtra(WALLET_ADDRESS));
    intent.putExtra(SESSION, data.getStringExtra(SESSION));

    return intent;
  }

  private void showCancelDialog() {
    binding.createTicketLayout.createTicketCard.setVisibility(View.GONE);
    binding.canceledTicketLayout.canceledCard.setVisibility(View.VISIBLE);
    binding.canceledTicketLayout.closeButton.setOnClickListener(view -> {
      binding.canceledTicketLayout.canceledCard.setVisibility(View.GONE);
      binding.createTicketLayout.createTicketCard.setVisibility(View.VISIBLE);
    });
  }
}
