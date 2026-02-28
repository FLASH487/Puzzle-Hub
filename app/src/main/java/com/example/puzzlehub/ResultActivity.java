package com.example.puzzlehub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.puzzlehub.db.AppDatabase;
import com.example.puzzlehub.db.ScoreEntity;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.concurrent.Executors;

public class ResultActivity extends AppCompatActivity {
    private String gameType;
    private String difficulty;
    private int moves;
    private int timeSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        gameType = getIntent().getStringExtra("GAME_TYPE");
        difficulty = getIntent().getStringExtra("DIFFICULTY");
        moves = getIntent().getIntExtra("MOVES", 0);
        timeSeconds = getIntent().getIntExtra("TIME_SECONDS", 0);

        String gameName = "MEMORY".equals(gameType) ? "Memory Match Cards" : "Sliding Number Puzzle";
        int min = timeSeconds / 60;
        int sec = timeSeconds % 60;
        String timeStr = String.format(Locale.US, "%02d:%02d", min, sec);

        TextView tvGameType = findViewById(R.id.tvGameType);
        TextView tvDifficulty = findViewById(R.id.tvDifficulty);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvMovesResult = findViewById(R.id.tvMovesResult);

        tvGameType.setText("Game: " + gameName);
        tvDifficulty.setText("Difficulty: " + difficulty);
        tvTime.setText("Time: " + timeStr);
        tvMovesResult.setText("Moves: " + moves);

        MaterialButton btnSaveScore = findViewById(R.id.btnSaveScore);
        MaterialButton btnShare = findViewById(R.id.btnShare);
        MaterialButton btnPlayAgain = findViewById(R.id.btnPlayAgain);
        MaterialButton btnHome = findViewById(R.id.btnHome);

        btnSaveScore.setOnClickListener(v -> {
            ScoreEntity score = new ScoreEntity(gameType, difficulty, moves, timeSeconds, System.currentTimeMillis());
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(this).scoreDao().insert(score);
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                    btnSaveScore.setEnabled(false);
                });
            });
        });

        btnShare.setOnClickListener(v -> {
            String shareText = String.format(Locale.US,
                    "I solved %s (%s) in %s with %d moves!",
                    gameName, difficulty, timeStr, moves);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share your score"));
        });

        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameHubActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
