package com.example.puzzlehub;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

/**
 * HomeActivity - The main menu screen of the app.
 *
 * WHAT IS AN ACTIVITY?
 * An Activity represents a single screen. HomeActivity is the main hub
 * where the user can navigate to different parts of the app.
 *
 * EXPLICIT INTENTS:
 * All navigation from this screen uses Explicit Intents. An Explicit Intent
 * specifies the exact Activity class to open (e.g., GameHubActivity.class).
 * This is different from an Implicit Intent which asks the system to find
 * an appropriate app to handle an action.
 *
 * NAVIGATION:
 * - Play Games → GameHubActivity (choose a game)
 * - Score History → ScoreHistoryActivity (view saved scores)
 * - How To Play → HowToPlayActivity (game instructions)
 * - About → AboutActivity (app information)
 */
public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Find buttons from the XML layout using their IDs
        MaterialButton btnPlayGames = findViewById(R.id.btnPlayGames);
        MaterialButton btnScoreHistory = findViewById(R.id.btnScoreHistory);
        MaterialButton btnHowToPlay = findViewById(R.id.btnHowToPlay);
        MaterialButton btnAbout = findViewById(R.id.btnAbout);

        // EXPLICIT INTENTS: Each button navigates to a specific Activity
        // We use "new Intent(this, TargetActivity.class)" to create an explicit intent
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
