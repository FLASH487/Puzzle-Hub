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

/**
 * ResultActivity - Shows the game result after the player wins.
 *
 * READING INTENT EXTRAS:
 * This Activity receives game data (type, difficulty, moves, time) from
 * the game fragment via Intent extras. It reads them using:
 * - getIntent().getStringExtra("KEY") for Strings
 * - getIntent().getIntExtra("KEY", defaultValue) for integers
 *
 * IMPLICIT INTENT (ACTION_SEND):
 * The Share button uses an Implicit Intent with ACTION_SEND.
 * An Implicit Intent does NOT specify which app to open. Instead, it tells
 * Android "I want to send text" and Android shows a chooser with all apps
 * that can handle sending text (WhatsApp, Email, etc.).
 *
 * ROOM DATABASE:
 * The Save Score button saves the result to the local Room database
 * using a background thread (Executors) to avoid blocking the UI.
 */
public class ResultActivity extends AppCompatActivity {
    private String gameType;
    private String difficulty;
    private int moves;
    private int timeSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // READING INTENT EXTRAS: Get the game results passed from the game fragment
        gameType = getIntent().getStringExtra("GAME_TYPE");
        difficulty = getIntent().getStringExtra("DIFFICULTY");
        moves = getIntent().getIntExtra("MOVES", 0);
        timeSeconds = getIntent().getIntExtra("TIME_SECONDS", 0);

        // Format the data for display
        String gameName = "MEMORY".equals(gameType) ? "Memory Match Cards" : "Sliding Number Puzzle";
        int min = timeSeconds / 60;
        int sec = timeSeconds % 60;
        String timeStr = String.format(Locale.US, "%02d:%02d", min, sec);

        // Find and set TextViews with game result data
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

        // ROOM DATABASE: Save the score to the local database
        // Database operations must run on a background thread (not the main UI thread)
        btnSaveScore.setOnClickListener(v -> {
            ScoreEntity score = new ScoreEntity(gameType, difficulty, moves, timeSeconds, System.currentTimeMillis());
            Executors.newSingleThreadExecutor().execute(() -> {
                // Insert the score into Room database (runs on background thread)
                AppDatabase.getInstance(this).scoreDao().insert(score);
                // Show a toast message on the main thread
                runOnUiThread(() -> {
                    Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                    btnSaveScore.setEnabled(false);
                });
            });
        });

        // IMPLICIT INTENT (ACTION_SEND): Share the score via any app
        // This is an "implicit intent" because we don't specify which app to use.
        // Android will show a chooser dialog with compatible apps.
        btnShare.setOnClickListener(v -> {
            String shareText = String.format(Locale.US,
                    "I solved %s (%s) in %s with %d moves! 🎉",
                    gameName, difficulty, timeStr, moves);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share your score 💌"));
        });

        // EXPLICIT INTENT: Navigate back to GameHubActivity
        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameHubActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // EXPLICIT INTENT: Navigate back to HomeActivity
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
