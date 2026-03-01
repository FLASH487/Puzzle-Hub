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
import com.example.puzzlehub.db.ScoreEntity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
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
 * We observe getAllScores() ONCE and keep a local copy of all scores.
 * When the user filters by chip, we filter the local list - no extra observers needed.
 *
 * FILTER CHIPS:
 * Material Design Chips allow filtering scores by game type (All, Memory, Slide).
 */
public class ScoreHistoryActivity extends AppCompatActivity {
    private ScoreAdapter adapter;
    private ScoreDao scoreDao;
    private TextView tvEmpty;
    private RecyclerView rvScores;
    // Store all scores locally so we can filter without adding multiple LiveData observers
    private List<ScoreEntity> allScores = new ArrayList<>();
    private String currentFilter = null;  // null = show all, "MEMORY" or "SLIDE" = filtered

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

        // Chips set the currentFilter and refresh the displayed list from local data
        chipAll.setOnClickListener(v -> { currentFilter = null; applyFilter(); });
        chipMemory.setOnClickListener(v -> { currentFilter = "MEMORY"; applyFilter(); });
        chipSlide.setOnClickListener(v -> { currentFilter = "SLIDE"; applyFilter(); });

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

        // Observe ALL scores ONCE - when data changes, update local copy and re-apply filter
        scoreDao.getAllScores().observe(this, scores -> {
            allScores = scores;
            applyFilter();
        });
    }

    /**
     * Applies the current filter to the locally stored score list and updates the RecyclerView.
     * This avoids adding multiple LiveData observers when chips are clicked.
     */
    private void applyFilter() {
        List<ScoreEntity> filtered;
        if (currentFilter == null) {
            filtered = allScores;
        } else {
            filtered = new ArrayList<>();
            for (ScoreEntity score : allScores) {
                if (currentFilter.equals(score.gameType)) {
                    filtered.add(score);
                }
            }
        }
        adapter.setScores(filtered);
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
        rvScores.setVisibility(filtered.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
