package com.appcoins.eskills2048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appcoins.eskills2048.model.UserRankings;

import java.util.List;

public class RankingsAdapter extends RecyclerView.Adapter<RankingsAdapter.ViewHolder> {

    private Context mContext;
    private List<UserRankings> playerList;

    public RankingsAdapter(Context mContext, List<UserRankings> playerList) {
        this.mContext = mContext;
        this.playerList = playerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        v = layoutInflater.inflate(R.layout.player_rank_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.username.setText(playerList.get(position).getRankingUsername());
        holder.rank.setText(String.valueOf(playerList.get(position).getRankPosition()));
        holder.score.setText(String.valueOf(playerList.get(position).getRankingScore()));
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView username;
        TextView rank;
        TextView score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.rankingUsername);
            rank = itemView.findViewById(R.id.rankingRank);
            score = itemView.findViewById(R.id.rankingScore);
        }
    }
}
