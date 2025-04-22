package com.example.flextrack_ianation.services;

import com.example.flextrack_ianation.models.Exercise;
import com.example.flextrack_ianation.models.Workout;
import com.example.flextrack_ianation.models.WorkoutPlan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Specialized workout generator for obese individuals.
 * Focus: Extremely low-impact exercises, joint-friendly movements, and incremental progression.
 * Prioritizes safety, accessibility, and sustainable progress.
 */
public class WorkoutGeneratorObese {
    
    public static void addWeightLossWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout weightLossWorkout = new Workout(
                "Gentle Weight Management",
                "Very low-impact exercises designed for safe, gradual weight loss",
                "Full Body",
                workoutTimeMinutes,
                fitnessLevel,
                "Cardio",
                focusArea,
                environment
        );
        
        // Set creation timestamp
        weightLossWorkout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add appropriate exercises based on environment
        if (environment.equals("Home")) {
            addHomeWeightLossExercises(weightLossWorkout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymWeightLossExercises(weightLossWorkout, fitnessLevel);
        } else {
            addOutdoorWeightLossExercises(weightLossWorkout, fitnessLevel);
        }
        
        // Estimate calories burned
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Cardio");
        weightLossWorkout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(weightLossWorkout);
    }
    
    public static void addEnduranceWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout enduranceWorkout = new Workout(
                "Gradual Endurance Building",
                "Safe cardiovascular exercises to slowly build stamina",
                "Full Body",
                workoutTimeMinutes,
                fitnessLevel,
                "Cardio",
                focusArea,
                environment
        );
        
        // Set creation timestamp
        enduranceWorkout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add appropriate exercises based on environment
        if (environment.equals("Home")) {
            addHomeEnduranceExercises(enduranceWorkout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymEnduranceExercises(enduranceWorkout, fitnessLevel);
        } else {
            addOutdoorEnduranceExercises(enduranceWorkout, fitnessLevel);
        }
        
        // Estimate calories burned
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Cardio");
        enduranceWorkout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(enduranceWorkout);
    }
    
    public static void addMuscleBuildingWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout muscleWorkout = new Workout(
                "Safe Strength Development",
                "Joint-friendly strength exercises to build supportive muscle",
                "Full Body",
                workoutTimeMinutes,
                fitnessLevel,
                "Strength",
                focusArea,
                environment
        );
        
        // Set creation timestamp
        muscleWorkout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add appropriate exercises based on environment
        if (environment.equals("Home")) {
            addHomeMuscleExercises(muscleWorkout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymMuscleExercises(muscleWorkout, fitnessLevel);
        } else {
            addOutdoorMuscleExercises(muscleWorkout, fitnessLevel);
        }
        
        // Estimate calories burned
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        muscleWorkout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(muscleWorkout);
    }
    
    public static void addBalancedWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout balancedWorkout = new Workout(
                "Gentle Balanced Fitness",
                "Comprehensive workout combining mobility, light cardio, and basic strength exercises",
                "Full Body",
                workoutTimeMinutes,
                fitnessLevel,
                "Mixed",
                focusArea,
                environment
        );
        
        // Set creation timestamp
        balancedWorkout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add appropriate exercises based on environment
        if (environment.equals("Home")) {
            addHomeBalancedExercises(balancedWorkout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymBalancedExercises(balancedWorkout, fitnessLevel);
        } else {
            addOutdoorBalancedExercises(balancedWorkout, fitnessLevel);
        }
        
        // Estimate calories burned
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Mixed");
        balancedWorkout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(balancedWorkout);
    }
    
