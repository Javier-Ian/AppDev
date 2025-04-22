package com.example.flextrack_ianation.services;

import com.example.flextrack_ianation.models.Exercise;
import com.example.flextrack_ianation.models.Workout;
import com.example.flextrack_ianation.models.WorkoutPlan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Specialized workout generator for overweight individuals.
 * Focus: Low-impact exercises, progressive cardio, and strength training to improve metabolism.
 */
public class WorkoutGeneratorOverweight {
    
    public static void addWeightLossWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout weightLossWorkout = new Workout(
                "Weight Management",
                "Progressive cardio and light resistance training for healthy weight loss",
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
    
    public static void addStrengthWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout strengthWorkout = new Workout(
                "Functional Strength",
                "Safe strength training to build muscle and boost metabolism",
                "Full Body",
                workoutTimeMinutes,
                fitnessLevel,
                "Strength",
                focusArea,
                environment
        );
        
        // Set creation timestamp
        strengthWorkout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add appropriate exercises based on environment
        if (environment.equals("Home")) {
            addHomeStrengthExercises(strengthWorkout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymStrengthExercises(strengthWorkout, fitnessLevel);
        } else {
            addOutdoorStrengthExercises(strengthWorkout, fitnessLevel);
        }
        
        // Estimate calories burned
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        strengthWorkout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(strengthWorkout);
    }
    
    public static void addBalancedWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout balancedWorkout = new Workout(
                "Balanced Fitness",
                "Comprehensive program with cardio and strength for overall health",
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
    
    public static void addEnduranceWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout enduranceWorkout = new Workout(
                "Endurance Builder",
                "Low-impact cardiovascular training to build stamina and endurance",
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
                "Muscle Development",
                "Joint-friendly resistance training to build muscle and increase metabolism",
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
    
    // Supporting exercise methods
    private static void addHomeWeightLossExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Marching in Place", "5 minutes moderate pace", "Cardio", 
                1, 5, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Modified Jumping Jacks", "3 sets of 1 minute", "Cardio", 
                3, 60, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Wall Push-ups", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Wall", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Leg Raises", "3 sets of 12 reps", "Lower Body", 
                3, 12, 60, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Chair Squats", "3 sets of 10 reps", "Lower Body", 
                3, 10, 60, "Chair", fitnessLevel, "Strength"));
    }
    
