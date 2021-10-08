package com.appcoins.eskills2048;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.appcoins.eskills2048.model.User;

import java.util.List;

public class PlayerRankingAdapter extends RecyclerView.Adapter<PlayerRankingAdapter.ViewHolder> {
  private List<User> localDataSet;
  private final Context context;

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final ImageView medalImageView;
    private final TextView userNameTextView;
    private final TextView userScoreTextView;

    public ViewHolder(View view) {
      super(view);
      medalImageView = (ImageView) view.findViewById(R.id.medal_image);
      userNameTextView = (TextView) view.findViewById(R.id.user_name);
      userScoreTextView = (TextView) view.findViewById(R.id.user_score);
    }

    public ImageView getMedalImageView() {
      return medalImageView;
    }

    public TextView getUserNameTextView() {
      return userNameTextView;
    }

    public TextView getUserScoreTextView() {
      return userScoreTextView;
    }
  }

  public PlayerRankingAdapter(Context context, List<User> dataSet) {
    localDataSet = dataSet;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.player_row_item, viewGroup, false);

    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, final int position) {
    viewHolder.getMedalImageView().setImageDrawable(getMedalDrawable(position));
    viewHolder.getUserNameTextView().setText(localDataSet.get(position).getUserName());
    viewHolder.getUserScoreTextView().setText(
        context.getResources().getString(R.string.rank_score_details,
            localDataSet.get(position).getScore()));
  }

  private Drawable getMedalDrawable(int position) {
    switch (position) {
      case 1:
        return ResourcesCompat.getDrawable(
            context.getResources(), R.drawable.ic_medal_second_place, null);
      case 2:
        return ResourcesCompat.getDrawable(
            context.getResources(), R.drawable.ic_medal_third_place, null);
      default:
        return ResourcesCompat.getDrawable(
            context.getResources(), R.drawable.ic_medal_first_place, null);
    }
  }

  @Override
  public int getItemCount() {
    return localDataSet.size();
  }

  public void updateData(List<User> dataSet) {
    localDataSet = dataSet;
    notifyDataSetChanged();
  }
}
