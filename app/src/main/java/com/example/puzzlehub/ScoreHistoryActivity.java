package com.example.puzzlehub;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puzzlehub.adapter.ScoreAdapter;
import com.example.puzzlehub.db.AppDatabase;
import com.example.puzzlehub.db.ScoreDao;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.concurrent.Executors;

/**
 * ScoreHistoryActivity - Displays saved game scores from the Room database.
 *
 * RECYCLERVIEW:
 * Uses a RecyclerView with LinearLayoutManager to show a scrollable list of scores.
 * The RecyclerView efficiently recycles item views as the user scrolls.
 *
 * ROOM DATABASE (LiveData):
 * Scores are loaded from the Room database using LiveData.
 * LiveData automatically updates the UI when the database data changes.
 * The observe() method watches for changes and updates the RecyclerView.
 *
 * FILTER CHIPS:
 * Material Design Chips allow filtering scores by game type (All, Memory, Slide).
 */
public class ScoreHistoryActivity extends AppCompatActivity {
    private ScoreAdapter adapter;
    private ScoreDao scoreDao;
    private TextView tvEmpty;
    private RecyclerView rvScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        // Get the DAO (Data Access Object) from the Room database singleton
        scoreDao = AppDatabase.getInstance(this).scoreDao();

        // Create the adapter for the RecyclerView
        adapter = new ScoreAdapter();

        rvScores = findViewById(R.id.rvScores);
        tvEmpty = findViewById(R.id.tvEmpty);

        // LinearLayoutManager arranges items in a vertical list
        rvScores.setLayoutManager(new LinearLayoutManager(this));
        rvScores.setAdapter(adapter);

        // Filter chips to show scores by game type
        Chip chipAll = findViewById(R.id.chipAll);
        Chip chipMemory = findViewById(R.id.chipMemory);
        Chip chipSlide = findViewById(R.id.chipSlide);

        chipAll.setOnClickListener(v -> loadAllScores());
        chipMemory.setOnClickListener(v -> loadScoresByType("MEMORY"));
        chipSlide.setOnClickListener(v -> loadScoresByType("SLIDE"));

        // Clear all scores with confirmation dialog
        MaterialButton btnClearAll = findViewById(R.id.btnClearAll);
        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Clear All Scores")
                    .setMessage("Are you sure you want to delete all scores?")
                    .setPositiveButton("Yes", (d, w) -> {
                        // Delete on background thread (database operations can't run on main thread)
                        Executors.newSingleThreadExecutor().execute(() -> scoreDao.deleteAll());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Load all scores when the screen opens
        loadAllScores();
    }

    /**
     * Loads all scores from the database using LiveData.
     * LiveData.observe() automatically updates when data changes.
     */
    private void loadAllScores() {
        scoreDao.getAllScores().observe(this, scores -> {
            adapter.setScores(scores);
            tvEmpty.setVisibility(scores.isEmpty() ? View.VISIBLE : View.GONE);
            rvScores.setVisibility(scores.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }

    /** Loads scores filtered by game type */
    private void loadScoresByType(String type) {
        scoreDao.getScoresByType(type).observe(this, scores -> {
            adapter.setScores(scores);
            tvEmpty.setVisibility(scores.isEmpty() ? View.VISIBLE : View.GONE);
            rvScores.setVisibility(scores.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }
}
