package com.tpcstld.twozerogame.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.tpcstld.twozerogame.R;
import com.tpcstld.twozerogame.databinding.ActivityFinishGameBinding;
import com.tpcstld.twozerogame.factory.RoomApiFactory;
import com.tpcstld.twozerogame.repository.RoomRepository;
import com.tpcstld.twozerogame.usecase.GetRoomUseCase;
import com.tpcstld.twozerogame.vm.FinishGameActivityViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class FinishGameActivity extends AppCompatActivity {

  public static final String ROOM_ID = "ROOM_ID";
  public static final String WALLET_ADDRESS = "WALLET_ADDRESS";

  private ActivityFinishGameBinding binding;
  private FinishGameActivityViewModel viewModel;

  public static final Intent buildIntent(Context context, String roomId, String walletAddress) {
    Intent intent = new Intent(context, FinishGameActivity.class);
    intent.putExtra(ROOM_ID, roomId);
    intent.putExtra(WALLET_ADDRESS, walletAddress);
    return intent;
  }

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityFinishGameBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    String roomId = getIntent().getStringExtra(ROOM_ID);
    String walletAddress = getIntent().getStringExtra(WALLET_ADDRESS);
    viewModel = new FinishGameActivityViewModel(
        new GetRoomUseCase(new RoomRepository(RoomApiFactory.buildRoomApi())), roomId,
        walletAddress);

    viewModel.isWinner()
        .observeOn(AndroidSchedulers.mainThread())
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
