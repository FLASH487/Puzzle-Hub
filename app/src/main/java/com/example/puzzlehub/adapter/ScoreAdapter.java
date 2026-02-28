package com.example.puzzlehub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puzzlehub.R;
import com.example.puzzlehub.db.ScoreEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private List<ScoreEntity> scores = new ArrayList<>();

    public void setScores(List<ScoreEntity> scores) {
        this.scores = scores;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        ScoreEntity score = scores.get(position);
        String gameName = "MEMORY".equals(score.gameType) ? "Memory Match" : "Sliding Puzzle";
        holder.tvScoreGame.setText(gameName);
        holder.tvScoreDifficulty.setText(score.difficulty);

        int min = score.timeSeconds / 60;
        int sec = score.timeSeconds % 60;
        holder.tvScoreTime.setText(String.format(Locale.US, "%02d:%02d", min, sec));
        holder.tvScoreMoves.setText(score.moves + " moves");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        holder.tvScoreDate.setText(sdf.format(new Date(score.dateMillis)));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvScoreGame, tvScoreDifficulty, tvScoreTime, tvScoreMoves, tvScoreDate;

        ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScoreGame = itemView.findViewById(R.id.tvScoreGame);
            tvScoreDifficulty = itemView.findViewById(R.id.tvScoreDifficulty);
            tvScoreTime = itemView.findViewById(R.id.tvScoreTime);
            tvScoreMoves = itemView.findViewById(R.id.tvScoreMoves);
            tvScoreDate = itemView.findViewById(R.id.tvScoreDate);
        }
    }
}
