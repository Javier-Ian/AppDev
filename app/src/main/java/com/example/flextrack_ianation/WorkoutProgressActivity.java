package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WorkoutProgressActivity extends AppCompatActivity {
    private static final String TAG = "WorkoutProgressActivity";
    
    // UI elements
    private TextView workoutNameText;
    private TextView exerciseNameText;
    private TextView exerciseDescriptionText;
    private TextView exerciseDetailsText;
    private TextView equipmentText;
    private TextView progressText;
    private ProgressBar exerciseProgressBar;
    private Button completeExerciseButton;
    private Button skipExerciseButton;
    private CardView restTimerCard;
    private TextView restTimerText;
    private Button finishWorkoutButton;
    
    // Workout data
    private Workout currentWorkout;
    private List<Exercise> exercises;
    private int currentExerciseIndex = 0;
    private int totalExercises = 0;
    private boolean isRestPeriod = false;
    private CountDownTimer restTimer;
    
    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_progress);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        // Initialize UI elements
        initViews();
        
        // Get workout from intent
        currentWorkout = (Workout) getIntent().getSerializableExtra("workout");
        if (currentWorkout != null && currentWorkout.getExercises() != null) {
            exercises = currentWorkout.getExercises();
            totalExercises = exercises.size();
            
            // Display workout name
            workoutNameText.setText(currentWorkout.getName());
            
            // Set up the progress bar
            exerciseProgressBar.setMax(totalExercises);
            exerciseProgressBar.setProgress(0);
            
            // Set up the progress text
            updateProgressText();
            
            // Load the first exercise
            if (totalExercises > 0) {
                loadExercise(0);
            } else {
                Toast.makeText(this, "No exercises found in this workout", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error loading workout", Toast.LENGTH_SHORT).show();
            finish();
        }
        
        // Set up button listeners
        setupButtonListeners();
    }
    
    private void initViews() {
        workoutNameText = findViewById(R.id.workout_name_text);
        exerciseNameText = findViewById(R.id.exercise_name_text);
        exerciseDescriptionText = findViewById(R.id.exercise_description_text);
        exerciseDetailsText = findViewById(R.id.exercise_details_text);
        equipmentText = findViewById(R.id.equipment_text);
        progressText = findViewById(R.id.progress_text);
        exerciseProgressBar = findViewById(R.id.exercise_progress_bar);
        completeExerciseButton = findViewById(R.id.complete_exercise_button);
        skipExerciseButton = findViewById(R.id.skip_exercise_button);
        restTimerCard = findViewById(R.id.rest_timer_card);
        restTimerText = findViewById(R.id.rest_timer_text);
        finishWorkoutButton = findViewById(R.id.finish_workout_button);
    }
    
    private void setupButtonListeners() {
        completeExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeCurrentExercise();
            }
        });
        
        skipExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipRestPeriod();
            }
        });
        
        finishWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmFinishWorkout();
            }
        });
    }
    
    private void loadExercise(int exerciseIndex) {
        if (exerciseIndex < 0 || exerciseIndex >= totalExercises) {
            completeWorkout();
            return;
        }
        
        // Get the current exercise
        Exercise exercise = exercises.get(exerciseIndex);
        
        // Set the exercise details
        exerciseNameText.setText(exercise.getName());
        exerciseDescriptionText.setText(exercise.getDescription());
        
        // Build the details text
        String details = String.format("Sets: %d | Reps: %d | Rest: %ds", 
                exercise.getSets(), 
                exercise.getRepsPerSet(), 
                exercise.getRestBetweenSets());
        exerciseDetailsText.setText(details);
        
        // Set equipment text
        if (exercise.getEquipmentNeeded() != null && !exercise.getEquipmentNeeded().equals("None")) {
            equipmentText.setText("Equipment: " + exercise.getEquipmentNeeded());
            equipmentText.setVisibility(View.VISIBLE);
        } else {
            equipmentText.setVisibility(View.GONE);
        }
        
        // Update the progress
        exerciseProgressBar.setProgress(exerciseIndex);
        updateProgressText();
        
        // Show exercise view, hide rest timer
        setExerciseViewVisible(true);
    }
    
    private void updateProgressText() {
        progressText.setText(String.format("Exercise %d of %d", currentExerciseIndex + 1, totalExercises));
    }
    
    private void completeCurrentExercise() {
        // If this is the last exercise, complete the workout
        if (currentExerciseIndex >= totalExercises - 1) {
            completeWorkout();
            return;
        }
        
        // Otherwise, start the rest timer if there's a rest period
        Exercise currentExercise = exercises.get(currentExerciseIndex);
        int restTime = currentExercise.getRestBetweenSets();
        
        if (restTime > 0) {
            startRestTimer(restTime);
        } else {
            // If no rest time, move to next exercise
            currentExerciseIndex++;
            loadExercise(currentExerciseIndex);
        }
    }
    
    private void startRestTimer(int seconds) {
        // Show the rest timer, hide exercise view
        setExerciseViewVisible(false);
        
        // Initialize and start the rest timer
        isRestPeriod = true;
        restTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                restTimerText.setText(String.format("Rest: %d seconds remaining", secondsRemaining));
            }
            
            @Override
            public void onFinish() {
                isRestPeriod = false;
                currentExerciseIndex++;
                loadExercise(currentExerciseIndex);
            }
        }.start();
    }
    
    private void skipRestPeriod() {
        if (isRestPeriod && restTimer != null) {
            restTimer.cancel();
            isRestPeriod = false;
            currentExerciseIndex++;
            loadExercise(currentExerciseIndex);
        }
    }
    
    private void setExerciseViewVisible(boolean isVisible) {
        int exerciseVisibility = isVisible ? View.VISIBLE : View.GONE;
        int restVisibility = isVisible ? View.GONE : View.VISIBLE;
        
        exerciseNameText.setVisibility(exerciseVisibility);
        exerciseDescriptionText.setVisibility(exerciseVisibility);
        exerciseDetailsText.setVisibility(exerciseVisibility);
        equipmentText.setVisibility(exerciseVisibility);
        completeExerciseButton.setVisibility(exerciseVisibility);
        
        restTimerCard.setVisibility(restVisibility);
        skipExerciseButton.setVisibility(restVisibility);
    }
    
    private void completeWorkout() {
        // Mark the workout as completed in Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentWorkout != null) {
            String userId = currentUser.getUid();
            String workoutId = currentWorkout.getWorkoutId();
            
            if (workoutId == null) {
                workoutId = "workout_" + System.currentTimeMillis();
                currentWorkout.setWorkoutId(workoutId);
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
                                showWorkoutCompletedDialog();
                            } else {
                                Toast.makeText(WorkoutProgressActivity.this, 
                                        "Failed to record workout completion", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    
    private void showWorkoutCompletedDialog() {
        // Hide all exercise content, show completed message
        findViewById(R.id.exercise_content).setVisibility(View.GONE);
        findViewById(R.id.workout_completed_view).setVisibility(View.VISIBLE);
        
        // Enable finish button
        finishWorkoutButton.setEnabled(true);
    }
    
    private void confirmFinishWorkout() {
        // Go back to workout details activity
        Intent intent = new Intent(this, WorkoutDetailsActivity.class);
        intent.putExtra("workout", currentWorkout);
        intent.putExtra("workoutCompleted", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        // Show confirmation dialog before exiting
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit Workout?");
        builder.setMessage("Are you sure you want to quit this workout? Your progress will not be saved.");
        builder.setPositiveButton("Quit", (dialog, which) -> {
            super.onBackPressed();
        });
        builder.setNegativeButton("Continue", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create().show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        // Cancel any active timer
        if (restTimer != null) {
            restTimer.cancel();
        }
    }
} 