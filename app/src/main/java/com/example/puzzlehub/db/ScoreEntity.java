package com.example.puzzlehub.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scores")
public class ScoreEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String gameType;
    public String difficulty;
    public int moves;
    public int timeSeconds;
    public long dateMillis;

    public ScoreEntity(String gameType, String difficulty, int moves, int timeSeconds, long dateMillis) {
        this.gameType = gameType;
        this.difficulty = difficulty;
        this.moves = moves;
        this.timeSeconds = timeSeconds;
        this.dateMillis = dateMillis;
    }
}