    private static void addGymWeightLossExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Recumbent Bike", "10 minutes at comfortable pace", "Cardio", 
                1, 10, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Elliptical", "10 minutes at low resistance", "Cardio", 
                1, 10, 0, "Elliptical Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Leg Press (Light Weight)", "3 sets of 12 reps", "Lower Body", 
                3, 12, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Row Machine", "3 sets of 12 reps", "Upper Body", 
                3, 12, 60, "Rowing Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Chest Press Machine", "3 sets of 12 reps", "Upper Body", 
                3, 12, 60, "Chest Press Machine", fitnessLevel, "Strength"));
    }
    
    private static void addOutdoorWeightLossExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Walking", "15 minutes at comfortable pace", "Cardio", 
                1, 15, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Park Bench Step-ups", "3 sets of 8 reps per leg", "Lower Body", 
                3, 8, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Modified Push-ups on Bench", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Walking Lunges", "3 sets of 8 steps per leg", "Lower Body", 
                3, 8, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Stationary Marching", "3 sets of 1 minute", "Cardio", 
                3, 60, 30, "None", fitnessLevel, "Cardio"));
    }
    
    private static void addHomeStrengthExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Modified Push-ups", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Chair Squats", "3 sets of 12 reps", "Lower Body", 
                3, 12, 60, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Wall Plank", "3 sets of 30 seconds", "Core", 
                3, 30, 45, "Wall", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Water Bottle Curls", "3 sets of 12 reps", "Upper Body", 
                3, 12, 45, "Water Bottles", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Leg Extensions", "3 sets of 12 reps", "Lower Body", 
                3, 12, 45, "Chair", fitnessLevel, "Strength"));
    }
    
    private static void addGymStrengthExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Leg Press", "3 sets of 12 reps", "Lower Body", 
                3, 12, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Lat Pulldown", "3 sets of 12 reps", "Upper Body", 
                3, 12, 60, "Lat Pulldown Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Chest Press", "3 sets of 12 reps", "Upper Body", 
                3, 12, 60, "Chest Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Leg Curl", "3 sets of 12 reps", "Lower Body", 
                3, 12, 60, "Leg Curl Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Shoulder Press", "3 sets of 12 reps", "Upper Body", 
                3, 12, 60, "Shoulder Press Machine", fitnessLevel, "Strength"));
    }
    
    private static void addOutdoorStrengthExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Bench Push-ups", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Park Bench Squats", "3 sets of 12 reps", "Lower Body", 
                3, 12, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bench Tricep Dips", "3 sets of 8 reps", "Upper Body", 
                3, 8, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Stationary Lunges", "3 sets of 10 reps per leg", "Lower Body", 
                3, 10, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Standing Bicycle Crunches", "3 sets of 12 reps per side", "Core", 
                3, 12, 45, "None", fitnessLevel, "Strength"));
    }
    
    private static void addHomeBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Marching in Place", "5 minutes", "Cardio", 
                1, 5, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Modified Push-ups", "3 sets of 8 reps", "Upper Body", 
                3, 8, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Chair Squats", "3 sets of 10 reps", "Lower Body", 
                3, 10, 60, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Russian Twists", "3 sets of 12 reps per side", "Core", 
                3, 12, 45, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("High Knee Marching", "3 sets of 45 seconds", "Cardio", 
                3, 45, 45, "None", fitnessLevel, "Cardio"));
    }
    
    private static void addGymBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Recumbent Bike", "8 minutes", "Cardio", 
                1, 8, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Leg Press", "3 sets of 10 reps", "Lower Body", 
                3, 10, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Chest Press", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Chest Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Elliptical", "8 minutes", "Cardio", 
                1, 8, 0, "Elliptical Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Cable Rows", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Cable Machine", fitnessLevel, "Strength"));
    }
    
    private static void addOutdoorBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Walking", "10 minutes", "Cardio", 
                1, 10, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Bench Push-ups", "3 sets of 8 reps", "Upper Body", 
                3, 8, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Park Bench Squats", "3 sets of 10 reps", "Lower Body", 
                3, 10, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Walking Lunges", "3 sets of 8 steps per leg", "Lower Body", 
                3, 8, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Bicycle Crunches", "3 sets of 10 reps per side", "Core", 
                3, 10, 45, "Bench", fitnessLevel, "Strength"));
    }
    
    // Supporting exercise methods for endurance workouts
    private static void addHomeEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Low-Impact Marching", "5 minutes moderate pace", "Cardio", 
                1, 5, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Standing Side Steps", "3 sets of 1 minute", "Cardio", 
                3, 60, 45, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Punches", "3 sets of 1 minute", "Cardio, Upper Body", 
                3, 60, 45, "Chair", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Step Touches", "3 sets of 1 minute", "Cardio, Lower Body", 
                3, 60, 45, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Arm Circles", "3 sets of 1 minute", "Cardio, Upper Body", 
                3, 60, 30, "None", fitnessLevel, "Cardio"));
    }
    
    private static void addGymEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Recumbent Bike", "10 minutes with intervals", "Cardio", 
                1, 10, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Water Aerobics", "10 minutes", "Cardio", 
                1, 10, 0, "Pool", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Elliptical Trainer", "10 minutes easy pace", "Cardio", 
                1, 10, 0, "Elliptical Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Stepper", "5 minutes", "Cardio, Lower Body", 
                1, 5, 0, "Stepper Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Arm Ergometer", "5 minutes", "Cardio, Upper Body", 
                1, 5, 0, "Arm Ergometer", fitnessLevel, "Cardio"));
    }
    
    private static void addOutdoorEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Walking Intervals", "15 minutes (1 min brisk, 2 min normal)", "Cardio", 
                5, 3, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Bench Step-ups (Slow Pace)", "3 sets of 10 reps per leg", "Cardio, Lower Body", 
                3, 10, 60, "Bench", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Standing Side Bends", "3 sets of 15 reps per side", "Core", 
                3, 15, 45, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Modified Jumping Jacks", "3 sets of 45 seconds", "Cardio", 
                3, 45, 45, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Seated Arm Circles", "3 sets of 30 seconds", "Cardio, Upper Body", 
                3, 30, 30, "Bench", fitnessLevel, "Cardio"));
    }
    
    // Supporting exercise methods for muscle building
    private static void addHomeMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Wall Push-ups", "3 sets of 12 reps", "Chest, Shoulders, Triceps", 
                3, 12, 60, "Wall", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Chair Squats", "3 sets of 12 reps", "Quadriceps, Hamstrings, Glutes", 
                3, 12, 60, "Chair", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Shoulder Press", "3 sets of 12 reps", "Shoulders", 
                3, 12, 60, "Light Dumbbells or Water Bottles", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Row with Resistance Band", "3 sets of 12 reps", "Back, Biceps", 
                3, 12, 60, "Resistance Band", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Leg Extensions", "3 sets of 15 reps", "Quadriceps", 
                3, 15, 45, "Chair", fitnessLevel, "Strength"));
    }
    
    private static void addGymMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Machine Chest Press", "3 sets of 12 reps", "Chest, Triceps", 
                3, 12, 60, "Chest Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Leg Press", "3 sets of 12 reps", "Quadriceps, Hamstrings, Glutes", 
                3, 12, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Lat Pulldown", "3 sets of 12 reps", "Back, Biceps", 
                3, 12, 60, "Lat Pulldown Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Leg Curl", "3 sets of 12 reps", "Hamstrings", 
                3, 12, 60, "Leg Curl Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Machine Shoulder Press", "3 sets of 12 reps", "Shoulders", 
                3, 12, 60, "Shoulder Press Machine", fitnessLevel, "Strength"));
    }
    
    private static void addOutdoorMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Bench Incline Push-ups", "3 sets of 12 reps", "Chest, Shoulders", 
                3, 12, 60, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bench Step-ups", "3 sets of 10 reps per leg", "Quadriceps, Glutes", 
                3, 10, 60, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bench Dips", "3 sets of 10 reps", "Triceps", 
                3, 10, 60, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Standing Rows with Resistance Band", "3 sets of 12 reps", "Back, Biceps", 
                3, 12, 60, "Resistance Band, Tree or Post", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Stationary Lunges", "3 sets of 10 reps per leg", "Quadriceps, Hamstrings, Glutes", 
                3, 10, 60, "None", fitnessLevel, "Strength"));
    }
    
    // Helper method to estimate calories burned
    private static int estimateCaloriesBurn(int durationMinutes, String fitnessLevel, String exerciseType) {
        // Base calories burned per minute for different exercise types
        // For overweight individuals, standard multipliers are used but with slightly modified base rates
        int baseCaloriesPerMinute;
        
        // Different base rates for different exercise types
        switch (exerciseType) {
            case "Cardio":
                baseCaloriesPerMinute = 8; // Lower intensity cardio for joint protection
                break;
            case "Strength":
                baseCaloriesPerMinute = 7; // Standard strength training
                break;
            case "Mixed":
                baseCaloriesPerMinute = 7; // Balanced approach
                break;
            case "Recovery":
                baseCaloriesPerMinute = 4; // Very low intensity
                break;
            default:
                baseCaloriesPerMinute = 6;
        }
        
        // Adjust based on fitness level
        double levelMultiplier = 1.0;
        switch (fitnessLevel) {
            case "Beginner":
                levelMultiplier = 0.8; // Standard beginner multiplier
                break;
            case "Intermediate":
                levelMultiplier = 1.0; // Standard intermediate multiplier
                break;
            case "Advanced":
                levelMultiplier = 1.2; // Standard advanced multiplier
                break;
        }
        
        // Overweight individuals typically burn more calories due to higher body weight
        double weightMultiplier = 1.15; // 15% more calories than standard
        
        // Calculate total calories
        return (int) (baseCaloriesPerMinute * durationMinutes * levelMultiplier * weightMultiplier);
    }
} 