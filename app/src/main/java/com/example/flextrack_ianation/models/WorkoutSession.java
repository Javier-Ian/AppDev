package com.example.flextrack_ianation.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Model class representing a completed workout session with progress metrics.
 */
public class WorkoutSession implements Serializable {
    private String workoutId;
    private String workoutName;
    private String workoutType;
    private long startTimeMillis;
    private int durationMinutes;
    private int caloriesBurned;
    private boolean completed;
    private String userId;
    private Map<String, Boolean> exerciseProgress; // Map of exerciseName to completion status

    // Empty constructor for Firebase
    public WorkoutSession() {
        // Required for Firebase
    }

    public WorkoutSession(String workoutId, String workoutName, String workoutType, 
                          long startTimeMillis, int durationMinutes, int caloriesBurned, 
                          boolean completed, String userId) {
        this.workoutId = workoutId;
        this.workoutName = workoutName;
        this.workoutType = workoutType;
        this.startTimeMillis = startTimeMillis;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.completed = completed;
        this.userId = userId;
        this.exerciseProgress = new HashMap<>();
    }

    // Convert to a Map for Firebase
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("workoutId", workoutId);
        map.put("workoutName", workoutName);
        map.put("workoutType", workoutType);
        map.put("startTimeMillis", startTimeMillis);
        map.put("durationMinutes", durationMinutes);
        map.put("caloriesBurned", caloriesBurned);
        map.put("completed", completed);
        map.put("userId", userId);
        map.put("exerciseProgress", exerciseProgress);
        return map;
    }

    // Getters and setters
    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Boolean> getExerciseProgress() {
        return exerciseProgress;
    }

    public void setExerciseProgress(Map<String, Boolean> exerciseProgress) {
        this.exerciseProgress = exerciseProgress;
    }

    // Add exercise to progress tracking
    public void addExercise(String exerciseName) {
        if (exerciseProgress == null) {
            exerciseProgress = new HashMap<>();
        }
        exerciseProgress.put(exerciseName, false);
    }

    // Mark an exercise as completed
    public void completeExercise(String exerciseName) {
        if (exerciseProgress != null && exerciseProgress.containsKey(exerciseName)) {
            exerciseProgress.put(exerciseName, true);
            
            // Check if all exercises are completed
            boolean allCompleted = true;
            for (Boolean status : exerciseProgress.values()) {
                if (!status) {
                    allCompleted = false;
                    break;
                }
            }
            
            // Update workout completion status
            if (allCompleted) {
                this.completed = true;
            }
        }
    }

    // Calculate completion percentage
    public int getCompletionPercentage() {
        if (exerciseProgress == null || exerciseProgress.isEmpty()) {
            return completed ? 100 : 0;
        }

        int completedCount = 0;
        for (Boolean status : exerciseProgress.values()) {
            if (status) {
                completedCount++;
            }
        }

        return (int) Math.round((double) completedCount / exerciseProgress.size() * 100);
    }

    // Get list of exercises from the exerciseProgress map
    public List<String> getExercises() {
        if (exerciseProgress == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(exerciseProgress.keySet());
    }
} 