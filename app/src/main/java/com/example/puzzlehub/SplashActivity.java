package com.example.puzzlehub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity - The first screen the user sees when the app opens.
 *
 * WHAT IS AN ACTIVITY?
 * An Activity is a single screen in an Android app. Each screen you see
 * (splash, home, game, etc.) is a separate Activity. Activities are declared
 * in AndroidManifest.xml so the Android system knows about them.
 *
 * ACTIVITY LIFECYCLE:
 * When this Activity starts, Android calls onCreate() automatically.
 * This is where we set up the screen layout and start any logic.
 *
 * This splash screen shows the app logo for 1.5 seconds, then navigates
 * to HomeActivity using an Explicit Intent (we specify exactly which
 * Activity to open).
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView connects this Activity to its XML layout file
        setContentView(R.layout.activity_splash);

        // Handler is used to create a simple delay (1.5 seconds = 1500 milliseconds)
        // After the delay, we navigate to HomeActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // EXPLICIT INTENT: We specify exactly which Activity to open (HomeActivity.class)
            // This is called an "explicit intent" because we explicitly name the target
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            // finish() removes this Activity from the back stack so user can't go back to splash
            finish();
        }, 1500);
    }
}
