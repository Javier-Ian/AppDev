package com.example.flextrack_ianation.services;

import com.example.flextrack_ianation.models.Exercise;
import com.example.flextrack_ianation.models.Workout;
import com.example.flextrack_ianation.models.WorkoutPlan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Specialized workout generator for normal weight individuals.
 * Focus: Balanced approach to fitness covering strength, endurance, and mobility.
 */
public class WorkoutGeneratorNormal {
    
    public static void addMuscleWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout muscleWorkout = new Workout(
                "Muscle Definition",
                "Balanced workout for muscle definition and tone",
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
    
    public static void addEnduranceWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout enduranceWorkout = new Workout(
                "Endurance Builder",
                "Cardio-focused workout to improve stamina and endurance",
                "Cardio",
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
    
    public static void addBalancedWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout balancedWorkout = new Workout(
                "Complete Fitness",
                "Comprehensive workout with emphasis on overall fitness and functional movement",
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
    
    // Supporting exercise methods
    private static void addHomeMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Push-ups", "3 sets of 12-15 reps", "Upper Body", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 15-20 reps", "Lower Body", 
                3, 20, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Lunges", "3 sets of 12 reps per leg", "Lower Body", 
                3, 12, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Tricep Dips", "3 sets of 12 reps", "Upper Body", 
                3, 12, 60, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Plank", "3 sets of 45 seconds", "Core", 
                3, 45, 45, "None", fitnessLevel, "Strength"));
    }
    
    private static void addGymMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Dumbbell Bench Press", "3 sets of 10-12 reps", "Upper Body", 
                3, 12, 60, "Dumbbells, Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Lat Pulldowns", "3 sets of 10-12 reps", "Upper Body", 
                3, 12, 60, "Lat Pulldown Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Leg Press", "3 sets of 12-15 reps", "Lower Body", 
                3, 15, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Shoulder Press", "3 sets of 10-12 reps", "Upper Body", 
                3, 12, 60, "Dumbbells, Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Ab Crunch Machine", "3 sets of 15 reps", "Core", 
                3, 15, 45, "Ab Machine", fitnessLevel, "Strength"));
    }
    
    private static void addOutdoorMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Push-ups", "3 sets of 10-15 reps", "Upper Body", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Pull-ups on Playground Bar", "3 sets of 6-10 reps", "Upper Body", 
                3, 10, 60, "Bar", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Step-ups", "3 sets of 12 reps per leg", "Lower Body", 
                3, 12, 60, "Bench or Step", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bench Dips", "3 sets of 12 reps", "Upper Body", 
                3, 12, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Russian Twists", "3 sets of 20 reps", "Core", 
                3, 20, 45, "None", fitnessLevel, "Strength"));
    }
    
    private static void addHomeEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Jumping Jacks", "3 sets of 1 minute", "Cardio", 
                3, 60, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("High Knees", "3 sets of 45 seconds", "Cardio", 
                3, 45, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 20 reps", "Lower Body", 
                3, 20, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Mountain Climbers", "3 sets of 1 minute", "Cardio", 
                3, 60, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Burpees", "3 sets of 10 reps", "Full Body", 
                3, 10, 45, "None", fitnessLevel, "Cardio"));
    }
    
    private static void addGymEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Treadmill Intervals", "5 sets of 1 minute sprint, 1 minute rest", "Cardio", 
                5, 60, 60, "Treadmill", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Stationary Bike", "15 minutes at moderate intensity", "Cardio", 
                1, 15, 0, "Stationary Bike", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Stair Climber", "10 minutes", "Cardio", 
                1, 10, 0, "Stair Climber", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Rowing Machine", "10 minutes", "Cardio", 
                1, 10, 0, "Rowing Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Battle Ropes", "3 sets of 30 seconds", "Cardio", 
                3, 30, 30, "Battle Ropes", fitnessLevel, "Cardio"));
    }
    
    private static void addOutdoorEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Running", "20 minutes at moderate pace", "Cardio", 
                1, 20, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Hill Sprints", "5 sets of 30 seconds uphill, walk back down", "Cardio", 
                5, 30, 60, "Hill", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Stair Running", "3 sets of stair climbs", "Cardio", 
                3, 1, 60, "Stairs", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Jump Squats", "3 sets of 15 reps", "Lower Body", 
                3, 15, 45, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Shuttle Runs", "5 sets of 5 shuttle runs", "Cardio", 
                5, 5, 45, "None", fitnessLevel, "Cardio"));
    }
    
    private static void addHomeBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Jump Rope", "3 minutes", "Cardio", 
                1, 180, 60, "Jump Rope", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Push-ups", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 15 reps", "Lower Body", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Plank", "3 sets of 30 seconds", "Core", 
                3, 30, 30, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Walking Lunges", "3 sets of 10 steps per leg", "Lower Body", 
                3, 10, 45, "None", fitnessLevel, "Strength"));
    }
    
    private static void addGymBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Treadmill", "10 minutes at moderate pace", "Cardio", 
                1, 10, 0, "Treadmill", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Dumbbell Bench Press", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Dumbbells, Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Lat Pulldowns", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Lat Pulldown Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Leg Press", "3 sets of 12 reps", "Lower Body", 
                3, 12, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Plank", "3 sets of 45 seconds", "Core", 
                3, 45, 30, "None", fitnessLevel, "Strength"));
    }
    
    private static void addOutdoorBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Jogging", "10 minutes", "Cardio", 
                1, 10, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Push-ups", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 15 reps", "Lower Body", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Plank", "3 sets of 30 seconds", "Core", 
                3, 30, 30, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Walking Lunges", "3 sets of 10 steps per leg", "Lower Body", 
                3, 10, 45, "None", fitnessLevel, "Strength"));
    }
    
    // Helper method to estimate calories burned
    private static int estimateCaloriesBurn(int durationMinutes, String fitnessLevel, String exerciseType) {
        // Base calories burned per minute for strength training
        int baseCaloriesPerMinute;
        
        // Different base rates for different exercise types
        switch (exerciseType) {
            case "Cardio":
                baseCaloriesPerMinute = 8;
                break;
            case "Strength":
                baseCaloriesPerMinute = 6;
                break;
            case "Mixed":
                baseCaloriesPerMinute = 7;
                break;
            default:
                baseCaloriesPerMinute = 6;
        }
        
        // Adjust based on fitness level
        double levelMultiplier = 1.0;
        switch (fitnessLevel) {
            case "Beginner":
                levelMultiplier = 0.8;
                break;
            case "Intermediate":
                levelMultiplier = 1.0;
                break;
            case "Advanced":
                levelMultiplier = 1.2;
                break;
        }
        
        // Calculate total calories
        return (int) (baseCaloriesPerMinute * durationMinutes * levelMultiplier);
    }
} 