    // Supporting exercise methods for weight loss
    private static void addHomeWeightLossExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Seated Marching", "2 sets of 1 minute", "Cardio, Lower Body", 
                2, 60, 60, "Chair", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Arm Circles", "2 sets of 1 minute", "Cardio, Upper Body", 
                2, 60, 60, "Chair", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Side Bends", "2 sets of 10 reps per side", "Core", 
                2, 10, 45, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Wall Arm Raises", "2 sets of 10 reps", "Upper Body", 
                2, 10, 45, "Wall", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Deep Breathing", "2 minutes of slow, deep breaths", "Core", 
                1, 2, 0, "None", fitnessLevel, "Recovery"));
    }
    
    private static void addGymWeightLossExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Water Walking", "10 minutes at comfortable pace", "Cardio", 
                1, 10, 0, "Pool", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Recumbent Bike", "5 minutes very light resistance", "Cardio, Lower Body", 
                1, 5, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Chest Press", "2 sets of 10 reps", "Upper Body", 
                2, 10, 60, "Chest Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Row", "2 sets of 10 reps", "Upper Body", 
                2, 10, 60, "Row Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Stretching Session", "5 minutes of gentle stretching", "Full Body", 
                1, 5, 0, "Mat", fitnessLevel, "Flexibility"));
    }
    
    private static void addOutdoorWeightLossExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Slow Walking", "10 minutes at very comfortable pace", "Cardio", 
                1, 10, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Arm Raises", "2 sets of 10 reps", "Upper Body", 
                2, 10, 45, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Leg Extensions", "2 sets of 10 reps", "Lower Body", 
                2, 10, 45, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Torso Rotations", "2 sets of 10 reps per side", "Core", 
                2, 10, 45, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Deep Breathing", "2 minutes of slow, deep breaths", "Recovery", 
                1, 2, 0, "None", fitnessLevel, "Recovery"));
    }
    
    // Supporting exercise methods for endurance
    private static void addHomeEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Seated Marching", "3 sets of 1 minute", "Cardio, Lower Body", 
                3, 60, 60, "Chair", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Standing Support Marching", "2 sets of 1 minute", "Cardio, Lower Body", 
                2, 60, 60, "Counter or Table for Support", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Arm Pumps", "2 sets of 1 minute", "Cardio, Upper Body", 
                2, 60, 45, "Chair", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Standing Supported Side Steps", "2 sets of 30 seconds", "Cardio, Lower Body", 
                2, 30, 45, "Counter or Wall for Support", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Recovery Breathing", "1 minute of controlled breathing", "Recovery", 
                1, 60, 0, "None", fitnessLevel, "Recovery"));
    }
    
    private static void addGymEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Water Walking", "8 minutes", "Cardio", 
                1, 8, 0, "Pool", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Water Arm Circles", "2 sets of 1 minute", "Cardio, Upper Body", 
                2, 60, 45, "Pool", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Recumbent Bike", "5 minutes", "Cardio, Lower Body", 
                1, 5, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Arm Ergometer", "3 minutes", "Cardio, Upper Body", 
                1, 3, 0, "Arm Ergometer", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Supported Standing", "2 minutes alternating weight shifts", "Balance", 
                1, 2, 0, "Parallel Bars or Support Rails", fitnessLevel, "Balance"));
    }
    
    private static void addOutdoorEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Gentle Walking", "8 minutes with breaks as needed", "Cardio", 
                1, 8, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Bench Seated Arm Circles", "2 sets of 1 minute", "Cardio, Upper Body", 
                2, 60, 45, "Park Bench", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Standing Supported Side Steps", "2 sets of 30 seconds", "Cardio, Lower Body", 
                2, 30, 45, "Bench or Rail for Support", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Gentle Stepping", "10 steps up then down, with support", "Cardio, Lower Body", 
                2, 10, 90, "Low Step with Handrail", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Recovery", "2 minutes seated rest with deep breathing", "Recovery", 
                1, 2, 0, "Bench", fitnessLevel, "Recovery"));
    }
    
    // Supporting exercise methods for muscle building
    private static void addHomeMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Seated Arm Raises", "2 sets of 10 reps", "Shoulders", 
                2, 10, 45, "Chair, Light Weights or Water Bottles", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Wall Push", "2 sets of 10 reps", "Chest, Arms", 
                2, 10, 45, "Wall", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Chair Stand Assist", "2 sets of 8 reps", "Legs", 
                2, 8, 60, "Chair with Armrests", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Knee Lifts", "2 sets of 10 per leg", "Core, Hip Flexors", 
                2, 10, 45, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Back Stretch", "2 sets of 30 seconds", "Back", 
                2, 30, 30, "Chair", fitnessLevel, "Flexibility"));
    }
    
    private static void addGymMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Seated Chest Press (Light)", "2 sets of 10 reps", "Chest, Arms", 
                2, 10, 60, "Chest Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Row (Light)", "2 sets of 10 reps", "Back", 
                2, 10, 60, "Row Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Leg Press (Very Light)", "2 sets of 10 reps", "Legs", 
                2, 10, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Leg Extensions (Light)", "2 sets of 10 reps", "Quadriceps", 
                2, 10, 60, "Leg Extension Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Water Resistance Exercises", "5 minutes", "Full Body", 
                1, 5, 0, "Pool", fitnessLevel, "Strength"));
    }
    
    private static void addOutdoorMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Bench Seated Arm Raises", "2 sets of 10 reps", "Shoulders", 
                2, 10, 45, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bench Assisted Stand-ups", "2 sets of 8 reps", "Legs", 
                2, 8, 60, "Bench with Armrests or Handles", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bench Seated Leg Extensions", "2 sets of 10 reps per leg", "Quadriceps", 
                2, 10, 45, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Wall Lean", "2 sets of 30 seconds", "Full Body", 
                2, 30, 30, "Wall", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Torso Rotations", "2 sets of 10 reps per side", "Core", 
                2, 10, 45, "Bench", fitnessLevel, "Strength"));
    }
    
    // Supporting exercise methods for balanced workout
    private static void addHomeBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Seated Marching", "2 minutes", "Cardio, Lower Body", 
                1, 2, 0, "Chair", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Wall Push", "2 sets of 8 reps", "Upper Body", 
                2, 8, 45, "Wall", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Knee Extensions", "2 sets of 10 reps per leg", "Lower Body", 
                2, 10, 45, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Side Bends", "2 sets of 8 reps per side", "Core", 
                2, 8, 45, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Full Body Seated Stretch Sequence", "3 minutes", "Flexibility", 
                1, 3, 0, "Chair", fitnessLevel, "Flexibility"));
    }
    
    private static void addGymBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Recumbent Bike", "5 minutes", "Cardio", 
                1, 5, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Machine Circuit", "1 set of 10 reps on 3 different machines", "Strength", 
                3, 10, 60, "Weight Machines", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Water Walking", "5 minutes", "Cardio", 
                1, 5, 0, "Pool", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Supported Standing Balance", "2 sets of 30 seconds", "Balance", 
                2, 30, 30, "Support Rails", fitnessLevel, "Balance"));
        workout.addExercise(new Exercise("Guided Stretching", "5 minutes with trainer", "Flexibility", 
                1, 5, 0, "Mat, Trainer", fitnessLevel, "Flexibility"));
    }
    
    private static void addOutdoorBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Gentle Walking", "5 minutes", "Cardio", 
                1, 5, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Bench Assisted Exercises", "3 different movements, 2 sets of 8 reps each", "Strength", 
                6, 8, 45, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Supported Standing Side Steps", "2 sets of 30 seconds", "Cardio, Balance", 
                2, 30, 45, "Bench or Rail for Support", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Rest with Deep Breathing", "2 minutes", "Recovery", 
                1, 2, 0, "Bench", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Seated Stretching", "3 minutes", "Flexibility", 
                1, 3, 0, "Bench", fitnessLevel, "Flexibility"));
    }
    
    // Helper method to estimate calories burned - accounts for obese individuals
    private static int estimateCaloriesBurn(int durationMinutes, String fitnessLevel, String exerciseType) {
        // Base calories burned per minute for different exercise types
        int baseCaloriesPerMinute;
        
        // Different base rates for different exercise types
        // Lower intensity due to modified exercises, but higher calorie burn due to higher body weight
        switch (exerciseType) {
            case "Cardio":
                baseCaloriesPerMinute = 7; // Lower intensity to ensure safety
                break;
            case "Strength":
                baseCaloriesPerMinute = 6; // Modified strength training
                break;
            case "Mixed":
                baseCaloriesPerMinute = 6; // Balanced approach
                break;
            case "Recovery":
                baseCaloriesPerMinute = 3; // Very low intensity
                break;
            default:
                baseCaloriesPerMinute = 5;
        }
        
        // Adjust based on fitness level
        double levelMultiplier = 1.0;
        switch (fitnessLevel) {
            case "Beginner":
                levelMultiplier = 0.7; // Extra caution for beginners
                break;
            case "Intermediate":
                levelMultiplier = 0.9; // Still cautious
                break;
            case "Advanced":
                levelMultiplier = 1.1; // More intensity but still careful
                break;
        }
        
        // Obese individuals burn more calories due to higher body weight
        double weightMultiplier = 1.3; // 30% more calories than standard
        
        // Calculate total calories
        return (int) (baseCaloriesPerMinute * durationMinutes * levelMultiplier * weightMultiplier);
    }
} 