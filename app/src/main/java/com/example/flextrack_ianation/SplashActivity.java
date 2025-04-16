package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

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

        // Find views
        TextView appNameText = findViewById(R.id.splash_app_name);

        // Create fade-in animation
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);

        // Set animation listeners
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Start a handler to delay the next activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Create an Intent to start the next Activity
                        Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(mainIntent);
                        finish(); // Close this activity
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start animations
        appNameText.startAnimation(fadeIn);
    }
} 