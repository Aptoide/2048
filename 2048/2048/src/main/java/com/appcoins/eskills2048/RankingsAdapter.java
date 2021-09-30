package com.appcoins.eskills2048;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.appcoins.eskills2048.model.RankingsItem;
import com.appcoins.eskills2048.model.RankingsTitle;
import com.appcoins.eskills2048.model.UserRankings;
import java.util.List;

public class RankingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final String TAG = "RankingsAdapter";
  private List<RankingsItem> rankingsItems;

  private static final DiffUtil.ItemCallback<RankingsItem> DIFF_CALLBACK =
      new DiffUtil.ItemCallback<RankingsItem>() {
        @Override public boolean areItemsTheSame(@NonNull RankingsItem oldProduct,
            @NonNull RankingsItem newProduct) {
          return oldProduct.equals(newProduct);
        }

        @Override public boolean areContentsTheSame(@NonNull RankingsItem oldProduct,
            @NonNull RankingsItem newProduct) {
          return areItemsTheSame(oldProduct, newProduct);
        }
      };

  private final AsyncListDiffer<RankingsItem> differ = new AsyncListDiffer<>(this, DIFF_CALLBACK);
  private final LayoutInflater layoutInflater;

  public RankingsAdapter(List<RankingsItem> rankingsItems, LayoutInflater layoutInflater) {
    this.rankingsItems = rankingsItems;
    this.layoutInflater = layoutInflater;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    Log.d(TAG, String.format("\n\ntype=%d\n\n", viewType));
    if (viewType == 1) {

      View v = layoutInflater.inflate(R.layout.player_rank_layout, parent, false);
      return new PlayerStatsViewHolder(v);
    }
    View v = layoutInflater.inflate(R.layout.rankings_title, parent, false);
    return new RankingTitleViewHolder(v);
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (getItemViewType(position) == 1) {
      UserRankings player = (UserRankings) rankingsItems.get(position);
      ((PlayerStatsViewHolder) holder).setPlayerStats(player);
    } else {
      RankingsTitle title = (RankingsTitle) rankingsItems.get(position);
      ((RankingTitleViewHolder) holder).setTitle(title);
    }
  }

  public void setRankings(List<RankingsItem> rankingsItems) {
    this.rankingsItems = rankingsItems;
    differ.submitList(rankingsItems);
  }

  @Override public int getItemCount() {
    return rankingsItems.size();
  }

  @Override public int getItemViewType(int position) {
    return rankingsItems.get(position)
        .getItemType();
  }

  static class PlayerStatsViewHolder extends RecyclerView.ViewHolder {

    TextView username;
    TextView rank;
    TextView score;

    PlayerStatsViewHolder(@NonNull View itemView) {
      super(itemView);
      username = itemView.findViewById(R.id.rankingUsername);
      rank = itemView.findViewById(R.id.rankingRank);
      score = itemView.findViewById(R.id.rankingScore);
    }

    void setPlayerStats(UserRankings player) {
      username.setText(player.getRankingUsername());
      score.setText(String.valueOf(player.getRankingScore()));
      rank.setText(String.valueOf(player.getRankPosition()));
    }
  }

  static class RankingTitleViewHolder extends RecyclerView.ViewHolder {

    TextView rankingTitle;

    RankingTitleViewHolder(@NonNull View itemView) {
      super(itemView);
      rankingTitle = itemView.findViewById(R.id.rankingTitle);
    }

    void setTitle(RankingsTitle title) {
      rankingTitle.setText(title.getTitle());
    }
  }
}
