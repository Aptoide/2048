package com.appcoins.eskills2048;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class InstallWalletFragment extends Fragment {

  public InstallWalletFragment() {
    super(R.layout.install_wallet_layout);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
  Bundle savedInstanceState) {

    View  view = inflater.inflate(R.layout.install_wallet_layout, container, false);
    return view;
  }
}
