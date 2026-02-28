package com.example.puzzlehub;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialButton btnPlayGames = findViewById(R.id.btnPlayGames);
        MaterialButton btnScoreHistory = findViewById(R.id.btnScoreHistory);
        MaterialButton btnHowToPlay = findViewById(R.id.btnHowToPlay);
        MaterialButton btnAbout = findViewById(R.id.btnAbout);

        btnPlayGames.setOnClickListener(v ->
                startActivity(new Intent(this, GameHubActivity.class)));
        btnScoreHistory.setOnClickListener(v ->
                startActivity(new Intent(this, ScoreHistoryActivity.class)));
        btnHowToPlay.setOnClickListener(v ->
                startActivity(new Intent(this, HowToPlayActivity.class)));
        btnAbout.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));
    }
}
