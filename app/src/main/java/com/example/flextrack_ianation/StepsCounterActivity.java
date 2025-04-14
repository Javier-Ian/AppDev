package com.example.flextrack_ianation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.flextrack_ianation.services.StepCounterService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.content.res.ColorStateList;

public class StepsCounterActivity extends AppCompatActivity {
    private static final String TAG = "StepsCounterActivity";
    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1001;
    private static final String PREFS_NAME = "StepCounterPrefs";
    
    // UI elements
    private TextView stepCountTextView;
    private TextView stepsLabelTextView;
    private ProgressBar stepsProgressBar;
    private TextView goalTextView;
    private Button actionButton;
    private Button resetStepsButton;
    private TextView caloriesValueTextView;
    private TextView distanceValueTextView;
    private TextView timeValueTextView;
    private TextView dailyStepsTextView;
    private TextView dailyGoalTextView;
    private ProgressBar dailyProgressBar;
    private FloatingActionButton setGoalFab;
    
    // Step counter service
    private StepCounterService stepCounterService;
    private boolean bound = false;
    
    // Default step goal
    private int stepGoal = 10000; // Fixed at 10000, no longer user-configurable
    
    // Service connection
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepCounterService.StepBinder binder = (StepCounterService.StepBinder) service;
            stepCounterService = binder.getService();
            bound = true;
            
            // Get the current hardware step count (should never be reset)
            int totalSteps = stepCounterService.getTotalSteps();
            
            // Update only the session steps to 0 - don't modify the hardware counter display
            int sessionSteps = 0; // Session always starts from 0
            
            // Update UI with session reset but keep hardware counter intact
            updateSessionUI(sessionSteps, 0f, 0f, 0);
            
            // Make sure hardware counter stays visible with correct value
            updateHardwareStepCount(totalSteps);
            
