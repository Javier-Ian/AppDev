package com.example.flextrack_ianation.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a workout session.
 */
public class Workout implements Serializable {
    private String workoutId;
    private String name;
    private String description;
    private String targetMuscleGroups;
    private int durationMinutes;
    private String difficultyLevel;
    private String workoutType; // "Full Body", "Upper Body", "Lower Body", "Core", etc.
    private List<Exercise> exercises;
    private String focusArea;
    private String workoutEnvironment; // "Home", "Gym", "Outdoor"
    private int caloriesBurnEstimate;
    private String createdAt;
    private boolean isCompleted;
    private int weekNumber; // Tracks which week this workout belongs to for progression
    private String fitnessLevel; // "Beginner", "Intermediate", "Advanced"

    // Empty constructor required for Firebase
    public Workout() {
        exercises = new ArrayList<>();
    }

    public Workout(String name, String description, String targetMuscleGroups,
                  int durationMinutes, String difficultyLevel, String workoutType,
                  String focusArea, String workoutEnvironment) {
        this.name = name;
        this.description = description;
        this.targetMuscleGroups = targetMuscleGroups;
        this.durationMinutes = durationMinutes;
        this.difficultyLevel = difficultyLevel;
        this.workoutType = workoutType;
        this.focusArea = focusArea;
        this.workoutEnvironment = workoutEnvironment;
        this.exercises = new ArrayList<>();
        this.isCompleted = false;
        this.weekNumber = 1; // Default to week 1
        this.fitnessLevel = "Beginner"; // Default to beginner
    }

    public void addExercise(Exercise exercise) {
        if (exercises == null) {
            exercises = new ArrayList<>();
        }
        exercises.add(exercise);
    }

    // Getters and setters
    public String getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(String workoutId) {
        this.workoutId = workoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetMuscleGroups() {
        return targetMuscleGroups;
    }

    public void setTargetMuscleGroups(String targetMuscleGroups) {
        this.targetMuscleGroups = targetMuscleGroups;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public void setFocusArea(String focusArea) {
        this.focusArea = focusArea;
    }

    public String getWorkoutEnvironment() {
        return workoutEnvironment;
    }

    public void setWorkoutEnvironment(String workoutEnvironment) {
        this.workoutEnvironment = workoutEnvironment;
    }

    public int getCaloriesBurnEstimate() {
        return caloriesBurnEstimate;
    }

    public void setCaloriesBurnEstimate(int caloriesBurnEstimate) {
        this.caloriesBurnEstimate = caloriesBurnEstimate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }
} 