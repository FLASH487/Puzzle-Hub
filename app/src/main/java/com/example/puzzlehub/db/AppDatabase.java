package com.example.puzzlehub.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * AppDatabase - Room Database singleton for the Puzzle Hub app.
 *
 * HOW ROOM DATABASE WORKS:
 * Room is a library that makes it easy to store data locally on the device.
 * It uses SQLite under the hood but provides a simpler API.
 *
 * Key components:
 * 1. Entity (ScoreEntity) - Defines the table structure (like a class with fields)
 * 2. DAO (ScoreDao) - Defines database operations (insert, query, delete)
 * 3. Database (this class) - The main access point, created as a singleton
 *
 * SINGLETON PATTERN:
 * We only create ONE instance of the database for the entire app.
 * getInstance() checks if an instance already exists before creating a new one.
 * This prevents creating multiple database connections which wastes resources.
 *
 * The "synchronized" keyword ensures only one thread can create the instance at a time.
 */
@Database(entities = {ScoreEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    // Volatile ensures the variable is always read from main memory, not from cache
    private static volatile AppDatabase INSTANCE;

    // Abstract method that Room generates the implementation for
    public abstract ScoreDao scoreDao();

    /**
     * Gets the singleton database instance.
     * If the database doesn't exist yet, it creates one using Room.databaseBuilder().
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Room.databaseBuilder creates the database file on the device
                    // "puzzle_hub_db" is the name of the database file
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "puzzle_hub_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
