package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity is the entry point of the app.
 * It displays a minimal splash screen with just the app name,
 * then redirects to the SignInActivity after a short delay.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Use a Handler to delay loading the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the next Activity
                Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(mainIntent);
                
                // Disable transition animation
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                
                // Close this activity
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
} 