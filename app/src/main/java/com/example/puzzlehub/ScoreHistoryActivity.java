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

public class ScoreHistoryActivity extends AppCompatActivity {
    private ScoreAdapter adapter;
    private ScoreDao scoreDao;
    private TextView tvEmpty;
    private RecyclerView rvScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_history);

        scoreDao = AppDatabase.getInstance(this).scoreDao();
        adapter = new ScoreAdapter();

        rvScores = findViewById(R.id.rvScores);
        tvEmpty = findViewById(R.id.tvEmpty);
        rvScores.setLayoutManager(new LinearLayoutManager(this));
        rvScores.setAdapter(adapter);

        Chip chipAll = findViewById(R.id.chipAll);
        Chip chipMemory = findViewById(R.id.chipMemory);
        Chip chipSlide = findViewById(R.id.chipSlide);

        chipAll.setOnClickListener(v -> loadAllScores());
        chipMemory.setOnClickListener(v -> loadScoresByType("MEMORY"));
        chipSlide.setOnClickListener(v -> loadScoresByType("SLIDE"));

        MaterialButton btnClearAll = findViewById(R.id.btnClearAll);
        btnClearAll.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Clear All Scores")
                    .setMessage("Are you sure you want to delete all scores?")
                    .setPositiveButton("Yes", (d, w) -> {
                        Executors.newSingleThreadExecutor().execute(() -> scoreDao.deleteAll());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        loadAllScores();
    }

    private void loadAllScores() {
        scoreDao.getAllScores().observe(this, scores -> {
            adapter.setScores(scores);
            tvEmpty.setVisibility(scores.isEmpty() ? View.VISIBLE : View.GONE);
            rvScores.setVisibility(scores.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }

    private void loadScoresByType(String type) {
        scoreDao.getScoresByType(type).observe(this, scores -> {
            adapter.setScores(scores);
            tvEmpty.setVisibility(scores.isEmpty() ? View.VISIBLE : View.GONE);
            rvScores.setVisibility(scores.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }
}
