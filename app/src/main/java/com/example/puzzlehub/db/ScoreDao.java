package com.example.puzzlehub.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * ScoreDao - Data Access Object for score operations.
 *
 * WHAT IS A DAO?
 * A DAO (Data Access Object) is an interface that defines all the database
 * operations we need. Room automatically generates the implementation code
 * from the annotations (@Insert, @Query).
 *
 * HOW ROOM STORES DATA:
 * - @Insert automatically generates SQL INSERT code
 * - @Query lets us write custom SQL queries
 * - LiveData wraps the result so the UI updates automatically when data changes
 *
 * Note: Database operations (insert, delete) must run on a background thread.
 * Query operations that return LiveData are automatically handled by Room.
 */
@Dao
public interface ScoreDao {
    /** Inserts a new score into the database */
    @Insert
    void insert(ScoreEntity score);

    /** Gets all scores sorted by date (newest first), wrapped in LiveData for auto-updates */
    @Query("SELECT * FROM scores ORDER BY dateMillis DESC")
    LiveData<List<ScoreEntity>> getAllScores();

    /** Gets scores filtered by game type (MEMORY or SLIDE) */
    @Query("SELECT * FROM scores WHERE gameType = :type ORDER BY dateMillis DESC")
    LiveData<List<ScoreEntity>> getScoresByType(String type);

    /** Deletes all scores from the database */
    @Query("DELETE FROM scores")
    void deleteAll();
}
