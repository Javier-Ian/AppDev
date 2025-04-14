package com.example.flextrack_ianation.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a complete workout plan with multiple workouts.
 */
public class WorkoutPlan implements Serializable {
    private String planId;
    private String name;
    private String description;
    private String targetBmiCategory; // "Underweight", "Normal", "Overweight", "Obese"
    private String fitnessGoal; // "Lose Weight", "Build Muscle", "Stay Fit", etc.
    private String fitnessLevel; // "Beginner", "Intermediate", "Advanced"
    private int durationWeeks;
    private int workoutsPerWeek;
    private List<Workout> workouts;
    private String createdAt;
    private int estimatedCaloriesBurn;
    private String focusArea;
    private String workoutEnvironment;
    private boolean isActive;

    // Empty constructor required for Firebase
    public WorkoutPlan() {
        workouts = new ArrayList<>();
    }

    public WorkoutPlan(String name, String description, String targetBmiCategory,
                      String fitnessGoal, String fitnessLevel, int durationWeeks,
                      int workoutsPerWeek, String focusArea, String workoutEnvironment) {
        this.name = name;
        this.description = description;
        this.targetBmiCategory = targetBmiCategory;
        this.fitnessGoal = fitnessGoal;
        this.fitnessLevel = fitnessLevel;
        this.durationWeeks = durationWeeks;
        this.workoutsPerWeek = workoutsPerWeek;
        this.focusArea = focusArea;
        this.workoutEnvironment = workoutEnvironment;
        this.workouts = new ArrayList<>();
        this.isActive = true;
    }

    public void addWorkout(Workout workout) {
        if (workouts == null) {
            workouts = new ArrayList<>();
        }
        workouts.add(workout);
    }

    // Getters and setters
    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
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

    public String getTargetBmiCategory() {
        return targetBmiCategory;
    }

    public void setTargetBmiCategory(String targetBmiCategory) {
        this.targetBmiCategory = targetBmiCategory;
    }

    public String getFitnessGoal() {
        return fitnessGoal;
    }

    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public int getWorkoutsPerWeek() {
        return workoutsPerWeek;
    }

    public void setWorkoutsPerWeek(int workoutsPerWeek) {
        this.workoutsPerWeek = workoutsPerWeek;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getEstimatedCaloriesBurn() {
        return estimatedCaloriesBurn;
    }

    public void setEstimatedCaloriesBurn(int estimatedCaloriesBurn) {
        this.estimatedCaloriesBurn = estimatedCaloriesBurn;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
} 