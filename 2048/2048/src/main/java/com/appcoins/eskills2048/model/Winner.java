package com.appcoins.eskills2048.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data public class Winner {

  @SerializedName("wallet_address") private String walletAddress;
}
