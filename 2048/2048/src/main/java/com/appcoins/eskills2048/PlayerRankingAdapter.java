package com.appcoins.eskills2048;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.appcoins.eskills2048.model.User;
import java.util.List;

public class PlayerRankingAdapter extends RecyclerView.Adapter<PlayerRankingAdapter.ViewHolder> {
  private List<User> localDataSet;

  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView userNameTextView;
    private final TextView userScoreTextView;

    public ViewHolder(View view) {
      super(view);
      userNameTextView = (TextView) view.findViewById(R.id.user_name);
      userScoreTextView = (TextView) view.findViewById(R.id.user_score);
    }

    public void bind(User user) {
      userNameTextView.setText(user.getUserName());
      userScoreTextView.setText(itemView.getResources()
          .getString(R.string.rank_score_details, user.getScore()));
    }
  }

  public PlayerRankingAdapter(List<User> dataSet) {
    localDataSet = dataSet;
  }

  @NonNull @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view = LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.player_row_item, viewGroup, false);

    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder viewHolder, final int position) {
    viewHolder.bind(localDataSet.get(position));
  }

  @Override public int getItemCount() {
    return localDataSet.size();
  }

  public void updateData(List<User> dataSet) {
    localDataSet = dataSet;
    notifyDataSetChanged();
  }
}
