package com.example.puzzlehub;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

/**
 * GameHubActivity - Game selection screen where the user picks a game.
 *
 * PASSING DATA WITH INTENT EXTRAS:
 * When the user selects a game and difficulty, we create an Intent and
 * attach extra data to it using intent.putExtra("KEY", value).
 * The receiving Activity can then read this data using
 * getIntent().getStringExtra("KEY").
 *
 * This demonstrates:
 * - Explicit Intents (navigating to MemoryMatchActivity or SlidingPuzzleActivity)
 * - Intent Extras (passing GAME_TYPE and DIFFICULTY to the next Activity)
 * - MaterialCardView for game selection cards
 */
public class GameHubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hub);

        // MaterialCardView is used for clickable game selection cards
        MaterialCardView cardMemory = findViewById(R.id.cardMemory);
        MaterialCardView cardSliding = findViewById(R.id.cardSliding);

        // When a card is clicked, show a difficulty selection dialog
        cardMemory.setOnClickListener(v -> showMemoryDifficultyDialog());
        cardSliding.setOnClickListener(v -> showSlidingDifficultyDialog());
    }

    /**
     * Shows a dialog to let the user choose Memory Match difficulty.
     * After selection, navigates to MemoryMatchActivity with Intent extras.
     */
    private void showMemoryDifficultyDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_difficulty)
                .setItems(new String[]{"Easy (4×4)", "Hard (6×6)"}, (dialog, which) -> {
                    // EXPLICIT INTENT with EXTRAS: Navigate to MemoryMatchActivity
                    Intent intent = new Intent(this, MemoryMatchActivity.class);
                    // putExtra() attaches data to the intent so the next Activity can read it
                    intent.putExtra("GAME_TYPE", "MEMORY");
                    intent.putExtra("DIFFICULTY", which == 0 ? "EASY" : "HARD");
                    startActivity(intent);
                })
                .show();
    }

    /**
     * Shows a dialog to let the user choose Sliding Puzzle difficulty.
     * After selection, navigates to SlidingPuzzleActivity with Intent extras.
     */
    private void showSlidingDifficultyDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_difficulty)
                .setItems(new String[]{"3×3", "4×4"}, (dialog, which) -> {
                    // EXPLICIT INTENT with EXTRAS: Navigate to SlidingPuzzleActivity
                    Intent intent = new Intent(this, SlidingPuzzleActivity.class);
                    intent.putExtra("GAME_TYPE", "SLIDE");
                    intent.putExtra("DIFFICULTY", which == 0 ? "3x3" : "4x4");
                    startActivity(intent);
                })
                .show();
    }
}
