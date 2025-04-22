package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.flextrack_ianation.models.Workout;
import com.example.flextrack_ianation.models.WorkoutPlan;
import com.example.flextrack_ianation.services.WorkoutPlanGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.flextrack_ianation.utils.NavigationHelper;

public class WorkoutPlanActivity extends AppCompatActivity {
    private static final String TAG = "WorkoutPlanActivity";
    
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    
    private TextView weekTitleTextView, programTypeTextView;
    private LinearLayout workoutCardsContainer;
    private Button generatePlanButton, nextWeekButton, previousWeekButton;
    private LinearLayout weekIndicator;
    private int currentDay = 0;
    
    private WorkoutPlan currentPlan;
    private int currentWeek = 1;
    private int totalWeeks = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_plan);
        
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        initViews();
        setupButtons();
        setupBottomNavigation();
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loadUserHealthProfile(currentUser.getUid());
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void initViews() {
        // Week and Program information
        weekTitleTextView = findViewById(R.id.week_title_text);
        programTypeTextView = findViewById(R.id.program_type_text);
        
        // Use a container for dynamic workout cards
        workoutCardsContainer = findViewById(R.id.workout_cards_container);
        
        // Buttons
        generatePlanButton = findViewById(R.id.generate_plan_button);
        nextWeekButton = findViewById(R.id.next_week_button);
        previousWeekButton = findViewById(R.id.previous_week_button);
        
        // Week indicator
        weekIndicator = findViewById(R.id.weekIndicator);
    }
    
    private void setupButtons() {
        generatePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    loadUserHealthProfile(currentUser.getUid());
                }
            }
        });
        
        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWeek < totalWeeks) {
                    currentWeek++;
                    updateWeekDisplay();
                }
            }
        });
        
        previousWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWeek > 1) {
                    currentWeek--;
                    updateWeekDisplay();
                }
            }
        });
    }
    
    private void updateWeekDisplay() {
        weekTitleTextView.setText("Week " + currentWeek + " of " + totalWeeks);
        
        // Update button states
        previousWeekButton.setEnabled(currentWeek > 1);
        nextWeekButton.setEnabled(currentWeek < totalWeeks);
        
        // Update workout plan details for the current week
        if (currentPlan != null) {
            updateWorkoutCards();
        }
    }
    
    private void loadUserHealthProfile(String userId) {
        mDatabase.child("users").child(userId).child("healthProfile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> profileData = (Map<String, Object>) dataSnapshot.getValue();
                            generateWorkoutPlan(profileData);
                        } else {
                            Toast.makeText(WorkoutPlanActivity.this, "No health profile found", 
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(WorkoutPlanActivity.this, "Failed to load profile: " + 
                                databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private void generateWorkoutPlan(Map<String, Object> profileData) {
        try {
            // Extract profile data with null checks
            if (!profileData.containsKey("bmi") || !profileData.containsKey("fitnessGoal") ||
                !profileData.containsKey("fitnessLevel") || !profileData.containsKey("focusArea") ||
                !profileData.containsKey("workoutEnvironment") || !profileData.containsKey("workoutTimePerDay") ||
                !profileData.containsKey("daysPerWeek")) {
                Toast.makeText(this, "Missing required profile data", Toast.LENGTH_SHORT).show();
                return;
            }

            double bmi = Double.parseDouble(profileData.get("bmi").toString());
            String bmiCategory = getBmiCategory(bmi);
            String fitnessGoal = profileData.get("fitnessGoal").toString();
            String fitnessLevel = profileData.get("fitnessLevel").toString();
            String focusArea = profileData.get("focusArea").toString();
            String workoutEnvironment = profileData.get("workoutEnvironment").toString();
            
            // Parse integers with error handling
            int workoutTimePerDay;
            int daysPerWeek;
            try {
                workoutTimePerDay = Integer.parseInt(profileData.get("workoutTimePerDay").toString());
                daysPerWeek = Integer.parseInt(profileData.get("daysPerWeek").toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid workout time or days per week", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Validate values
            if (workoutTimePerDay <= 0 || daysPerWeek <= 0 || daysPerWeek > 7) {
                Toast.makeText(this, "Invalid workout parameters", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // Log the parameters being passed to the workout generator
            Log.d(TAG, "Generating workout plan with parameters:");
            Log.d(TAG, "  BMI Category: " + bmiCategory);
            Log.d(TAG, "  Fitness Goal: " + fitnessGoal);
            Log.d(TAG, "  Fitness Level: " + fitnessLevel);
            Log.d(TAG, "  Focus Area: " + focusArea);
            Log.d(TAG, "  Environment: " + workoutEnvironment);
            Log.d(TAG, "  Workout Time: " + workoutTimePerDay + " minutes");
            Log.d(TAG, "  Days Per Week: " + daysPerWeek);
            
            // Generate workout plan
            try {
                currentPlan = WorkoutPlanGenerator.generateWorkoutPlan(
                        bmiCategory,
                        fitnessGoal,
                        fitnessLevel,
                        focusArea,
                        workoutEnvironment,
                        workoutTimePerDay,
                        daysPerWeek
                );
                
                if (currentPlan != null) {
                    List<Workout> workouts = currentPlan.getWorkouts();
                    Log.d(TAG, "Workout plan generated. Workouts: " + (workouts != null ? workouts.size() : 0));
                    
                    if (workouts == null || workouts.isEmpty()) {
                        Log.e(TAG, "Workout plan has no workouts, generating default workouts");
                        Toast.makeText(this, "No workouts were generated. Adding default workouts.", 
                                Toast.LENGTH_LONG).show();
                        
                        // Initialize empty workout list if null
                        if (workouts == null) {
                            currentPlan.setWorkouts(new ArrayList<>());
                        }
                        
                        // Add at least one default workout so UI isn't empty
                        addDefaultWorkout(currentPlan, fitnessGoal, fitnessLevel, focusArea, workoutEnvironment);
                    }
                    
                    // Display plan in UI
                    displayWorkoutPlan(currentPlan);
                    
                    // Save plan to Firebase
                    saveWorkoutPlan(currentPlan);
                } else {
                    Log.e(TAG, "Failed to generate workout plan - plan is null");
                    Toast.makeText(this, "Failed to generate workout plan, creating a default plan", Toast.LENGTH_SHORT).show();
                    
                    // Create a default plan if generator fails
                    createDefaultPlan(bmiCategory, fitnessGoal, fitnessLevel, focusArea, 
                            workoutEnvironment, workoutTimePerDay, daysPerWeek);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in workout generator", e);
                Toast.makeText(this, "Error in workout generator: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                
                // Create a default plan if generator fails with exception
                createDefaultPlan(bmiCategory, fitnessGoal, fitnessLevel, focusArea, 
                        workoutEnvironment, workoutTimePerDay, daysPerWeek);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error generating workout plan", e);
            Toast.makeText(this, "Error generating workout plan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void createDefaultPlan(String bmiCategory, String fitnessGoal, String fitnessLevel, 
                              String focusArea, String environment, int workoutTimePerDay, int daysPerWeek) {
        try {
            // Create a basic workout plan
            currentPlan = new WorkoutPlan(
                    "Default Fitness Plan",
                    "A basic workout plan with essential exercises.",
                    bmiCategory,
                    fitnessGoal,
                    fitnessLevel,
                    4, // 4 weeks default
                    Math.min(3, daysPerWeek), // Max 3 days per week
                    focusArea,
                    environment
            );
            
            // Add default workouts
            for (int i = 0; i < Math.min(3, daysPerWeek); i++) {
                addDefaultWorkout(currentPlan, fitnessGoal, fitnessLevel, focusArea, environment);
            }
            
            // Display and save
            displayWorkoutPlan(currentPlan);
            saveWorkoutPlan(currentPlan);
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating default plan", e);
            Toast.makeText(this, "Failed to create default plan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void addDefaultWorkout(WorkoutPlan plan, String fitnessGoal, String fitnessLevel, 
                             String focusArea, String environment) {
        // Create a basic workout
        Workout workout = new Workout(
                "Basic " + focusArea + " Workout",
                "A simple workout focusing on " + focusArea.toLowerCase(),
                focusArea,
                30, // 30 minutes
                fitnessLevel,
                fitnessGoal.contains("Muscle") ? "Strength" : "Cardio",
                focusArea,
                environment
        );
        
        // Add some basic exercises
        com.example.flextrack_ianation.models.Exercise exercise1 = new com.example.flextrack_ianation.models.Exercise(
                "Bodyweight Squats",
                "Stand with feet shoulder-width apart, lower your body as if sitting in a chair, then return to standing",
                "Legs, Glutes",
                3, 15, 60,
                "None",
                fitnessLevel,
                "Strength"
        );
        
        com.example.flextrack_ianation.models.Exercise exercise2 = new com.example.flextrack_ianation.models.Exercise(
                "Push-ups",
                "Start in plank position, lower chest to ground, then push back up",
                "Chest, Shoulders, Triceps",
                3, 10, 60,
                "None",
                fitnessLevel,
                "Strength"
        );
        
        com.example.flextrack_ianation.models.Exercise exercise3 = new com.example.flextrack_ianation.models.Exercise(
                "Jumping Jacks",
                "Stand with feet together, jump while spreading legs and raising arms overhead",
                "Full Body",
                3, 30, 30,
                "None",
                fitnessLevel,
                "Cardio"
        );
        
        // Add exercises to workout
        workout.addExercise(exercise1);
        workout.addExercise(exercise2);
        workout.addExercise(exercise3);
        
        // Add workout to plan
        plan.addWorkout(workout);
    }
    
    private void displayWorkoutPlan(WorkoutPlan plan) {
        if (plan == null) {
            Log.e(TAG, "displayWorkoutPlan: plan is null");
            Toast.makeText(this, "Error: Unable to display workout plan", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Log plan details for debugging
        List<Workout> workouts = plan.getWorkouts();
        Log.d(TAG, "Displaying workout plan: " + plan.getName());
        Log.d(TAG, "Plan contains " + (workouts != null ? workouts.size() : 0) + " workouts");
        Log.d(TAG, "Fitness goal: " + plan.getFitnessGoal() + ", Level: " + plan.getFitnessLevel());
        
        // Check for null or empty workout list
        if (workouts == null || workouts.isEmpty()) {
            Log.e(TAG, "Plan has no workouts to display");
            Toast.makeText(this, "No workouts found in plan. Generating default workouts.", Toast.LENGTH_LONG).show();
            // Ensure we have at least an empty list
            if (workouts == null) {
                plan.setWorkouts(new ArrayList<>());
            }
        } else {
            // Log each workout for debugging
            for (int i = 0; i < workouts.size(); i++) {
                Workout workout = workouts.get(i);
                if (workout != null) {
                    Log.d(TAG, "Workout " + i + ": " + workout.getName() + 
                          ", Type: " + workout.getWorkoutType() + 
                          ", Exercises: " + (workout.getExercises() != null ? workout.getExercises().size() : 0));
                } else {
                    Log.e(TAG, "Workout at index " + i + " is null!");
                }
            }
        }
        
        // Reset to the first week when displaying a new plan
        currentWeek = 1;
        
        // Set total weeks based on plan duration
        totalWeeks = plan.getDurationWeeks();
        
        // Update program type based on fitness goal
        String programType = plan.getFitnessGoal();
        if (programType.equalsIgnoreCase("Weight Loss") || 
            programType.equalsIgnoreCase("Lose Weight")) {
            programType = "Personalized Weight Loss Plan";
        } else if (programType.equalsIgnoreCase("Muscle Gain") || 
                  programType.equalsIgnoreCase("Build Muscle")) {
            programType = "Personalized Muscle Building Plan";
        } else if (programType.contains("Cardio") || 
                  programType.contains("Endurance")) {
            programType = "Personalized Cardio Endurance Plan";
        } else if (programType.contains("Flexibility")) {
            programType = "Personalized Flexibility Plan";
        } else if (programType.contains("Athletic")) {
            programType = "Personalized Athletic Performance Plan";
        } else {
            programType = "Your Personalized " + plan.getName();
        }
        
        programTypeTextView.setText(programType);
        
        // Show all workouts from the plan without week separation
        weekTitleTextView.setText("Your Personalized Workout Plan (" + plan.getWorkoutsPerWeek() + " workouts/week)");
        
        // Now call updateWorkoutCards to handle displaying workout cards and setting up click listeners
        updateWorkoutCards();
        
        // Hide week navigation buttons
        nextWeekButton.setVisibility(View.GONE);
        previousWeekButton.setVisibility(View.GONE);
        
        // Hide week indicator
        weekIndicator.setVisibility(View.GONE);
        
        // Show success message to user about personalization
        Toast.makeText(this, "Workout plan generated based on your personal health profile", Toast.LENGTH_LONG).show();
    }
    
    private void saveWorkoutPlan(WorkoutPlan plan) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String userId = currentUser.getUid();
        String planId = mDatabase.child("users").child(userId).child("workoutPlans").push().getKey();
        
        if (planId != null) {
            plan.setPlanId(planId);
            
            mDatabase.child("users").child(userId).child("workoutPlans").child(planId)
                    .setValue(plan)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(WorkoutPlanActivity.this, 
                                        "Workout plan saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(WorkoutPlanActivity.this, 
                                        "Failed to save workout plan", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            
            // Set this as the active plan
            Map<String, Object> activeUpdate = new HashMap<>();
            activeUpdate.put("activePlanId", planId);
            mDatabase.child("users").child(userId).updateChildren(activeUpdate);
        }
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

    private void setupWeekIndicator() {
        weekIndicator.removeAllViews();

        // Get the number of workout days from the plan
        int daysPerWeek = currentPlan.getWorkoutsPerWeek();

        for (int i = 0; i < daysPerWeek; i++) {
            TextView dayIndicator = new TextView(this);
            dayIndicator.setText(String.valueOf(i + 1));
            dayIndicator.setTextSize(16);
            dayIndicator.setPadding(16, 8, 16, 8);
            dayIndicator.setBackgroundResource(R.drawable.bg_day_indicator);
            dayIndicator.setTextColor(getResources().getColor(R.color.white));
            dayIndicator.setGravity(Gravity.CENTER);

            // Set the first day as selected by default
            if (i == 0) {
                dayIndicator.setBackgroundResource(R.drawable.bg_day_indicator_selected);
            }

            final int dayIndex = i;
            dayIndicator.setOnClickListener(v -> {
                // Update selected day
                for (int j = 0; j < weekIndicator.getChildCount(); j++) {
                    TextView indicator = (TextView) weekIndicator.getChildAt(j);
                    indicator.setBackgroundResource(R.drawable.bg_day_indicator);
                }
                dayIndicator.setBackgroundResource(R.drawable.bg_day_indicator_selected);
                currentDay = dayIndex;
                updateWorkoutCards();
            });

            weekIndicator.addView(dayIndicator);
        }
    }
    
    private void updateWorkoutCards() {
        if (currentPlan == null) {
            Log.e(TAG, "updateWorkoutCards: currentPlan is null");
            return;
        }
        
        // Clear the container first
        workoutCardsContainer.removeAllViews();
        
        // Display all workouts
        List<Workout> workouts = currentPlan.getWorkouts();
        Log.d(TAG, "Updating workout cards. Total workouts: " + (workouts != null ? workouts.size() : 0));
        
        if (workouts != null && !workouts.isEmpty()) {
            // Add a header explaining personalization
            TextView personalizationHeader = new TextView(this);
            personalizationHeader.setText("These workouts are customized based on your health profile, fitness level, goals, and selected focus area.");
            personalizationHeader.setTextSize(14);
            personalizationHeader.setPadding(16, 8, 16, 24);
            personalizationHeader.setTextColor(getResources().getColor(R.color.primary_text));
            workoutCardsContainer.addView(personalizationHeader);
            
            for (int i = 0; i < workouts.size(); i++) {
                Workout workout = workouts.get(i);
                
                if (workout == null) {
                    Log.e(TAG, "Skipping null workout at index " + i);
                    continue;
                }
                
                // Inflate a workout card from layout
                View cardView = getLayoutInflater().inflate(R.layout.workout_day_card, workoutCardsContainer, false);
                
                // Find views in the card
                CardView card = cardView.findViewById(R.id.workout_card);
                TextView dayTitle = cardView.findViewById(R.id.day_title);
                TextView workoutType = cardView.findViewById(R.id.workout_type);
                TextView workoutName = cardView.findViewById(R.id.workout_name);
                TextView exerciseCount = cardView.findViewById(R.id.exercise_count);
                TextView workoutDuration = cardView.findViewById(R.id.workout_duration);
                
                // Determine which day and week this workout is for
                int workoutWeek = (i / currentPlan.getWorkoutsPerWeek()) + 1;
                int workoutDay = (i % currentPlan.getWorkoutsPerWeek()) + 1;
                
                // Set workout data
                dayTitle.setText("Week " + workoutWeek + ", Day " + workoutDay);
                
                // Use safe getters with null checks
                String type = workout.getWorkoutType() != null ? workout.getWorkoutType() : "General";
                String focus = workout.getFocusArea() != null ? workout.getFocusArea() : "Full Body";
                workoutType.setText(type + " • " + focus);
                
                String name = workout.getName() != null ? workout.getName() : "Workout " + (i + 1);
                workoutName.setText(name);
                
                // Set exercise count
                int numExercises = workout.getExercises() != null ? workout.getExercises().size() : 0;
                exerciseCount.setText(numExercises + " exercises");
                
                // Set duration
                workoutDuration.setText("Duration: " + workout.getDurationMinutes() + " minutes");
                
                // Set click listener
                final Workout currentWorkout = workout;
                final int workoutIndex = i;
                
                // Enhanced click handling with logging
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Workout card clicked: " + currentWorkout.getName() + " (index: " + workoutIndex + ")");
                        showWorkoutDetails(currentWorkout);
                    }
                };
                
                // Apply the click listener to both the card and the entire card view for better response
                card.setOnClickListener(clickListener);
                cardView.setOnClickListener(clickListener);
                
                // Add card to container
                workoutCardsContainer.addView(cardView);
                
                // Verify card is clickable with more visible styling
                card.setClickable(true);
                card.setFocusable(true);
                
                // Add ripple effect and highlight for better visual feedback
                card.setForeground(getResources().getDrawable(android.R.drawable.list_selector_background));
                
                // Make the card elevation higher on touch to provide better feedback
                card.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case android.view.MotionEvent.ACTION_DOWN:
                            card.setCardElevation(12f); // Higher elevation when pressed
                            return false;
                        case android.view.MotionEvent.ACTION_UP:
                        case android.view.MotionEvent.ACTION_CANCEL:
                            card.setCardElevation(4f); // Back to normal when released
                            return false;
                    }
                    return false;
                });
                
                // Log successful card setup
                Log.d(TAG, "Added workout card for: " + workout.getName() + ", clickable: " + card.isClickable());
            }
        } else {
            Log.e(TAG, "No workouts found in the current plan");
            // Display a message to the user
            TextView noWorkoutsText = new TextView(this);
            noWorkoutsText.setText("No workouts found. Please try generating a new plan.");
            noWorkoutsText.setPadding(16, 32, 16, 16);
            noWorkoutsText.setGravity(Gravity.CENTER);
            noWorkoutsText.setTextSize(16);
            workoutCardsContainer.addView(noWorkoutsText);
        }
    }

    private void showWorkoutDetails(Workout workout) {
        if (workout == null) {
            Log.e(TAG, "Cannot show details for null workout");
            Toast.makeText(this, "Error: Workout data is missing", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Log.d(TAG, "Showing details for workout: " + workout.getName());
        
        // Verify workout has essential data
        if (workout.getName() == null || workout.getName().isEmpty()) {
            Log.e(TAG, "Workout name is empty or null");
            workout.setName("Untitled Workout");
        }
        
        // Ensure workout has at least empty exercise list to avoid null pointer exceptions
        if (workout.getExercises() == null) {
            Log.e(TAG, "Workout has null exercise list, initializing empty list");
            workout.setExercises(new ArrayList<>());
        }
        
        // Create intent and put the workout as extra
        Intent intent = new Intent(this, WorkoutDetailsActivity.class);
        intent.putExtra("workout", workout);
        
        try {
            Log.d(TAG, "Starting WorkoutDetailsActivity with workout: " + workout.getName() + 
                    ", exercises: " + workout.getExercises().size());
            startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to launch WorkoutDetailsActivity", e);
            Toast.makeText(this, "Error opening workout details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        
        // Select the workout tab (position 1)
        bottomNavigation.getMenu().getItem(1).setChecked(true);
        
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
                        startActivity(new Intent(WorkoutPlanActivity.this, StepsCounterActivity.class));
                        finish();
                        return true;
                    case 1: // Workout Plan (current)
                        return true;
                    case 2: // Progress
                        startActivity(new Intent(WorkoutPlanActivity.this, ProgressActivity.class));
                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }
} 