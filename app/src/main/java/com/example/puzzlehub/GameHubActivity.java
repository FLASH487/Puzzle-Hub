package com.example.puzzlehub;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class GameHubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hub);

        MaterialCardView cardMemory = findViewById(R.id.cardMemory);
        MaterialCardView cardSliding = findViewById(R.id.cardSliding);

        cardMemory.setOnClickListener(v -> showMemoryDifficultyDialog());
        cardSliding.setOnClickListener(v -> showSlidingDifficultyDialog());
    }

    private void showMemoryDifficultyDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_difficulty)
                .setItems(new String[]{"Easy (4×4)", "Hard (6×6)"}, (dialog, which) -> {
                    Intent intent = new Intent(this, MemoryMatchActivity.class);
                    intent.putExtra("GAME_TYPE", "MEMORY");
                    intent.putExtra("DIFFICULTY", which == 0 ? "EASY" : "HARD");
                    startActivity(intent);
                })
                .show();
    }

    private void showSlidingDifficultyDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_difficulty)
                .setItems(new String[]{"3×3", "4×4"}, (dialog, which) -> {
                    Intent intent = new Intent(this, SlidingPuzzleActivity.class);
                    intent.putExtra("GAME_TYPE", "SLIDE");
                    intent.putExtra("DIFFICULTY", which == 0 ? "3x3" : "4x4");
                    startActivity(intent);
                })
                .show();
    }
}
