package com.example.puzzlehub;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.puzzlehub.fragment.BaseMemoryFragment;
import com.example.puzzlehub.fragment.MemoryEasyFragment;
import com.example.puzzlehub.fragment.MemoryHardFragment;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

/**
 * MemoryMatchActivity - Controls the Memory Match Cards game.
 *
 * FRAGMENTS:
 * This Activity uses Fragments to display different game boards.
 * A Fragment is a reusable piece of UI that lives inside an Activity.
 * - MemoryEasyFragment shows a 4×4 grid
 * - MemoryHardFragment shows a 6×6 grid
 * The Fragment is loaded into a FrameLayout container in the XML layout.
 *
 * READING INTENT EXTRAS:
 * This Activity reads the "DIFFICULTY" extra that was passed from GameHubActivity
 * using getIntent().getStringExtra("DIFFICULTY") to decide which fragment to load.
 *
 * TIMER:
 * Uses a simple Handler to count elapsed seconds (no advanced threading).
 */
public class MemoryMatchActivity extends AppCompatActivity {
    private TextView tvTimer;
    private TextView tvMoves;
    // Handler is used for the simple timer - it runs code on the main thread after a delay
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private int elapsedSeconds = 0;
    private boolean timerRunning = false;

    // Runnable that increments the timer every 1 second
    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedSeconds++;
            updateTimerDisplay();
            // Schedule this runnable to run again in 1 second (1000ms)
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_match);

        tvTimer = findViewById(R.id.tvTimer);
        tvMoves = findViewById(R.id.tvMoves);
        MaterialButton btnRestart = findViewById(R.id.btnRestart);

        // READING INTENT EXTRAS: Get the difficulty that was passed from GameHubActivity
        String difficulty = getIntent().getStringExtra("DIFFICULTY");

        // Choose which Fragment to display based on the difficulty
        Fragment fragment;
        if ("HARD".equals(difficulty)) {
            fragment = MemoryHardFragment.newInstance();
        } else {
            fragment = MemoryEasyFragment.newInstance();
        }

        // FRAGMENT TRANSACTION: Load the chosen fragment into the container
        // replace() swaps the fragment in the FrameLayout with id "fragmentContainer"
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

        // Restart button resets the timer, moves, and the game board
        btnRestart.setOnClickListener(v -> {
            stopTimer();
            elapsedSeconds = 0;
            updateTimerDisplay();
            tvMoves.setText("Moves: 0");

            // Get the current fragment and call its resetGame() method
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if (currentFragment instanceof BaseMemoryFragment) {
                ((BaseMemoryFragment) currentFragment).resetGame();
            }
        });
    }

    /** Called by the Fragment to update the move counter display */
    public void updateMoves(int moves) {
        tvMoves.setText(String.format(Locale.US, "Moves: %d", moves));
    }

    /** Starts the timer (called on the first move) */
    public void startTimer() {
        if (!timerRunning) {
            timerRunning = true;
            timerHandler.postDelayed(timerRunnable, 1000);
        }
    }

    /** Stops the timer and returns the elapsed seconds */
    public int stopTimer() {
        timerRunning = false;
        timerHandler.removeCallbacks(timerRunnable);
        return elapsedSeconds;
    }

    /** Updates the timer display in MM:SS format */
    private void updateTimerDisplay() {
        int min = elapsedSeconds / 60;
        int sec = elapsedSeconds % 60;
        tvTimer.setText(String.format(Locale.US, "Time: %02d:%02d", min, sec));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up: remove any pending timer callbacks to prevent memory leaks
        timerHandler.removeCallbacks(timerRunnable);
    }
}
