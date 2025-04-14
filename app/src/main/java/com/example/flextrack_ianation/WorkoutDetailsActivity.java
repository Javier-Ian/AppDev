package com.example.flextrack_ianation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.flextrack_ianation.models.Exercise;
import com.example.flextrack_ianation.models.Workout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WorkoutDetailsActivity extends AppCompatActivity {
    private TextView workoutTitle;
    private TextView workoutType;
    private TextView workoutDescription;
    private TextView workoutDuration;
    private TextView workoutDifficulty;
    private TextView workoutMuscleGroups;
    private LinearLayout exercisesContainer;
    private FloatingActionButton startWorkoutButton;
    
    private Workout currentWorkout;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        workoutTitle = findViewById(R.id.workout_title);
        workoutType = findViewById(R.id.workout_type);
        workoutDescription = findViewById(R.id.workout_description);
        workoutDuration = findViewById(R.id.workout_duration);
        workoutDifficulty = findViewById(R.id.workout_difficulty);
        workoutMuscleGroups = findViewById(R.id.workout_muscle_groups);
        exercisesContainer = findViewById(R.id.exercises_container);
        startWorkoutButton = findViewById(R.id.start_workout_fab);

        // Get workout from intent
        currentWorkout = (Workout) getIntent().getSerializableExtra("workout");
        if (currentWorkout != null) {
            displayWorkoutDetails(currentWorkout);
            displayExercises(currentWorkout);
            
            // Check if workout was completed
            boolean workoutCompleted = getIntent().getBooleanExtra("workoutCompleted", false);
            if (workoutCompleted) {
                currentWorkout.setCompleted(true);
                updateCompletionStatus();
            } else {
                // Set up click listener for the start workout button
                startWorkoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startWorkout();
                    }
                });
            }
        }
    }

    private void displayWorkoutDetails(Workout workout) {
        workoutTitle.setText(workout.getName());
        workoutType.setText(workout.getWorkoutType() + " • " + workout.getFocusArea());
        
        // Enhance description to emphasize personalization
        String personalizedDesc = workout.getDescription();
        if (!personalizedDesc.contains("personalized")) {
            personalizedDesc = "This workout is personalized based on your health profile and " + 
                workout.getFitnessLevel() + " fitness level. " + personalizedDesc;
        }
        workoutDescription.setText(personalizedDesc);
        
        workoutDuration.setText(workout.getDurationMinutes() + " minutes");
        workoutDifficulty.setText(workout.getDifficultyLevel());
        workoutMuscleGroups.setText(workout.getTargetMuscleGroups());
    }
    
    private void displayExercises(Workout workout) {
        if (workout.getExercises() == null || workout.getExercises().isEmpty()) {
            TextView noExercisesText = new TextView(this);
            noExercisesText.setText("No exercises found for this workout.");
            noExercisesText.setPadding(16, 16, 16, 16);
            exercisesContainer.addView(noExercisesText);
            return;
        }
        
        // Add a personalization header
        TextView personalizationHeader = new TextView(this);
        personalizationHeader.setText("These exercises are tailored for your " + workout.getFocusArea() + 
                " focus in a " + workout.getWorkoutEnvironment() + " environment.");
        personalizationHeader.setTextSize(14);
        personalizationHeader.setTextColor(getResources().getColor(R.color.primary));
        personalizationHeader.setPadding(16, 8, 16, 24);
        exercisesContainer.addView(personalizationHeader);
        
        // Add a header for exercises section
        TextView exercisesHeader = new TextView(this);
        exercisesHeader.setText("Exercises");
        exercisesHeader.setTextSize(18);
        exercisesHeader.setPadding(16, 8, 16, 8);
        exercisesContainer.addView(exercisesHeader);
        
        // Add each exercise as a card
        for (Exercise exercise : workout.getExercises()) {
            CardView exerciseCard = new CardView(this);
            exerciseCard.setRadius(8);
            exerciseCard.setCardElevation(4);
            exerciseCard.setUseCompatPadding(true);
            exerciseCard.setContentPadding(16, 16, 16, 16);
            
            LinearLayout cardContent = new LinearLayout(this);
            cardContent.setOrientation(LinearLayout.VERTICAL);
            
            // Exercise name
            TextView exerciseName = new TextView(this);
            exerciseName.setText(exercise.getName());
            exerciseName.setTextSize(16);
            exerciseName.setTextColor(getResources().getColor(R.color.primary_text));
            exerciseName.setTypeface(null, Typeface.BOLD);
            exerciseName.setPadding(0, 0, 0, 8);
            cardContent.addView(exerciseName);
            
            // Muscle group - add this to make it more obvious which muscle groups are targeted
            TextView muscleGroup = new TextView(this);
            muscleGroup.setText("Target: " + exercise.getMuscleGroup());
            muscleGroup.setTextSize(14);
            muscleGroup.setTextColor(getResources().getColor(R.color.primary));
            muscleGroup.setPadding(0, 0, 0, 8);
            cardContent.addView(muscleGroup);
            
            // Exercise description
            TextView exerciseDescription = new TextView(this);
            exerciseDescription.setText(exercise.getDescription());
            cardContent.addView(exerciseDescription);
            
            // Exercise details
            TextView exerciseDetails = new TextView(this);
            String details = "Sets: " + exercise.getSets() + 
                    " | Reps: " + exercise.getRepsPerSet() + 
                    " | Rest: " + exercise.getRestBetweenSets() + "s";
            exerciseDetails.setText(details);
            exerciseDetails.setPadding(0, 8, 0, 0);
            cardContent.addView(exerciseDetails);
            
            // Equipment needed
            if (exercise.getEquipmentNeeded() != null && !exercise.getEquipmentNeeded().equals("None")) {
                TextView equipmentText = new TextView(this);
                equipmentText.setText("Equipment: " + exercise.getEquipmentNeeded());
                equipmentText.setPadding(0, 8, 0, 0);
                cardContent.addView(equipmentText);
            }
            
            // Add difficulty level
            TextView difficultyText = new TextView(this);
            difficultyText.setText("Difficulty: " + exercise.getDifficultyLevel() + " • Type: " + exercise.getExerciseType());
            difficultyText.setPadding(0, 8, 0, 0);
            difficultyText.setTextSize(12);
            difficultyText.setTextColor(getResources().getColor(R.color.secondary_text));
            cardContent.addView(difficultyText);
            
            exerciseCard.addView(cardContent);
            exercisesContainer.addView(exerciseCard);
        }
        
        // Add a note about personalized progression
        TextView progressionNote = new TextView(this);
        progressionNote.setText("Note: This workout is part of your personalized progression plan. " +
                "As you improve, the exercises will adapt to your increasing fitness level.");
        progressionNote.setTextSize(14);
        progressionNote.setPadding(16, 24, 16, 16);
        progressionNote.setTextColor(getResources().getColor(R.color.secondary_text));
        progressionNote.setTypeface(null, Typeface.ITALIC);
        exercisesContainer.addView(progressionNote);
    }
    
    private void startWorkout() {
        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start Workout");
        builder.setMessage("Are you ready to start this workout?");
        builder.setPositiveButton("Let's Go!", (dialog, which) -> {
            // User confirmed, start workout countdown
            showCountdownDialog();
        });
        builder.setNegativeButton("Not Yet", (dialog, which) -> {
            // User canceled, do nothing
            dialog.dismiss();
        });
        builder.create().show();
    }
    
    private void showCountdownDialog() {
        // Create a dialog for the countdown
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View countdownView = getLayoutInflater().inflate(R.layout.dialog_countdown, null);
        TextView countdownText = countdownView.findViewById(R.id.countdown_text);
        builder.setView(countdownView);
        builder.setCancelable(false);
        
        AlertDialog dialog = builder.create();
        dialog.show();
        
        // Start a 3-second countdown
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update countdown text
                int secondsRemaining = (int) (millisUntilFinished / 1000) + 1;
                countdownText.setText(String.valueOf(secondsRemaining));
            }
            
            @Override
            public void onFinish() {
                // Dismiss the dialog
                dialog.dismiss();
                
                // Mark the workout as in progress
                beginWorkout();
            }
        }.start();
    }
    
    private void beginWorkout() {
        // Mark the workout as started in Firebase
        recordWorkoutStarted();
        
        // Start the workout progress activity to guide through exercises
        Intent intent = new Intent(this, WorkoutProgressActivity.class);
        intent.putExtra("workout", currentWorkout);
        startActivity(intent);
    }
    
    private void showWorkoutCompletionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Workout Completed?");
        builder.setMessage("Have you completed the workout?");
        builder.setPositiveButton("Yes, Completed!", (dialog, which) -> {
            // Mark the workout as completed
            markWorkoutCompleted();
        });
        builder.setNegativeButton("Not Yet", (dialog, which) -> {
            // User hasn't completed, do nothing
            dialog.dismiss();
        });
        builder.create().show();
    }
    
    private void recordWorkoutStarted() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null || currentWorkout == null) {
            return;
        }
        
        String userId = currentUser.getUid();
        String workoutId = currentWorkout.getWorkoutId();
        if (workoutId == null) {
            workoutId = "workout_" + System.currentTimeMillis(); // Generate an ID if not present
            currentWorkout.setWorkoutId(workoutId);
        }
        
        // Record workout start time
        Map<String, Object> workoutHistoryUpdate = new HashMap<>();
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        
        workoutHistoryUpdate.put("startTime", currentDateTime);
        workoutHistoryUpdate.put("workoutName", currentWorkout.getName());
        workoutHistoryUpdate.put("workoutType", currentWorkout.getWorkoutType());
        workoutHistoryUpdate.put("completed", false);
        
        // Save to user's workout history
        mDatabase.child("users").child(userId).child("workoutHistory").child(workoutId)
                .updateChildren(workoutHistoryUpdate);
    }
    
    private void markWorkoutCompleted() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null || currentWorkout == null) {
            return;
        }
        
        String userId = currentUser.getUid();
        String workoutId = currentWorkout.getWorkoutId();
        if (workoutId == null) {
            // This shouldn't happen if recordWorkoutStarted was called first
            workoutId = "workout_" + System.currentTimeMillis();
        }
        
        // Update workout completion status
        Map<String, Object> completionUpdate = new HashMap<>();
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        
        completionUpdate.put("completed", true);
        completionUpdate.put("completionTime", currentDateTime);
        completionUpdate.put("caloriesBurned", currentWorkout.getCaloriesBurnEstimate());
        
        // Save to user's workout history
        mDatabase.child("users").child(userId).child("workoutHistory").child(workoutId)
                .updateChildren(completionUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Show completion message
                            Toast.makeText(WorkoutDetailsActivity.this, 
                                    "Great job! Workout completed!", Toast.LENGTH_LONG).show();
                            
                            // Update local workout object
                            currentWorkout.setCompleted(true);
                            
                            // Update UI if needed
                            updateCompletionStatus();
                        } else {
                            Toast.makeText(WorkoutDetailsActivity.this, 
                                    "Failed to record workout completion", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    
    private void updateCompletionStatus() {
        // Update UI to reflect completed status
        // For example, change the start button to a "completed" state
        startWorkoutButton.setImageResource(android.R.drawable.ic_menu_revert);
        startWorkoutButton.setEnabled(false);
        
        // You could also add a "completed" banner to the workout details
        TextView completedBanner = new TextView(this);
        completedBanner.setText("WORKOUT COMPLETED!");
        completedBanner.setTextSize(18);
        completedBanner.setTextColor(getResources().getColor(R.color.primary));
        completedBanner.setPadding(16, 16, 16, 16);
        completedBanner.setBackgroundColor(getResources().getColor(R.color.white));
        completedBanner.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        
        // Add the banner at the top of exercises
        exercisesContainer.addView(completedBanner, 0);
    }
} 