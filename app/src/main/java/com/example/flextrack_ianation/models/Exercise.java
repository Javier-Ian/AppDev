package com.example.flextrack_ianation.models;

import java.io.Serializable;

/**
 * Model class representing an individual exercise in a workout.
 */
public class Exercise implements Serializable {
    private String name;
    private String description;
    private String muscleGroup;
    private String imageUrl;
    private int sets;
    private int repsPerSet;
    private int restBetweenSets; // in seconds
    private String equipmentNeeded;
    private String difficultyLevel; // "Beginner", "Intermediate", "Advanced"
    private String exerciseType; // "Strength", "Cardio", "Flexibility", etc.
    private String videoUrl; // Optional URL for demonstration video

    // Empty constructor required for Firebase
    public Exercise() {
    }

    public Exercise(String name, String description, String muscleGroup, int sets, int repsPerSet, 
                   int restBetweenSets, String equipmentNeeded, String difficultyLevel, 
                   String exerciseType) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.restBetweenSets = restBetweenSets;
        this.equipmentNeeded = equipmentNeeded;
        this.difficultyLevel = difficultyLevel;
        this.exerciseType = exerciseType;
    }

    public Exercise(String name, String description, String muscleGroup, int sets, int repsPerSet, 
                   int restBetweenSets, String equipmentNeeded, String difficultyLevel, 
                   String exerciseType, String videoUrl) {
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.restBetweenSets = restBetweenSets;
        this.equipmentNeeded = equipmentNeeded;
        this.difficultyLevel = difficultyLevel;
        this.exerciseType = exerciseType;
        this.videoUrl = videoUrl;
    }

    // Getters and setters
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

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    public void setRepsPerSet(int repsPerSet) {
        this.repsPerSet = repsPerSet;
    }

    public int getRestBetweenSets() {
        return restBetweenSets;
    }

    public void setRestBetweenSets(int restBetweenSets) {
        this.restBetweenSets = restBetweenSets;
    }

    public String getEquipmentNeeded() {
        return equipmentNeeded;
    }

    public void setEquipmentNeeded(String equipmentNeeded) {
        this.equipmentNeeded = equipmentNeeded;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
} 