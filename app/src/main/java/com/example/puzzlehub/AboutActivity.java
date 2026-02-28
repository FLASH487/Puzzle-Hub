package com.example.puzzlehub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

/**
 * AboutActivity - Shows information about the app.
 *
 * IMPLICIT INTENT (ACTION_VIEW):
 * The "Open Project Page" button uses an Implicit Intent with ACTION_VIEW.
 * ACTION_VIEW tells Android to open a URL in the default web browser.
 * This is an implicit intent because we don't specify which browser to use -
 * Android decides which app can handle viewing the URL.
 *
 * This is different from ACTION_SEND (used in ResultActivity) which shares text.
 * Both are implicit intents but they perform different actions.
 */
public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        MaterialButton btnOpenProject = findViewById(R.id.btnOpenProject);
        btnOpenProject.setOnClickListener(v -> {
            // IMPLICIT INTENT (ACTION_VIEW): Opens a URL in the default web browser
            // Uri.parse() converts the URL string into a Uri object
            // The system will find an appropriate app (browser) to handle this intent
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://example.com"));
            startActivity(intent);
        });
    }
}
