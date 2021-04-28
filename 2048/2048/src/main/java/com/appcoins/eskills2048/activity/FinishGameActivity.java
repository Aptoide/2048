package com.appcoins.eskills2048.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.appcoins.eskills2048.R;
import com.appcoins.eskills2048.databinding.ActivityFinishGameBinding;
import com.appcoins.eskills2048.factory.RoomApiFactory;
import com.appcoins.eskills2048.repository.RoomRepository;
import com.appcoins.eskills2048.usecase.GetRoomUseCase;
import com.appcoins.eskills2048.vm.FinishGameActivityViewModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FinishGameActivity extends AppCompatActivity {

  public static final String SESSION = "SESSION";
  public static final String WALLET_ADDRESS = "WALLET_ADDRESS";

  private ActivityFinishGameBinding binding;
  private FinishGameActivityViewModel viewModel;

  public static final Intent buildIntent(Context context, String session, String walletAddress) {
    Intent intent = new Intent(context, FinishGameActivity.class);
    intent.putExtra(SESSION, session);
    intent.putExtra(WALLET_ADDRESS, walletAddress);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityFinishGameBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    String session = getIntent().getStringExtra(SESSION);
    viewModel = new FinishGameActivityViewModel(
        new GetRoomUseCase(new RoomRepository(RoomApiFactory.buildRoomApi())), session);

    viewModel.isWinner()
        .observeOn(AndroidSchedulers.mainThread())
        // TODO: 21-04-2021 remove this two lines
        .doOnError(throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG))
        .onErrorResumeNext(Single.just(true))
        .doOnSuccess(this::setWinner)
        .subscribe();
  }

  private void setWinner(Boolean winner) {
    binding.progressBar.setVisibility(View.GONE);
    if (winner) {
      binding.progressBarTv.setText(getResources().getString(R.string.you_won));
    } else {
      binding.progressBarTv.setText(getResources().getString(R.string.you_lost));
    }
  }
}
