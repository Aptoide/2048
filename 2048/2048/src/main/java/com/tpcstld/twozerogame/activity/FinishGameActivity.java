package com.tpcstld.twozerogame.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.tpcstld.twozerogame.R;
import com.tpcstld.twozerogame.databinding.ActivityFinishGameBinding;

public class FinishGameActivity extends AppCompatActivity {

  private ActivityFinishGameBinding binding;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityFinishGameBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
  }
}
