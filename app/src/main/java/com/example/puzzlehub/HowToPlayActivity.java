package com.example.puzzlehub;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * HowToPlayActivity - Displays instructions for both games.
 *
 * This is a simple Activity that only shows a ScrollView with text instructions.
 * It demonstrates that an Activity can be very simple - just a screen with content.
 * The layout uses ScrollView > LinearLayout to allow scrolling if content is long.
 */
public class HowToPlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
    }
}
