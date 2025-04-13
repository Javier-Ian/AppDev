package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.flextrack_ianation.models.Workout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static boolean PERSISTENCE_ENABLED = false;
    
    private TextView userEmailTextView, nameTextView, ageTextView, genderTextView, 
                     heightTextView, weightTextView, bmiTextView, bmiCategoryTextView,
                     fitnessGoalTextView, fitnessLevelTextView;
    // Step counter views
    private TextView stepCountTextView, stepGoalTextView;
    private ProgressBar stepProgressBar;
    // Today's workout views
    private TextView workoutNameTextView, workoutExercisesCountTextView, workoutDurationTextView;
    private CardView homeWorkoutCard;
    // Before and after pics
    private ImageView beforeImageView, afterImageView;
    private CardView beforeAfterCard;
    
    private Button editProfileButton, signOutButton, viewWorkoutPlanButton;
    
    // For simulating step counter
    private int currentStepCount = 0;
    private final int STEP_GOAL = 10000;
    private Random random = new Random();
    private Workout todaysWorkout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase - only set persistence once per app lifecycle
        initializeFirebase();
        
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        initViews();
        setupButtons();
        
        // Make sure sections are visible
        if (homeWorkoutCard != null) {
            homeWorkoutCard.setVisibility(View.VISIBLE);
        }
        if (beforeAfterCard != null) {
            beforeAfterCard.setVisibility(View.VISIBLE);
        }
        if (stepProgressBar != null) {
            stepProgressBar.setVisibility(View.VISIBLE);
        }
        
        // Debug: Check if our new sections were initialized correctly
        if (stepCountTextView != null && homeWorkoutCard != null && beforeAfterCard != null) {
            Toast.makeText(this, "New sections initialized successfully", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Step counter, Workout card, and Before/After sections initialized");
            Log.d(TAG, "Step counter visibility: " + (stepCountTextView.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE"));
            Log.d(TAG, "Workout card visibility: " + (homeWorkoutCard.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE"));
            Log.d(TAG, "Before/After card visibility: " + (beforeAfterCard.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE"));
        } else {
            String errorMsg = "Failed to initialize sections: " + 
                (stepCountTextView == null ? "Step counter null, " : "") +
                (homeWorkoutCard == null ? "Workout card null, " : "") +
                (beforeAfterCard == null ? "Before/After card null" : "");
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            Log.e(TAG, errorMsg);
        }
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userEmailTextView.setText("Email: " + currentUser.getEmail());
            loadUserHealthProfile(currentUser.getUid());
            loadTodaysWorkout(currentUser.getUid());
            loadStepCount(currentUser.getUid());
        } else {
            // Not signed in, go back to sign in
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }
    }
    
    private void initViews() {
        userEmailTextView = findViewById(R.id.user_email);
        nameTextView = findViewById(R.id.name_text);
        ageTextView = findViewById(R.id.age_text);
        genderTextView = findViewById(R.id.gender_text);
        heightTextView = findViewById(R.id.height_text);
        weightTextView = findViewById(R.id.weight_text);
        bmiTextView = findViewById(R.id.bmi_text);
        bmiCategoryTextView = findViewById(R.id.bmi_category_text);
        fitnessGoalTextView = findViewById(R.id.fitness_goal_text);
        fitnessLevelTextView = findViewById(R.id.fitness_level_text);
        
        // Step counter views
        stepCountTextView = findViewById(R.id.step_count);
        stepGoalTextView = findViewById(R.id.step_goal);
        stepProgressBar = findViewById(R.id.step_progress);
        
        // Today's workout views
        homeWorkoutCard = findViewById(R.id.home_workout_card);
        workoutNameTextView = findViewById(R.id.workout_name);
        workoutExercisesCountTextView = findViewById(R.id.workout_exercises_count);
        workoutDurationTextView = findViewById(R.id.workout_duration);
        
        // Before and after pics
        beforeAfterCard = findViewById(R.id.before_after_card);
        beforeImageView = findViewById(R.id.before_image);
        afterImageView = findViewById(R.id.after_image);
        
        editProfileButton = findViewById(R.id.edit_profile_button);
        signOutButton = findViewById(R.id.sign_out_button);
        viewWorkoutPlanButton = findViewById(R.id.view_workout_plan_button);
        
        // Log that we're initializing views
        Log.d(TAG, "Initializing views...");
        Log.d(TAG, "Step Count TextView found: " + (stepCountTextView != null));
        Log.d(TAG, "Home Workout Card found: " + (homeWorkoutCard != null));
        Log.d(TAG, "Before/After Card found: " + (beforeAfterCard != null));
    }
    
    private void setupButtons() {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HealthProfileActivity.class));
            }
        });
        
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
        });
        
        viewWorkoutPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WorkoutPlanActivity.class));
            }
        });
        
        // Setup home workout card click
        if (homeWorkoutCard != null) {
            homeWorkoutCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (todaysWorkout != null) {
                        Intent intent = new Intent(MainActivity.this, WorkoutDetailsActivity.class);
                        intent.putExtra("workout", todaysWorkout);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(MainActivity.this, WorkoutPlanActivity.class));
                    }
                }
            });
        } else {
            Log.e(TAG, "homeWorkoutCard is null in setupButtons");
        }
        
        // Setup before and after pics card click
        if (beforeAfterCard != null) {
            beforeAfterCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Photo upload feature coming soon!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e(TAG, "beforeAfterCard is null in setupButtons");
        }
    }
    
    private void loadUserHealthProfile(String userId) {
        mDatabase.child("users").child(userId).child("healthProfile")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> profileData = (Map<String, Object>) dataSnapshot.getValue();
                            
                            // Display basic profile information
                            nameTextView.setText("Name: " + profileData.get("fullName"));
                            ageTextView.setText("Age: " + profileData.get("age"));
                            genderTextView.setText("Gender: " + profileData.get("gender"));
                            heightTextView.setText("Height: " + profileData.get("height") + " cm");
                            weightTextView.setText("Weight: " + profileData.get("weight") + " kg");
                            
                            // Format and display BMI with category
                            double bmi = Double.parseDouble(profileData.get("bmi").toString());
                            DecimalFormat df = new DecimalFormat("#.##");
                            bmiTextView.setText("BMI: " + df.format(bmi));
                            bmiCategoryTextView.setText("Category: " + getBmiCategory(bmi));
                            
                            // Show fitness details
                            fitnessGoalTextView.setText("Goal: " + profileData.get("fitnessGoal"));
                            fitnessLevelTextView.setText("Level: " + profileData.get("fitnessLevel"));
                        } else {
                            Toast.makeText(MainActivity.this, "No health profile found", 
                                    Toast.LENGTH_SHORT).show();
                            
                            // No profile, go to create one
                            startActivity(new Intent(MainActivity.this, HealthProfileActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Failed to load profile: " + 
                                databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private void loadTodaysWorkout(String userId) {
        // This would normally query the database for today's scheduled workout
        // For now, we'll just check if the user has an active workout plan 
        mDatabase.child("users").child(userId).child("activePlanId")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String activePlanId = dataSnapshot.getValue(String.class);
                            if (activePlanId != null) {
                                loadActiveWorkoutPlan(userId, activePlanId);
                            } else {
                                displayNoWorkout();
                            }
                        } else {
                            displayNoWorkout();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        displayNoWorkout();
                    }
                });
    }
    
    private void loadActiveWorkoutPlan(String userId, String planId) {
        mDatabase.child("users").child(userId).child("workoutPlans").child(planId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.child("workouts").exists()) {
                            // For demo purposes, just grab the first workout
                            DataSnapshot workoutsSnapshot = dataSnapshot.child("workouts");
                            if (workoutsSnapshot.getChildrenCount() > 0) {
                                DataSnapshot firstWorkout = workoutsSnapshot.getChildren().iterator().next();
                                todaysWorkout = firstWorkout.getValue(Workout.class);
                                if (todaysWorkout != null) {
                                    displayTodaysWorkout(todaysWorkout);
                                } else {
                                    displayNoWorkout();
                                }
                            } else {
                                displayNoWorkout();
                            }
                        } else {
                            displayNoWorkout();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        displayNoWorkout();
                    }
                });
    }
    
    private void displayTodaysWorkout(Workout workout) {
        workoutNameTextView.setText(workout.getName());
        int exerciseCount = workout.getExercises() != null ? workout.getExercises().size() : 0;
        workoutExercisesCountTextView.setText(exerciseCount + " exercises");
        workoutDurationTextView.setText("Duration: " + workout.getDurationMinutes() + " minutes");
    }
    
    private void displayNoWorkout() {
        workoutNameTextView.setText("No workout scheduled");
        workoutExercisesCountTextView.setText("0 exercises");
        workoutDurationTextView.setText("Duration: 0 minutes");
    }
    
    private void loadStepCount(String userId) {
        // In a real app, this would load step count data from the database or sensors
        // For this demo, we'll simulate step count with a random number between 2000-9000
        currentStepCount = 2000 + random.nextInt(7000);
        updateStepCountDisplay();
        
        // Simulate incremental step count updates
        simulateStepUpdates();
    }
    
    private void updateStepCountDisplay() {
        stepCountTextView.setText(String.valueOf(currentStepCount));
        stepGoalTextView.setText("Goal: " + STEP_GOAL + " steps");
        stepProgressBar.setMax(STEP_GOAL);
        stepProgressBar.setProgress(currentStepCount);
    }
    
    private void simulateStepUpdates() {
        // Simulate step count increasing over time
        new Thread(() -> {
            try {
                while (currentStepCount < STEP_GOAL) {
                    Thread.sleep(15000); // Every 15 seconds
                    int stepIncrease = 50 + random.nextInt(200);
                    currentStepCount = Math.min(currentStepCount + stepIncrease, STEP_GOAL);
                    
                    // Update UI on main thread
                    runOnUiThread(() -> updateStepCountDisplay());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 24.9) {
            return "Normal weight";
        } else if (bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
    
    private void initializeFirebase() {
        // Ensure persistence is only initialized once
        if (!PERSISTENCE_ENABLED) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                PERSISTENCE_ENABLED = true;
                Log.d(TAG, "Firebase Persistence enabled");
            } catch (Exception e) {
                Log.d(TAG, "Firebase Persistence already enabled or could not be enabled: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        // Check if the ScrollView is scrollable
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.post(() -> {
                Toast.makeText(MainActivity.this, "Remember to scroll down to see all sections", Toast.LENGTH_LONG).show();
            });
        }
    }
} 