            // Update button state
            updateActionButtonState();
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };
    
    // Broadcast receiver for step updates
    private BroadcastReceiver stepUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int sessionSteps = intent.getIntExtra("steps", 0);
            int totalSteps = intent.getIntExtra("totalSteps", 0);
            float calories = intent.getFloatExtra("calories", 0f);
            float distance = intent.getFloatExtra("distance", 0f);
            long time = intent.getLongExtra("time", 0);
            
            // Update session UI without affecting hardware counter
            updateSessionUI(sessionSteps, calories, distance, time);
            
            // Update hardware counter independently
            updateHardwareStepCount(totalSteps);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_counter);
        
        initViews();
        setupButtonListeners();
        setupBottomNavigation();
        
        // Check and request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
        }
        
        // Register broadcast receiver
        registerReceiver(stepUpdateReceiver, new IntentFilter("STEP_COUNTER_UPDATE"));
        
        // Initialize with hardware step count immediately - temporarily bind to get step count
        Intent intent = new Intent(this, StepCounterService.class);
        startService(intent);
        bindService(intent, initialServiceConnection, Context.BIND_AUTO_CREATE);
    }
    
    private void initViews() {
        // Step count views
        stepCountTextView = findViewById(R.id.step_count);
        stepsLabelTextView = findViewById(R.id.steps_label);
        stepsProgressBar = findViewById(R.id.steps_progress);
        goalTextView = findViewById(R.id.goal_text);
        
        // Daily progress views
        dailyStepsTextView = findViewById(R.id.daily_steps_value);
        dailyGoalTextView = findViewById(R.id.daily_goal_value);
        dailyProgressBar = findViewById(R.id.daily_progress);
        
        // Control buttons
        actionButton = findViewById(R.id.start_pause_button);
        resetStepsButton = findViewById(R.id.reset_steps_button);
        
        // Metrics views
        caloriesValueTextView = findViewById(R.id.calories_value);
        distanceValueTextView = findViewById(R.id.distance_value);
        timeValueTextView = findViewById(R.id.time_value);
        
        // Initialize progress bars with fixed goal of 10,000 steps
        stepsProgressBar.setMax(10000);
        goalTextView.setText("Goal: 10,000 steps");
        
        // Initialize session progress with hidden goal
        dailyProgressBar.setMax(10000);
        dailyGoalTextView.setText(""); // Hide goal number
        
        // Update label to indicate these are total steps
        stepsLabelTextView.setText("total steps (hardware)");
    }
    
    private void setupButtonListeners() {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bound) {
                    // Start the service and bind to it
                    bindStepCounterService();
                    actionButton.setText("Stop");
                    Toast.makeText(StepsCounterActivity.this, 
                                  "Step counting started", Toast.LENGTH_SHORT).show();
                } else {
                    // Stop counting but don't reset - pause the session to continue later
                    if (stepCounterService != null) {
                        stepCounterService.pauseSession();
                    }
                    
                    // Unbind but don't stop service completely
                    unbindService(serviceConnection);
                    bound = false;
                    
                    // Update button text
                    actionButton.setText("Start");
                    Toast.makeText(StepsCounterActivity.this, 
                                  "Step counting stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        resetStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bound) {
                    // Show confirmation dialog
                    new AlertDialog.Builder(StepsCounterActivity.this)
                        .setTitle("Reset Session Counter")
                        .setMessage("Are you sure you want to reset your current session count to zero?")
                        .setPositiveButton("Reset", (dialog, which) -> {
                            // Only reset the session steps, not the hardware steps
                            stepCounterService.resetSessionSteps();
                            
                            // Reset UI elements for session stats
                            caloriesValueTextView.setText("0.0 kcal");
                            distanceValueTextView.setText("0.00 m");
                            timeValueTextView.setText("00:00:00");
                            
                            Toast.makeText(StepsCounterActivity.this, 
                                        "Session reset", Toast.LENGTH_SHORT).show();
                            
                            // Reset button to Start
                            actionButton.setText("Start");
                            
                            // Unbind and stop the service
                            unbindService(serviceConnection);
                            bound = false;
                            stopService(new Intent(StepsCounterActivity.this, StepCounterService.class));
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                } else {
                    // Reset the UI even if not bound
                    dailyStepsTextView.setText("0");
                    caloriesValueTextView.setText("0.0 kcal");
                    distanceValueTextView.setText("0.00 m");
                    timeValueTextView.setText("00:00:00");
                    dailyProgressBar.setProgress(0);
                    
                    // Reset button to Start
                    actionButton.setText("Start");
                    
                    // Stop the service if it's running in the background
                    stopService(new Intent(StepsCounterActivity.this, StepCounterService.class));
                    
                    Toast.makeText(StepsCounterActivity.this, 
                                  "Session reset", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    private void updateUI(int totalSteps, int sessionSteps, int dailySteps, int dailyGoal, float calories, float distance, long elapsedTime) {
        // Fixed goal value of 10,000 steps
        final int fixedGoal = 10000;
        
        // Update total steps (main counter)
        stepCountTextView.setText(String.format(Locale.getDefault(), "%,d", totalSteps));
        
        // Update hardware progress - shows progress toward 10,000 goal
        // but visually caps at 100% when above goal
        int hardwareProgressValue;
        if (totalSteps <= fixedGoal) {
            hardwareProgressValue = totalSteps;
            goalTextView.setText(String.format(Locale.getDefault(), "Goal: 10,000 steps"));
        } else {
            hardwareProgressValue = fixedGoal; // Visual cap at 100%
            // Update the goal text to show actual steps vs goal
            goalTextView.setText(String.format(Locale.getDefault(), 
                "Goal: 10,000 steps (Achieved: %,d)", totalSteps));
        }
        stepsProgressBar.setProgress(hardwareProgressValue);
        
        // Update session steps (session progress card)
        dailyStepsTextView.setText(String.format(Locale.getDefault(), "%,d", sessionSteps));
        dailyGoalTextView.setText(String.format(Locale.getDefault(), "%,d", fixedGoal));
        
        // Update session progress - show progress toward goal
        // but visually caps at 100% when above goal
        int sessionProgressValue;
        if (sessionSteps <= fixedGoal) {
            sessionProgressValue = sessionSteps;
        } else {
            sessionProgressValue = fixedGoal; // Visual cap at 100%
            // Update the session step text to indicate exceeding goal
            dailyStepsTextView.setText(String.format(Locale.getDefault(), "%,d", sessionSteps));
        }
        
        // Make sure the progress bar is properly adjusted for goalPercentage calculation
        dailyProgressBar.setMax(fixedGoal);
        dailyProgressBar.setProgress(sessionProgressValue);
        
        // Update goal percentage for session
        int goalPercentage = (int)(sessionSteps * 100 / fixedGoal);
        
        // Change progress bar color based on goal progress
        if (goalPercentage >= 100) {
            dailyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Green
        } else if (goalPercentage >= 50) {
            dailyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF9800"))); // Orange
        } else {
            dailyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#03A9F4"))); // Blue
        }
        
        // Format calories to 1 decimal place (based on session steps)
        DecimalFormat caloriesFormat = new DecimalFormat("#,##0.0");
        caloriesValueTextView.setText(caloriesFormat.format(calories) + " kcal");
        
        // Format distance - if less than 1000m show in meters, else in km (based on session steps)
        DecimalFormat distanceFormat = new DecimalFormat("#,##0.00");
        if (distance < 1000) {
            distanceValueTextView.setText(distanceFormat.format(distance) + " m");
        } else {
            distanceValueTextView.setText(distanceFormat.format(distance / 1000) + " km");
        }
        
        // Format time as HH:MM:SS (for the current session only)
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;
        
        timeValueTextView.setText(String.format(Locale.getDefault(), 
                                                "%02d:%02d:%02d", 
                                                hours, minutes, seconds));
    }
    
    private void updateActionButtonState() {
        if (bound && stepCounterService != null) {
            // If service is bound, set the button to "Stop"
            actionButton.setText("Stop");
        } else {
            // If not bound, always show "Start" regardless of paused state
            actionButton.setText("Start");
        }
    }
    
    private void bindStepCounterService() {
        // Get the current value of the hardware counter before resetting session
        String currentHardwareSteps = stepCountTextView.getText().toString();
        
        // Reset only the session stats UI
        dailyStepsTextView.setText("0");
        caloriesValueTextView.setText("0.0 kcal");
        distanceValueTextView.setText("0.00 m");
        timeValueTextView.setText("00:00:00");
        dailyProgressBar.setProgress(0);
        
        // Make sure hardware counter stays the same
        stepCountTextView.setText(currentHardwareSteps);
        
        // Start the service
        Intent intent = new Intent(this, StepCounterService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // Don't automatically bind on start - wait for user to press Start
        updateActionButtonState();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Check if service is running and update UI
        if (bound && stepCounterService != null) {
            // Update hardware counter
            updateHardwareStepCount(stepCounterService.getTotalSteps());
            
            // Update session UI
            updateSessionUI(
                stepCounterService.getCurrentSteps(),
                stepCounterService.getCaloriesBurned(),
                stepCounterService.getDistanceTraveled(),
                System.currentTimeMillis() - stepCounterService.getStartTime()
            );
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(serviceConnection);
            bound = false;
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Unbind from service if bound
        if (bound) {
            try {
                unbindService(serviceConnection);
                bound = false;
            } catch (Exception e) {
                Log.e(TAG, "Error unbinding service", e);
            }
        }
        
        // Unregister receiver
        try {
            unregisterReceiver(stepUpdateReceiver);
        } catch (Exception e) {
            Log.e(TAG, "Error unregistering receiver", e);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, 
                                          @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission granted for activity recognition", 
                            Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission required for step counting", 
                              Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        
        // Select the steps tab (position 0)
        bottomNavigation.getMenu().getItem(0).setChecked(true);
        
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
                    case 0: // Steps Counter (current)
                        return true;
                    case 1: // Workout Plan
                        startActivity(new Intent(StepsCounterActivity.this, WorkoutPlanActivity.class));
                        finish();
                        return true;
                    case 2: // Progress
                        startActivity(new Intent(StepsCounterActivity.this, ProgressActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
    
    // One-time service connection just to get the initial hardware step count
    private ServiceConnection initialServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepCounterService.StepBinder binder = (StepCounterService.StepBinder) service;
            StepCounterService initialService = binder.getService();
            
            // Update only the total steps (hardware counter)
            int totalHardwareSteps = initialService.getTotalSteps();
            updateHardwareStepCount(totalHardwareSteps);
            
            // Unbind immediately after getting the step count
            unbindService(this);
            stopService(new Intent(StepsCounterActivity.this, StepCounterService.class));
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Nothing needed here
        }
    };
    
    // Update only the hardware step count
    private void updateHardwareStepCount(int totalSteps) {
        // Fixed goal value of 10,000 steps
        final int fixedGoal = 10000;
        
        // Update total steps (main counter)
        stepCountTextView.setText(String.format(Locale.getDefault(), "%,d", totalSteps));
        
        // Update hardware progress - shows progress toward 10,000 goal
        // but visually caps at 100% when above goal
        int hardwareProgressValue;
        if (totalSteps <= fixedGoal) {
            hardwareProgressValue = totalSteps;
            goalTextView.setText(String.format(Locale.getDefault(), "Goal: 10,000 steps"));
        } else {
            hardwareProgressValue = fixedGoal; // Visual cap at 100%
            // Update the goal text to show actual steps vs goal
            goalTextView.setText(String.format(Locale.getDefault(), 
                "Goal: 10,000 steps (Achieved: %,d)", totalSteps));
        }
        stepsProgressBar.setProgress(hardwareProgressValue);
    }
    
    // Update only the session part of the UI
    private void updateSessionUI(int sessionSteps, float calories, float distance, long elapsedTime) {
        // Fixed goal value of 10,000 steps (hidden from UI but used for progress bar)
        final int fixedGoal = 10000;
        
        // Update session steps (session progress card) - only show current step count, not goal
        dailyStepsTextView.setText(String.format(Locale.getDefault(), "%,d", sessionSteps));
        // Hide the goal text by setting it to empty
        dailyGoalTextView.setText("");
        
        // Update session progress - show progress toward goal
        // but visually caps at 100% when above goal
        int sessionProgressValue;
        if (sessionSteps <= fixedGoal) {
            sessionProgressValue = sessionSteps;
        } else {
            sessionProgressValue = fixedGoal; // Visual cap at 100%
        }
        
        // Make sure the progress bar is properly adjusted for goalPercentage calculation
        dailyProgressBar.setMax(fixedGoal);
        dailyProgressBar.setProgress(sessionProgressValue);
        
        // Update goal percentage for session
        int goalPercentage = (int)(sessionSteps * 100 / fixedGoal);
        
        // Change progress bar color based on goal progress
        if (goalPercentage >= 100) {
            dailyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Green
        } else if (goalPercentage >= 50) {
            dailyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FF9800"))); // Orange
        } else {
            dailyProgressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#03A9F4"))); // Blue
        }
        
        // Format calories to 1 decimal place (based on session steps)
        DecimalFormat caloriesFormat = new DecimalFormat("#,##0.0");
        caloriesValueTextView.setText(caloriesFormat.format(calories) + " kcal");
        
        // Format distance - if less than 1000m show in meters, else in km (based on session steps)
        DecimalFormat distanceFormat = new DecimalFormat("#,##0.00");
        if (distance < 1000) {
            distanceValueTextView.setText(distanceFormat.format(distance) + " m");
        } else {
            distanceValueTextView.setText(distanceFormat.format(distance / 1000) + " km");
        }
        
        // Format time as HH:MM:SS (for the current session only)
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;
        
        timeValueTextView.setText(String.format(Locale.getDefault(), 
                                               "%02d:%02d:%02d", 
                                               hours, minutes, seconds));
    }
} 