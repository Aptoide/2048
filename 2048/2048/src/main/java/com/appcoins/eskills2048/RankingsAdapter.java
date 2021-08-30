package com.appcoins.eskills2048;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appcoins.eskills2048.model.RankingsItem;
import com.appcoins.eskills2048.model.RankingsTitle;
import com.appcoins.eskills2048.model.UserRankings;

import java.util.List;

public class RankingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RankingsAdapter";
    private List<RankingsItem> rankingsItems;
    private Context mContext;

    public RankingsAdapter(Context mContext, List<RankingsItem> rankingsItems) {
        this.mContext = mContext;
        this.rankingsItems = rankingsItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, String.format("\n\ntype=%d\n\n", viewType));
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (viewType == 1) {

            View v = layoutInflater.inflate(R.layout.player_rank_layout, parent, false);
            return new PlayerStatsViewHolder(v);
        }
        View v = layoutInflater.inflate(R.layout.rankings_title, parent, false);
        return new RankingTitleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 1) {
            UserRankings player = (UserRankings) rankingsItems.get(position);
            ((PlayerStatsViewHolder) holder).setPlayerStats(player);
        }
        else{
            RankingsTitle title = (RankingsTitle) rankingsItems.get(position);
            ((RankingTitleViewHolder) holder).setTitle(title);
        }

    }

    @Override
    public int getItemCount() {
        return rankingsItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return rankingsItems.get(position).getItemType();
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
