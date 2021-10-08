package com.appcoins.eskills2048;

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
import com.appcoins.eskills2048.model.UserRankingsItem;
import java.util.List;

public class RankingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final String TAG = "RankingsAdapter";

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

  public RankingsAdapter(LayoutInflater layoutInflater) {
    this.layoutInflater = layoutInflater;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    if (viewType == R.layout.player_rank_layout) {
      View v = layoutInflater.inflate(R.layout.player_rank_layout, parent, false);
      return new PlayerStatsViewHolder(v);
    } else if (viewType == R.layout.rankings_title) {
      View v = layoutInflater.inflate(R.layout.rankings_title, parent, false);
      return new RankingTitleViewHolder(v);
    } else {
      throw new RuntimeException("Invalid view type " + viewType);
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    int itemViewType = getItemViewType(position);
    if (itemViewType == R.layout.player_rank_layout) {
      UserRankingsItem player = (UserRankingsItem) differ.getCurrentList()
          .get(position);
      ((PlayerStatsViewHolder) holder).setPlayerStats(player);
    } else if (itemViewType == R.layout.rankings_title) {
      RankingsTitle title = (RankingsTitle) differ.getCurrentList()
          .get(position);
      ((RankingTitleViewHolder) holder).setTitle(title);
    } else {
      throw new RuntimeException("Invalid view type " + itemViewType);
    }
  }

  public void setRankings(List<RankingsItem> rankingsItems) {
    differ.submitList(rankingsItems);
  }

  @Override public int getItemCount() {
    return differ.getCurrentList()
        .size();
  }

  @Override public int getItemViewType(int position) {
    return differ.getCurrentList()
        .get(position)
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

    void setPlayerStats(UserRankingsItem player) {
      username.setText(player.getUserName());
      score.setText(String.valueOf(player.getScore()));
      rank.setText(String.valueOf(player.getRank()));
      int color;
      if (player.isCurrentUser()) {
        color = itemView.getResources()
            .getColor(R.color.icon_background);
      } else {
        color = itemView.getResources()
            .getColor(R.color.rankings_text_color);
      }
      username.setTextColor(color);
      rank.setTextColor(color);
      score.setTextColor(color);
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
