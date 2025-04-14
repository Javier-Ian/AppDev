package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Set up bottom navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);

        // Select the progress tab (position 2)
        bottomNavigation.getMenu().getItem(2).setChecked(true);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = -1;

                // Find the position of the selected item
                for (int i = 0; i < bottomNavigation.getMenu().size(); i++) {
                    if (bottomNavigation.getMenu().getItem(i) == item) {
                        position = i;
                        break;
                    }
                }

                // Navigate based on position
                switch (position) {
                    case 0: // Steps Counter
                        startActivity(new Intent(ProgressActivity.this, StepsCounterActivity.class));
                        finish();
                        return true;
                    case 1: // Workout Plan
                        startActivity(new Intent(ProgressActivity.this, WorkoutPlanActivity.class));
                        finish();
                        return true;
                    case 2: // Progress (current)
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
}
