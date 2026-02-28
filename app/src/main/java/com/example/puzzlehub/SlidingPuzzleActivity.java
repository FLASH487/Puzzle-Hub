package com.example.puzzlehub;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.puzzlehub.fragment.BaseSlidingFragment;
import com.example.puzzlehub.fragment.Slide3x3Fragment;
import com.example.puzzlehub.fragment.Slide4x4Fragment;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

/**
 * SlidingPuzzleActivity - Controls the Sliding Number Puzzle game.
 *
 * FRAGMENTS:
 * This Activity uses Fragments to display different puzzle boards.
 * - Slide3x3Fragment shows a 3×3 grid (8 numbered tiles)
 * - Slide4x4Fragment shows a 4×4 grid (15 numbered tiles)
 *
 * READING INTENT EXTRAS:
 * Reads "DIFFICULTY" from the Intent to decide which fragment to load.
 *
 * TIMER:
 * Uses a simple Handler to count elapsed seconds (same approach as MemoryMatchActivity).
 */
public class SlidingPuzzleActivity extends AppCompatActivity {
    private TextView tvTimer;
    private TextView tvMoves;
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private int elapsedSeconds = 0;
    private boolean timerRunning = false;

    // Timer runnable - runs every second to update the display
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedSeconds++;
            updateTimerDisplay();
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_puzzle);

        tvTimer = findViewById(R.id.tvTimer);
        tvMoves = findViewById(R.id.tvMoves);
        MaterialButton btnRestart = findViewById(R.id.btnRestart);

        // READING INTENT EXTRAS: Get difficulty passed from GameHubActivity
        String difficulty = getIntent().getStringExtra("DIFFICULTY");

        // Choose the correct Fragment based on difficulty
        Fragment fragment;
        if ("4x4".equals(difficulty)) {
            fragment = Slide4x4Fragment.newInstance();
        } else {
            fragment = Slide3x3Fragment.newInstance();
        }

        // FRAGMENT TRANSACTION: Load the fragment into the container
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        // Restart button handler
        btnRestart.setOnClickListener(v -> {
            stopTimer();
            elapsedSeconds = 0;
            updateTimerDisplay();
            tvMoves.setText("Moves: 0");

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if (currentFragment instanceof BaseSlidingFragment) {
                ((BaseSlidingFragment) currentFragment).resetGame();
            }
        });
    }

    /** Called by the Fragment to update the move counter display */
    public void updateMoves(int moves) {
        tvMoves.setText(String.format(Locale.US, "Moves: %d", moves));
    }

    /** Starts the timer on the first move */
    public void startTimer() {
        if (!timerRunning) {
            timerRunning = true;
            timerHandler.postDelayed(timerRunnable, 1000);
        }
    }

    /** Stops the timer and returns elapsed seconds */
    public int stopTimer() {
        timerRunning = false;
        timerHandler.removeCallbacks(timerRunnable);
        return elapsedSeconds;
    }

    private void updateTimerDisplay() {
        int min = elapsedSeconds / 60;
        int sec = elapsedSeconds % 60;
        tvTimer.setText(String.format(Locale.US, "Time: %02d:%02d", min, sec));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
