package com.example.puzzlehub.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScoreDao {
    @Insert
    void insert(ScoreEntity score);

    @Query("SELECT * FROM scores ORDER BY dateMillis DESC")
    LiveData<List<ScoreEntity>> getAllScores();

    @Query("SELECT * FROM scores WHERE gameType = :type ORDER BY dateMillis DESC")
    LiveData<List<ScoreEntity>> getScoresByType(String type);

    @Query("DELETE FROM scores")
    void deleteAll();
}
