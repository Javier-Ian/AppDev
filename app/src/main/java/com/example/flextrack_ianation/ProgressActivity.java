package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.flextrack_ianation.adapters.ProgressPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class ProgressActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ProgressPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Set up ViewPager and TabLayout
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        
        // Create and set up the adapter
        pagerAdapter = new ProgressPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        
        // Connect the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);

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
