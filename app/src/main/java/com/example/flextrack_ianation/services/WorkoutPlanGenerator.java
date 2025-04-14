package com.example.flextrack_ianation.services;

import com.example.flextrack_ianation.models.Exercise;
import com.example.flextrack_ianation.models.Workout;
import com.example.flextrack_ianation.models.WorkoutPlan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class for generating personalized workout plans based on user's health profile.
 */
public class WorkoutPlanGenerator {

    // Constants for progression
    private static final float WEEK_2_INTENSITY_MULTIPLIER = 1.1f;
    private static final float WEEK_3_INTENSITY_MULTIPLIER = 1.2f;
    private static final float WEEK_4_INTENSITY_MULTIPLIER = 1.3f;

    public static WorkoutPlan generateWorkoutPlan(String bmiCategory, String fitnessGoal,
                                                 String fitnessLevel, String focusArea,
                                                 String workoutEnvironment, int workoutTimeMinutes,
                                                 int daysPerWeek) {
        
        // Create a workout plan based on the user's profile
        String planName = getPlanName(bmiCategory, fitnessGoal);
        String description = getPlanDescription(bmiCategory, fitnessGoal, fitnessLevel);
        
        // Determine plan duration based on fitness level and goals
        int durationWeeks = calculatePlanDuration(bmiCategory, fitnessLevel, fitnessGoal);
        
        // Calculate appropriate number of workouts per week based on fitness level and available days
        int workoutsPerWeek = calculateWorkoutsPerWeek(daysPerWeek, fitnessLevel, bmiCategory);

        WorkoutPlan plan = new WorkoutPlan(
                planName,
                description,
                bmiCategory,
                fitnessGoal,
                fitnessLevel,
                durationWeeks,
                workoutsPerWeek,
                focusArea,
                workoutEnvironment
        );
        
        // Set creation timestamp
        plan.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add workouts to the plan
        addWorkoutsToPlan(plan, bmiCategory, fitnessGoal, fitnessLevel, focusArea, workoutEnvironment, workoutTimeMinutes, daysPerWeek);
        
        // Calculate estimated calories burn for the entire plan
        estimateTotalCaloriesBurn(plan);
        
        return plan;
    }
    
    private static int calculatePlanDuration(String bmiCategory, String fitnessLevel, String fitnessGoal) {
        // Base duration on multiple factors
        int baseWeeks = 4; // Default duration
        
        // Adjust based on BMI category
        switch (bmiCategory) {
            case "Underweight":
                baseWeeks = 6; // Longer plan for gradual muscle gain
                break;
            case "Overweight":
                baseWeeks = 8; // Longer plan for sustainable weight loss
                break;
            case "Obese":
                baseWeeks = 12; // Extended plan for safe progression
                break;
        }
        
        // Adjust based on fitness level
        switch (fitnessLevel) {
            case "Beginner":
                baseWeeks += 2; // Extra weeks for beginners to build foundation
                break;
            case "Advanced":
                baseWeeks -= 1; // Advanced users can progress faster
                break;
        }
        
        // Adjust based on fitness goal
        if (fitnessGoal.equals("Build Muscle")) {
            baseWeeks += 2; // Muscle building typically requires longer duration
        } else if (fitnessGoal.equals("Lose Weight")) {
            baseWeeks += 4; // Weight loss needs more time for sustainable results
        }
        
        return Math.max(4, baseWeeks); // Minimum 4 weeks
    }
    
    private static int calculateWorkoutsPerWeek(int availableDays, String fitnessLevel, String bmiCategory) {
        // Start with available days
        int workouts = availableDays;
        
        // Adjust based on fitness level
        switch (fitnessLevel) {
            case "Beginner":
                workouts = Math.min(3, availableDays); // Beginners start with fewer workouts
                break;
            case "Intermediate":
                workouts = Math.min(4, availableDays); // Intermediate can handle more
                break;
            case "Advanced":
                workouts = Math.min(5, availableDays); // Advanced can handle more intensity
                break;
        }
        
        // Adjust based on BMI category
        switch (bmiCategory) {
            case "Overweight":
            case "Obese":
                workouts = Math.min(4, workouts); // Limit workouts for weight loss focus
                break;
        }
        
        return Math.max(2, workouts); // Minimum 2 workouts per week
    }
    
    private static String getPlanName(String bmiCategory, String fitnessGoal) {
        switch (bmiCategory) {
            case "Underweight":
                return "Build & Strengthen Plan";
            case "Normal weight":
                switch (fitnessGoal) {
                    case "Build Muscle":
                        return "Muscle Building Program";
                    case "Lose Weight":
                        return "Lean Definition Plan";
                    case "Improve Endurance":
                        return "Endurance Booster Plan";
                    default:
                        return "Balanced Fitness Plan";
                }
            case "Overweight":
                return "Weight Management Program";
            case "Obese":
                return "Progressive Fitness Journey";
            default:
                return "Custom Fitness Plan";
        }
    }
    
    private static String getPlanDescription(String bmiCategory, String fitnessGoal, String fitnessLevel) {
        StringBuilder description = new StringBuilder();
        
        // Base description on BMI category
        switch (bmiCategory) {
            case "Underweight":
                description.append("A specialized plan focused on building muscle mass and strength. ");
                description.append("This program emphasizes progressive overload with adequate recovery and higher calorie intake. ");
                break;
            case "Normal weight":
                description.append("A balanced plan designed to maintain healthy weight while ");
                if (fitnessGoal.equals("Build Muscle")) {
                    description.append("increasing muscle mass and strength. ");
                } else if (fitnessGoal.equals("Lose Weight")) {
                    description.append("achieving greater muscle definition and toning. ");
                } else if (fitnessGoal.equals("Improve Endurance")) {
                    description.append("improving cardiovascular endurance and stamina. ");
                } else {
                    description.append("enhancing overall fitness and health. ");
                }
                break;
            case "Overweight":
                description.append("A comprehensive plan combining cardio exercises for calorie burning with strength training ");
                description.append("to preserve muscle mass while losing weight. This program focuses on sustainable progress. ");
                break;
            case "Obese":
                description.append("A carefully designed progressive program starting with low-impact exercises ");
                description.append("and gradually increasing intensity as fitness improves. Focus is on joint-friendly movements ");
                description.append("and building a sustainable exercise habit. ");
                break;
            default:
                description.append("A personalized fitness program tailored to your specific needs and goals. ");
        }
        
        // Add fitness level description
        switch (fitnessLevel) {
            case "Beginner":
                description.append("This program is designed for beginners with clear instructions and proper progression. ");
                break;
            case "Intermediate":
                description.append("This program includes more challenging exercises with varied intensity for continued progress. ");
                break;
            case "Advanced":
                description.append("This program features complex movements and higher intensity to challenge experienced fitness enthusiasts. ");
                break;
        }
        
        description.append("Follow this plan consistently for best results.");
        return description.toString();
    }
    
    private static void addWorkoutsToPlan(
            WorkoutPlan plan, String bmiCategory, String fitnessGoal, String fitnessLevel,
            String focusArea, String environment, int workoutTimePerDay, int daysPerWeek) {
        
        // Number of weeks to generate workouts for - based on plan duration
        int totalWeeks = plan.getDurationWeeks();
        int workoutsPerWeek = plan.getWorkoutsPerWeek();
        
        // Generate workouts for each week with progressive overload
        for (int week = 1; week <= totalWeeks; week++) {
            // Calculate intensity progression factor (increase intensity gradually)
            float progressionFactor = calculateProgressionFactor(week, totalWeeks, fitnessLevel);
            
            // Generate workouts for this week based on BMI category and fitness goal
            for (int day = 1; day <= workoutsPerWeek; day++) {
                // Determine if this is a rest day or active recovery
                if (shouldBeActiveRecovery(day, week, workoutsPerWeek, fitnessLevel)) {
                    addActiveRecoveryWorkout(plan, workoutTimePerDay, fitnessLevel, 
                            focusArea, environment, week);
                    continue;
                }
                
                // Add appropriate workout based on BMI category and goals with progression
                addSpecificWorkout(plan, bmiCategory, fitnessGoal, fitnessLevel, 
                        focusArea, environment, workoutTimePerDay, progressionFactor, week);
            }
        }
    }
    
    private static float calculateProgressionFactor(int currentWeek, int totalWeeks, String fitnessLevel) {
        // Base progression - starts at 1.0 and increases gradually
        float baseIncrease = (currentWeek - 1) * 0.05f;
        
        // Adjust progression rate based on fitness level
        switch (fitnessLevel) {
            case "Beginner":
                return 1.0f + (baseIncrease * 0.7f); // Slower progression
            case "Intermediate":
                return 1.0f + baseIncrease;          // Normal progression
            case "Advanced":
                return 1.0f + (baseIncrease * 1.3f); // Faster progression
            default:
                return 1.0f + baseIncrease;
        }
    }
    
    private static boolean shouldBeActiveRecovery(int day, int week, int workoutsPerWeek, String fitnessLevel) {
        // Determine if this workout should be active recovery based on fitness level and week pattern
        
        // For beginners, every 3rd workout is active recovery
        if (fitnessLevel.equals("Beginner") && day % 3 == 0) {
            return true;
        }
        
        // For intermediate, every 4th workout is active recovery
        if (fitnessLevel.equals("Intermediate") && day % 4 == 0) {
            return true;
        }
        
        // For advanced, every 5th workout is active recovery
        if (fitnessLevel.equals("Advanced") && day % 5 == 0) {
            return true;
        }
        
        return false;
    }
    
    private static void addActiveRecoveryWorkout(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment, int week) {
        
        Workout recoveryWorkout = new Workout(
                "Active Recovery",
                "Light activity to promote recovery while maintaining movement",
                "Full Body",
                workoutTimeMinutes,
                fitnessLevel,
                "Recovery",
                focusArea,
                environment
        );
        
        // Set creation timestamp
        recoveryWorkout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add recovery exercises based on environment
        if (environment.equals("Home")) {
            addHomeRecoveryExercises(recoveryWorkout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymRecoveryExercises(recoveryWorkout, fitnessLevel);
        } else {
            addOutdoorRecoveryExercises(recoveryWorkout, fitnessLevel);
        }
        
        // Set week number for progression tracking
        recoveryWorkout.setWeekNumber(week);
        
        // Estimate calories burned
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Recovery") / 2; // Lower intensity
        recoveryWorkout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(recoveryWorkout);
    }
    
    private static void addSpecificWorkout(WorkoutPlan plan, String bmiCategory, String fitnessGoal, 
            String fitnessLevel, String focusArea, String environment, int workoutTimePerDay, 
            float progressionFactor, int week) {
        
        // Generate the specific workout based on BMI category and fitness goal
        if (bmiCategory.equals("Obese")) {
            addObeseSpecificWorkout(plan, fitnessGoal, fitnessLevel, focusArea, 
                    environment, workoutTimePerDay, progressionFactor, week);
        } else if (bmiCategory.equals("Overweight")) {
            addOverweightSpecificWorkout(plan, fitnessGoal, fitnessLevel, focusArea, 
                    environment, workoutTimePerDay, progressionFactor, week);
        } else if (bmiCategory.equals("Normal weight")) {
            addNormalWeightSpecificWorkout(plan, fitnessGoal, fitnessLevel, focusArea, 
                    environment, workoutTimePerDay, progressionFactor, week);
        } else if (bmiCategory.equals("Underweight")) {
            addUnderweightSpecificWorkout(plan, fitnessGoal, fitnessLevel, focusArea, 
                    environment, workoutTimePerDay, progressionFactor, week);
        } else {
            // Default to normal weight workouts for any other BMI category
            addNormalWeightSpecificWorkout(plan, fitnessGoal, fitnessLevel, focusArea, 
                    environment, workoutTimePerDay, progressionFactor, week);
        }
    }
    
    private static void addObeseSpecificWorkout(WorkoutPlan plan, String fitnessGoal, String fitnessLevel, 
            String focusArea, String environment, int workoutTimePerDay, float progressionFactor, int week) {
        
        // Create a specialized workout for obese individuals based on their fitness goal
        Workout workout = null;
        
        if (fitnessGoal.equals("Weight Loss") || fitnessGoal.equals("Lose Weight")) {
            // Low-impact cardio focused workout for weight loss
            workout = new Workout(
                    "Low-Impact Cardio",
                    "Gentle cardio workout designed for weight loss with minimal joint stress",
                    "Cardiovascular System, " + focusArea,
                    workoutTimePerDay,
                    fitnessLevel,
                    "Cardio",
                    focusArea,
                    environment
            );
            
            // Set creation timestamp
            workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            
            // Add exercises based on environment and focus area
            if (environment.equals("Home")) {
                addObeseHomeCardioExercises(workout, fitnessLevel, focusArea);
            } else if (environment.equals("Gym")) {
                addObeseGymCardioExercises(workout, fitnessLevel, focusArea);
            } else {
                addObeseOutdoorCardioExercises(workout, fitnessLevel, focusArea);
            }
            
        } else if (fitnessGoal.equals("Build Muscle") || fitnessGoal.equals("Muscle Gain")) {
            // Resistance training focused on building strength with joint support
            workout = new Workout(
                    "Progressive Strength Training",
                    "Strength-focused workout with emphasis on proper form and joint health",
                    "Muscular System, " + focusArea,
                    workoutTimePerDay,
                    fitnessLevel,
                    "Strength",
                    focusArea,
                    environment
            );
            
            // Set creation timestamp
            workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            
            // Add exercises based on environment and focus area
            if (environment.equals("Home")) {
                addObeseHomeStrengthExercises(workout, fitnessLevel, focusArea);
            } else if (environment.equals("Gym")) {
                addObeseGymStrengthExercises(workout, fitnessLevel, focusArea);
            } else {
                addObeseOutdoorStrengthExercises(workout, fitnessLevel, focusArea);
            }
        } else {
            // Balanced, general fitness approach for other goals
            workout = new Workout(
                    "Balanced Movement Program",
                    "Well-rounded workout combining gentle cardio, strength, and flexibility",
                    "Full Body, " + focusArea,
                    workoutTimePerDay,
                    fitnessLevel,
                    "Mixed",
                    focusArea,
                    environment
            );
            
            // Set creation timestamp
            workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            
            // Add exercises based on environment and focus area
            if (environment.equals("Home")) {
                addObeseHomeBalancedExercises(workout, fitnessLevel, focusArea);
            } else if (environment.equals("Gym")) {
                addObeseGymBalancedExercises(workout, fitnessLevel, focusArea);
            } else {
                addObeseOutdoorBalancedExercises(workout, fitnessLevel, focusArea);
            }
        }
        
        // Estimate calories burned 
        int calorieEstimate = estimateCaloriesBurn(workoutTimePerDay, fitnessLevel, workout.getWorkoutType());
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        // Set week number for progression tracking
        workout.setWeekNumber(week);
        
        // Add workout to the plan
        plan.addWorkout(workout);
    }
    
    private static void addOverweightSpecificWorkout(WorkoutPlan plan, String fitnessGoal, String fitnessLevel, 
            String focusArea, String environment, int workoutTimePerDay, float progressionFactor, int week) {
        
        // Create a specialized workout for overweight individuals based on their fitness goal
        Workout workout = null;
        
        if (fitnessGoal.equals("Weight Loss") || fitnessGoal.equals("Lose Weight")) {
            // Moderate intensity cardio focused workout for weight loss
            workout = new Workout(
                    "Calorie-Burning Cardio",
                    "Effective cardio workout designed to maximize calorie burning",
                    "Cardiovascular System, " + focusArea,
                    workoutTimePerDay,
                    fitnessLevel,
                    "Cardio",
                    focusArea,
                    environment
            );
            
            // Set creation timestamp
            workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            
            // Add exercises based on environment and focus area
            if (environment.equals("Home")) {
                addOverweightHomeCardioExercises(workout, fitnessLevel, focusArea);
            } else if (environment.equals("Gym")) {
                addOverweightGymCardioExercises(workout, fitnessLevel, focusArea);
            } else {
                addOverweightOutdoorCardioExercises(workout, fitnessLevel, focusArea);
            }
            
        } else if (fitnessGoal.equals("Build Muscle") || fitnessGoal.equals("Muscle Gain")) {
            // Combined strength and cardio for muscle building with fat loss
            workout = new Workout(
                    "Strength & Conditioning",
                    "Workout that builds muscle while maintaining elevated heart rate",
                    "Muscular System, " + focusArea,
                    workoutTimePerDay,
                    fitnessLevel,
                    "Strength",
                    focusArea,
                    environment
            );
            
            // Set creation timestamp
            workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            
            // Add exercises based on environment and focus area
            if (environment.equals("Home")) {
                addOverweightHomeStrengthExercises(workout, fitnessLevel, focusArea);
            } else if (environment.equals("Gym")) {
                addOverweightGymStrengthExercises(workout, fitnessLevel, focusArea);
            } else {
                addOverweightOutdoorStrengthExercises(workout, fitnessLevel, focusArea);
            }
        } else {
            // Balanced, general fitness approach for other goals
            workout = new Workout(
                    "Balanced Fitness Circuit",
                    "Circuit-style workout combining strength, cardio, and mobility",
                    "Full Body, " + focusArea,
                    workoutTimePerDay,
                    fitnessLevel,
                    "Mixed",
                    focusArea,
                    environment
            );
            
            // Set creation timestamp
            workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
            
            // Add exercises based on environment and focus area
            if (environment.equals("Home")) {
                addOverweightHomeBalancedExercises(workout, fitnessLevel, focusArea);
            } else if (environment.equals("Gym")) {
                addOverweightGymBalancedExercises(workout, fitnessLevel, focusArea);
            } else {
                addOverweightOutdoorBalancedExercises(workout, fitnessLevel, focusArea);
            }
        }
        
        // Estimate calories burned 
        int calorieEstimate = estimateCaloriesBurn(workoutTimePerDay, fitnessLevel, workout.getWorkoutType());
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        // Set week number for progression tracking
        workout.setWeekNumber(week);
        
        // Add workout to the plan
        plan.addWorkout(workout);
    }
    
    private static void addNormalWeightSpecificWorkout(WorkoutPlan plan, String fitnessGoal, String fitnessLevel, 
            String focusArea, String environment, int workoutTimePerDay, float progressionFactor, int week) {
        
        // Add appropriate workouts based on fitness goal
        if (fitnessGoal.equals("Build Muscle") || fitnessGoal.equals("Muscle Gain")) {
            WorkoutGeneratorNormalWeight.addMuscleBuildingWorkouts(plan, workoutTimePerDay, fitnessLevel, focusArea, environment);
        } else if (fitnessGoal.equals("Improve Endurance") || fitnessGoal.equals("Endurance")) {
            WorkoutGeneratorNormalWeight.addEnduranceWorkouts(plan, workoutTimePerDay, fitnessLevel, focusArea, environment);
        } else if (fitnessGoal.equals("Lose Weight") || fitnessGoal.equals("Weight Loss")) {
            WorkoutGeneratorNormalWeight.addEnduranceWorkouts(plan, workoutTimePerDay, fitnessLevel, focusArea, environment);
        } else {
            WorkoutGeneratorNormalWeight.addBalancedWorkouts(plan, workoutTimePerDay, fitnessLevel, focusArea, environment);
        }
        
        // Set week number for the most recently added workout
        if (!plan.getWorkouts().isEmpty()) {
            Workout lastWorkout = plan.getWorkouts().get(plan.getWorkouts().size() - 1);
            lastWorkout.setWeekNumber(week);
        }
    }
    
    private static void addUnderweightSpecificWorkout(WorkoutPlan plan, String fitnessGoal, String fitnessLevel, 
            String focusArea, String environment, int workoutTimePerDay, float progressionFactor, int week) {
        
        // For underweight individuals, call the addWorkouts method directly
        WorkoutGeneratorUnderweight.addWorkouts(plan, workoutTimePerDay, fitnessLevel, focusArea, environment);
        
        // Set week number for the most recently added workout
        if (!plan.getWorkouts().isEmpty()) {
            Workout lastWorkout = plan.getWorkouts().get(plan.getWorkouts().size() - 1);
            lastWorkout.setWeekNumber(week);
        }
    }
    
    private static void addHomeRecoveryExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Gentle Walking in Place", "5 minutes at very easy pace", "Full Body", 
                1, 5, 0, "None", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Arm Circles", "1 minute forward, 1 minute backward", "Upper Body", 
                2, 60, 30, "None", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Gentle Torso Twists", "1 minute of slow rotations", "Core", 
                2, 60, 30, "None", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Knee Raises", "1 minute of slow knee raises", "Lower Body", 
                2, 60, 30, "Chair (optional for support)", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Full Body Stretch Sequence", "5 minutes of gentle stretching", "Full Body", 
                1, 5, 0, "None", fitnessLevel, "Flexibility"));
    }
    
    private static void addGymRecoveryExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Recumbent Bike", "10 minutes at very light pace", "Lower Body", 
                1, 10, 0, "Recumbent Bike", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Light Rowing Machine", "5 minutes at easy pace", "Full Body", 
                1, 5, 0, "Rowing Machine", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Gentle Mobility Circuit", "5 minutes of joint rotations", "Full Body", 
                1, 5, 0, "None", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Foam Rolling", "5 minutes rolling major muscle groups", "Full Body", 
                1, 5, 0, "Foam Roller", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Stretching Area", "5 minutes of guided stretching", "Full Body", 
                1, 5, 0, "Mat", fitnessLevel, "Flexibility"));
    }
    
    private static void addOutdoorRecoveryExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Leisurely Walk", "15 minutes at comfortable pace", "Full Body", 
                1, 15, 0, "None", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Gentle Arm Swings", "1 minute forward, 1 minute backward", "Upper Body", 
                2, 60, 30, "None", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Standing Torso Twists", "1 minute of slow rotations", "Core", 
                2, 60, 30, "None", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Calf Raises on Curb", "2 sets of 10 slow raises", "Lower Body", 
                2, 10, 60, "Curb or Step", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Nature Stretching Sequence", "5 minutes of gentle stretching", "Full Body", 
                1, 5, 0, "None", fitnessLevel, "Flexibility"));
    }
    
    private static void estimateTotalCaloriesBurn(WorkoutPlan plan) {
        // Simple estimation based on workouts
        int totalCalories = 0;
        for (Workout workout : plan.getWorkouts()) {
            totalCalories += workout.getCaloriesBurnEstimate();
        }
        plan.setEstimatedCaloriesBurn(totalCalories);
    }

    private static int estimateCaloriesBurn(int durationMinutes, String fitnessLevel, String exerciseType) {
        // Base calories burned per minute for different exercise types
        int baseCaloriesPerMinute;
        
        // Different base rates for different exercise types
        switch (exerciseType) {
            case "Cardio":
                baseCaloriesPerMinute = 10;
                break;
            case "Strength":
                baseCaloriesPerMinute = 8;
                break;
            case "Recovery":
                baseCaloriesPerMinute = 5;
                break;
            case "Mixed":
                baseCaloriesPerMinute = 9;
                break;
            default:
                baseCaloriesPerMinute = 7;
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

    // Helper methods for obese cardio exercises
    private static void addObeseHomeCardioExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common exercises regardless of focus area
        workout.addExercise(new Exercise("Seated Marching", "Sit on a chair and march in place", "Cardio, Lower Body", 
                3, 60, 60, "Chair", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Modified Jumping Jacks", "Arm motions without jumps", "Cardio, Full Body", 
                3, 45, 60, "None", fitnessLevel, "Cardio"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Seated Arm Circles", "Circular motions with arms", "Shoulders, Arms", 
                    3, 30, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Wall Push-offs", "Push-ups against a wall", "Chest, Triceps", 
                    3, 10, 60, "Wall", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Seated Leg Extensions", "Extend legs from seated position", "Quadriceps", 
                    3, 12, 60, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Calf Raises with Support", "Raise heels with chair support", "Calves", 
                    3, 15, 45, "Chair", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Seated Core Rotations", "Twist torso while seated", "Obliques", 
                    3, 10, 45, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Leg Lifts", "Raise legs slightly off floor", "Lower Abs", 
                    3, 8, 60, "Chair", fitnessLevel, "Strength"));
        } else {
            // Full Body or Cardio focus
            workout.addExercise(new Exercise("Seated Total Body Movement", "Coordinated arm and leg movements", "Full Body", 
                    3, 60, 60, "Chair", fitnessLevel, "Cardio"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Deep Breathing", "Controlled breathing with arm raises", "Recovery", 
                1, 3, 0, "None", fitnessLevel, "Recovery"));
    }
    
    private static void addObeseGymCardioExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common exercises regardless of focus area
        workout.addExercise(new Exercise("Recumbent Bike", "Low resistance cycling", "Cardio, Lower Body", 
                1, 10, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Seated Arm Ergometer", "Upper body cycling motion", "Shoulders, Arms", 
                    1, 8, 0, "Arm Ergometer", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Seated Cable Rows", "Light weight, higher reps", "Back, Biceps", 
                    3, 15, 60, "Cable Machine", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Seated Leg Press", "Light weight, higher reps", "Quadriceps, Hamstrings", 
                    3, 15, 60, "Leg Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Leg Extensions", "Controlled movement", "Quadriceps", 
                    3, 12, 60, "Leg Extension Machine", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Seated Cable Rotations", "Light weight, controlled", "Obliques", 
                    3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Stability Ball Seated Movements", "Gentle balance work", "Core", 
                    3, 30, 60, "Stability Ball", fitnessLevel, "Strength"));
        } else {
            // Full Body or Cardio focus
            workout.addExercise(new Exercise("Elliptical Trainer", "Low resistance", "Full Body", 
                    1, 10, 0, "Elliptical", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Water Aerobics", "If pool available", "Full Body", 
                    1, 15, 0, "Pool", fitnessLevel, "Cardio"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Stretching on Mat", "Gentle full body stretches", "Flexibility", 
                1, 5, 0, "Exercise Mat", fitnessLevel, "Flexibility"));
    }
    
    private static void addObeseOutdoorCardioExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common exercises regardless of focus area
        workout.addExercise(new Exercise("Gentle Walking", "Flat surface, moderate pace", "Cardio, Lower Body", 
                1, 10, 0, "None", fitnessLevel, "Cardio"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Walking with Arm Movements", "Add arm circles while walking", "Cardio, Upper Body", 
                    3, 2, 60, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Bench or Wall Push-offs", "Inclined push-up motion", "Chest, Triceps", 
                    3, 10, 60, "Bench or Wall", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Supported Squats", "Using bench for support", "Quadriceps, Glutes", 
                    3, 10, 60, "Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Step-ups (Low Height)", "Low bench or step", "Lower Body", 
                    3, 10, 60, "Step or Bench", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Seated Bench Rotations", "Twist torso while seated", "Obliques", 
                    3, 10, 45, "Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Side Bends", "Gentle side movements", "Obliques", 
                    3, 10, 45, "None", fitnessLevel, "Strength"));
        } else {
            // Full Body or Cardio focus
            workout.addExercise(new Exercise("Interval Walking", "Alternate between casual and brisk pace", "Cardio", 
                    6, 2, 60, "None", fitnessLevel, "Cardio"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Nature Stretching", "Gentle stretches outdoors", "Flexibility", 
                1, 5, 0, "None", fitnessLevel, "Flexibility"));
    }

    // Helper methods for obese strength exercises
    private static void addObeseHomeStrengthExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common exercises regardless of focus area
        workout.addExercise(new Exercise("Chair Sit-to-Stand", "Use chair for support", "Lower Body, Core", 
                3, 10, 60, "Chair", fitnessLevel, "Strength"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Wall Push-ups", "Push against wall at an angle", "Chest, Triceps", 
                    3, 12, 60, "Wall", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Overhead Press", "With light dumbbells or water bottles", "Shoulders, Triceps", 
                    3, 12, 60, "Light weights", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Row with Resistance Band", "Anchor band to doorknob", "Back, Biceps", 
                    3, 12, 60, "Resistance Band", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Supported Squats", "Hold onto chair", "Quadriceps, Hamstrings, Glutes", 
                    3, 12, 60, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Leg Extensions", "Straighten leg from seated position", "Quadriceps", 
                    3, 15, 45, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Calf Raises with Support", "Hold chair for balance", "Calves", 
                    3, 15, 30, "Chair", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Seated Core Rotations", "Twist torso while seated", "Obliques", 
                    3, 10, 45, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Leg Lifts", "Lift one leg at a time", "Lower Abs", 
                    3, 10, 45, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Wall Plank", "Plank position against wall", "Core", 
                    3, 30, 45, "Wall", fitnessLevel, "Strength"));
        } else {
            // Full Body or general focus
            workout.addExercise(new Exercise("Modified Bodyweight Circuit", "Combination of seated/standing exercises", "Full Body", 
                    3, 5, 60, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Row with Band", "Pull band while standing", "Back, Biceps", 
                    3, 12, 45, "Resistance Band", fitnessLevel, "Strength"));
        }
        
        // Cooldown for all
        workout.addExercise(new Exercise("Seated Stretching", "Gentle stretches from chair", "Flexibility", 
                1, 5, 0, "Chair", fitnessLevel, "Flexibility"));
    }
    
    private static void addObeseGymStrengthExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common exercises regardless of focus area
        workout.addExercise(new Exercise("Recumbent Bike Warm-up", "5 minutes low resistance", "Cardio", 
                1, 5, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Chest Press Machine", "Controlled movement", "Chest, Triceps", 
                    3, 12, 60, "Chest Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Row Machine", "Focus on form", "Back, Biceps", 
                    3, 12, 60, "Row Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Lat Pulldown", "Wide grip, light weight", "Back, Biceps", 
                    3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Leg Press Machine", "Light to moderate weight", "Quadriceps, Hamstrings, Glutes", 
                    3, 12, 60, "Leg Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Leg Extension", "Controlled movement", "Quadriceps", 
                    3, 12, 60, "Leg Extension Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Leg Curl", "Focus on hamstrings", "Hamstrings", 
                    3, 12, 60, "Leg Curl Machine", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Seated Torso Rotation Machine", "Controlled twists", "Obliques", 
                    3, 12, 45, "Torso Rotation Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Ab Crunch Machine", "Light weight", "Abdominals", 
                    3, 12, 45, "Ab Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Cable Rotations", "Light weight", "Obliques", 
                    3, 10, 45, "Cable Machine", fitnessLevel, "Strength"));
        } else {
            // Full Body or general focus
            workout.addExercise(new Exercise("Circuit Machines", "1 set at each machine", "Full Body", 
                    1, 10, 30, "Various Machines", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Medicine Ball Exercises", "Light ball, various movements", "Full Body", 
                    3, 10, 45, "Medicine Ball", fitnessLevel, "Strength"));
        }
        
        // Cooldown for all
        workout.addExercise(new Exercise("Stretching on Mat", "Full body stretches", "Flexibility", 
                1, 5, 0, "Exercise Mat", fitnessLevel, "Flexibility"));
    }
    
    private static void addObeseOutdoorStrengthExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common exercises regardless of focus area
        workout.addExercise(new Exercise("Walking Warm-up", "5 minutes moderate pace", "Cardio", 
                1, 5, 0, "None", fitnessLevel, "Cardio"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Bench or Wall Push-ups", "Inclined push-up position", "Chest, Triceps", 
                    3, 10, 60, "Bench or Wall", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Resistance Band Rows", "Using bench and bands", "Back, Biceps", 
                    3, 12, 60, "Bench, Resistance Band", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Wall Slides", "Against a wall", "Shoulders, Upper Back", 
                    3, 10, 45, "Wall", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Park Bench Sit-to-Stand", "Using bench for support", "Quadriceps, Glutes", 
                    3, 10, 60, "Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Supported Step-ups", "Low bench or step", "Quadriceps, Glutes", 
                    3, 10, 60, "Step or Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Calf Raises with Support", "Park railing for balance", "Calves", 
                    3, 15, 30, "Railing or Support", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Seated Bench Rotations", "Twist while seated", "Obliques", 
                    3, 10, 45, "Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Side Bends", "Gentle lateral movement", "Obliques", 
                    3, 12, 45, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Supported Leg Raises", "Using bench for support", "Lower Abs", 
                    3, 8, 45, "Bench", fitnessLevel, "Strength"));
        } else {
            // Full Body or general focus
            workout.addExercise(new Exercise("Circuit Training at Park", "Use various park fixtures", "Full Body", 
                    3, 5, 60, "Park Equipment", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Walking with Resistance Bands", "Band around thighs", "Lower Body", 
                    3, 2, 60, "Resistance Bands", fitnessLevel, "Strength"));
        }
        
        // Cooldown for all
        workout.addExercise(new Exercise("Outdoor Stretching", "Use bench for support if needed", "Flexibility", 
                1, 5, 0, "Bench (optional)", fitnessLevel, "Flexibility"));
    }

    // Helper methods for obese balanced exercises
    private static void addObeseHomeBalancedExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Start with a warm-up
        workout.addExercise(new Exercise("Seated Marching", "Warm up by marching in place", "Cardio", 
                1, 2, 0, "Chair", fitnessLevel, "Cardio"));
        
        // Common exercises focusing on the specified area
        if (focusArea.equals("Upper Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Wall Push-ups", "Push against wall at angle", "Chest, Triceps", 
                    3, 10, 45, "Wall", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Bicep Curls", "Light weights or water bottles", "Biceps", 
                    3, 12, 45, "Light Weights", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Lower Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Supported Chair Squats", "Use chair for balance", "Quadriceps, Glutes", 
                    3, 10, 45, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Leg Extensions", "Extend legs while seated", "Quadriceps", 
                    3, 12, 45, "Chair", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Core") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Seated Core Rotations", "Twist torso while seated", "Obliques", 
                    3, 10, 45, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Supported Standing Side Bends", "Hold chair for support", "Obliques", 
                    3, 8, 45, "Chair", fitnessLevel, "Strength"));
        }
        
        // Add some cardio
        workout.addExercise(new Exercise("Seated Arm Circles", "Move arms in circular motion", "Shoulders, Cardio", 
                3, 30, 30, "None", fitnessLevel, "Cardio"));
        
        // Add flexibility
        workout.addExercise(new Exercise("Seated Stretching Sequence", "Gentle stretches from chair", "Flexibility", 
                1, 5, 0, "Chair", fitnessLevel, "Flexibility"));
    }
    
    private static void addObeseGymBalancedExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Start with a warm-up
        workout.addExercise(new Exercise("Recumbent Bike", "5 minutes low resistance", "Cardio", 
                1, 5, 0, "Recumbent Bike", fitnessLevel, "Cardio"));
        
        // Common exercises focusing on the specified area
        if (focusArea.equals("Upper Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Chest Press Machine", "Light weight", "Chest, Triceps", 
                    3, 12, 60, "Chest Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Row Machine", "Focus on form", "Back, Biceps", 
                    3, 12, 60, "Row Machine", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Lower Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Leg Press Machine", "Light weight", "Quadriceps, Hamstrings", 
                    3, 12, 60, "Leg Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Leg Curl", "Controlled movement", "Hamstrings", 
                    3, 12, 60, "Leg Curl Machine", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Core") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Seated Ab Crunch", "Light resistance", "Abdominals", 
                    3, 12, 45, "Ab Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Torso Rotation", "Use rotation machine", "Obliques", 
                    3, 12, 45, "Torso Machine", fitnessLevel, "Strength"));
        }
        
        // Add some cardio
        workout.addExercise(new Exercise("Stationary Bike", "5 minutes moderate resistance", "Cardio", 
                1, 5, 0, "Stationary Bike", fitnessLevel, "Cardio"));
        
        // Add flexibility
        workout.addExercise(new Exercise("Stretching with Support", "Use equipment for stability", "Flexibility", 
                1, 5, 0, "Gym Equipment", fitnessLevel, "Flexibility"));
    }
    
    private static void addObeseOutdoorBalancedExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Start with a warm-up
        workout.addExercise(new Exercise("Gentle Walking", "5 minutes easy pace", "Cardio", 
                1, 5, 0, "None", fitnessLevel, "Cardio"));
        
        // Common exercises focusing on the specified area
        if (focusArea.equals("Upper Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Wall or Bench Push-ups", "Inclined position", "Chest, Triceps", 
                    3, 10, 60, "Wall or Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Resistance Band Pulls", "Use band around sturdy object", "Back, Biceps", 
                    3, 12, 60, "Resistance Band", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Lower Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Bench Sit-to-Stand", "Use bench for support", "Quadriceps, Glutes", 
                    3, 10, 60, "Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Calf Raises with Support", "Hold onto railing", "Calves", 
                    3, 15, 30, "Railing", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Core") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Seated Bench Rotations", "Twist while seated", "Obliques", 
                    3, 10, 45, "Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Side Leans", "Lean side to side", "Obliques", 
                    3, 10, 45, "None", fitnessLevel, "Strength"));
        }
        
        // Add some cardio
        workout.addExercise(new Exercise("Walking Intervals", "Alternate between casual and faster pace", "Cardio", 
                5, 2, 30, "None", fitnessLevel, "Cardio"));
        
        // Add flexibility
        workout.addExercise(new Exercise("Outdoor Stretching", "Use bench for support as needed", "Flexibility", 
                1, 5, 0, "Bench (optional)", fitnessLevel, "Flexibility"));
    }
    
    // Helper methods for overweight home exercises
    private static void addOverweightHomeCardioExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common warm-up
        workout.addExercise(new Exercise("Marching in Place", "Moderate pace", "Cardio", 
                1, 3, 0, "None", fitnessLevel, "Cardio"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Shadow Boxing", "Punching movements", "Cardio, Upper Body", 
                    3, 60, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Modified Burpees", "Without push-up portion", "Cardio, Full Body", 
                    3, 8, 60, "None", fitnessLevel, "Cardio"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("High Knee Marching", "Lift knees high", "Lower Body, Cardio", 
                    3, 45, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Stationary Lunges", "In place lunges", "Lower Body", 
                    3, 12, 45, "None", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Standing Oblique Twists", "Twist with arms extended", "Core, Cardio", 
                    3, 30, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Modified Mountain Climbers", "Slower pace", "Core, Cardio", 
                    3, 30, 45, "None", fitnessLevel, "Cardio"));
        } else {
            // Full Body or Cardio focus
            workout.addExercise(new Exercise("Jumping Jacks", "Modified for comfort", "Cardio, Full Body", 
                    3, 45, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Step-ups", "Use bottom stair or sturdy step", "Cardio, Lower Body", 
                    3, 20, 45, "Step", fitnessLevel, "Cardio"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Walking in Place", "Slow pace", "Recovery", 
                1, 3, 0, "None", fitnessLevel, "Recovery"));
        workout.addExercise(new Exercise("Deep Breathing", "Controlled breaths", "Recovery", 
                1, 2, 0, "None", fitnessLevel, "Recovery"));
    }
    
    private static void addOverweightHomeStrengthExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common warm-up
        workout.addExercise(new Exercise("Arm Circles", "Forward and backward", "Warm-up", 
                1, 30, 0, "None", fitnessLevel, "Warm-up"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Modified Push-ups", "Knees down or against wall", "Chest, Triceps", 
                    3, 10, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Dumbbell Rows", "Use light weights", "Back, Biceps", 
                    3, 12, 60, "Light Dumbbells", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Tricep Dips", "Using sturdy chair or couch", "Triceps", 
                    3, 10, 45, "Chair", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Bodyweight Squats", "To comfortable depth", "Quadriceps, Glutes", 
                    3, 12, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Reverse Lunges", "Step back lunges", "Quadriceps, Hamstrings", 
                    3, 10, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Glute Bridges", "Lift hips", "Glutes, Hamstrings", 
                    3, 12, 45, "None", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Modified Planks", "On knees if needed", "Core", 
                    3, 30, 45, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bicycle Crunches", "Slow controlled movement", "Abs, Obliques", 
                    3, 12, 45, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Side Planks", "Modified as needed", "Obliques", 
                    3, 20, 45, "None", fitnessLevel, "Strength"));
        } else {
            // Full Body focus
            workout.addExercise(new Exercise("Circuit Training", "Rotate between exercises", "Full Body", 
                    3, 45, 60, "Light Weights", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bodyweight Exercises", "Combine squats, modified push-ups", "Full Body", 
                    3, 12, 60, "None", fitnessLevel, "Strength"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Full Body Stretch", "Gentle stretching", "Flexibility", 
                1, 5, 0, "None", fitnessLevel, "Flexibility"));
    }
    
    private static void addOverweightHomeBalancedExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Warm-up
        workout.addExercise(new Exercise("Mobility Movements", "Joint rotations", "Warm-up", 
                1, 3, 0, "None", fitnessLevel, "Warm-up"));
        
        // Strength component focusing on specified area
        if (focusArea.equals("Upper Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Modified Push-ups", "On knees or against wall", "Chest, Triceps", 
                    3, 10, 45, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Resistance Band Pulls", "Anchor band to door", "Back, Biceps", 
                    3, 12, 45, "Resistance Band", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Lower Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Bodyweight Squats", "To comfortable depth", "Quadriceps, Glutes", 
                    3, 12, 45, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Calf Raises", "Use wall for balance", "Calves", 
                    3, 15, 30, "None", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Core") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Modified Planks", "On knees if needed", "Core", 
                    3, 20, 45, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bird-dog", "Opposite arm and leg extension", "Core, Back", 
                    3, 10, 30, "None", fitnessLevel, "Strength"));
        }
        
        // Cardio component
        workout.addExercise(new Exercise("Low Impact Cardio", "Marching, side steps", "Cardio", 
                3, 60, 45, "None", fitnessLevel, "Cardio"));
        
        // Flexibility component
        workout.addExercise(new Exercise("Stretching Sequence", "Full body stretches", "Flexibility", 
                1, 5, 0, "None", fitnessLevel, "Flexibility"));
    }

    private static void addOverweightGymCardioExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common warm-up
        workout.addExercise(new Exercise("Stationary Bike", "5 minutes moderate resistance", "Cardio", 
                1, 5, 0, "Stationary Bike", fitnessLevel, "Cardio"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Rowing Machine", "Moderate resistance", "Cardio, Upper Body", 
                    3, 3, 60, "Rowing Machine", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Cable Machine Punches", "Light weight", "Cardio, Chest, Shoulders", 
                    3, 45, 45, "Cable Machine", fitnessLevel, "Cardio"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Elliptical Trainer", "Focus on lower push", "Cardio, Lower Body", 
                    3, 5, 60, "Elliptical", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Stair Climber", "Moderate pace", "Cardio, Lower Body", 
                    3, 3, 60, "Stair Machine", fitnessLevel, "Cardio"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Cross Trainer with Core Focus", "Engage core while using", "Cardio, Core", 
                    3, 4, 60, "Cross Trainer", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Medicine Ball Rotations", "Light ball", "Cardio, Core", 
                    3, 20, 45, "Medicine Ball", fitnessLevel, "Cardio"));
        } else {
            // Full Body or Cardio focus
            workout.addExercise(new Exercise("Treadmill Intervals", "Walk/jog intervals", "Cardio", 
                    10, 1, 30, "Treadmill", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Circuit Training", "Rotate between cardio machines", "Cardio", 
                    3, 5, 60, "Various Machines", fitnessLevel, "Cardio"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Gentle Cycling", "Low resistance", "Recovery", 
                1, 5, 0, "Stationary Bike", fitnessLevel, "Recovery"));
    }
    
    private static void addOverweightGymStrengthExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common warm-up
        workout.addExercise(new Exercise("Light Cardio", "5 minutes walking or cycling", "Warm-up", 
                1, 5, 0, "Cardio Machine", fitnessLevel, "Warm-up"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Chest Press Machine", "Moderate weight", "Chest, Triceps", 
                    3, 12, 60, "Chest Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Lat Pulldown", "Controlled movement", "Back, Biceps", 
                    3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Row", "Focus on squeezing shoulder blades", "Back, Biceps", 
                    3, 12, 60, "Row Machine", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Leg Press", "Moderate weight", "Quadriceps, Glutes", 
                    3, 12, 60, "Leg Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Leg Extension", "Control the movement", "Quadriceps", 
                    3, 12, 60, "Leg Extension Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Leg Curl", "Focus on hamstrings", "Hamstrings", 
                    3, 12, 60, "Leg Curl Machine", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Cable Rotations", "Controlled twists", "Obliques", 
                    3, 12, 45, "Cable Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Machine Crunches", "Controlled movement", "Abdominals", 
                    3, 15, 45, "Ab Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Plank on Stability Ball", "Hold position", "Core", 
                    3, 30, 45, "Stability Ball", fitnessLevel, "Strength"));
        } else {
            // Full Body focus
            workout.addExercise(new Exercise("Machine Circuit", "Rotate between strength machines", "Full Body", 
                    3, 12, 45, "Various Machines", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Cable Exercises", "Multi-directional movements", "Full Body", 
                    3, 12, 45, "Cable Machine", fitnessLevel, "Strength"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Stretching", "Focus on worked muscle groups", "Flexibility", 
                1, 5, 0, "Mat", fitnessLevel, "Flexibility"));
    }
    
    private static void addOverweightGymBalancedExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Warm-up
        workout.addExercise(new Exercise("Elliptical Warm-up", "5 minutes light resistance", "Warm-up", 
                1, 5, 0, "Elliptical", fitnessLevel, "Warm-up"));
        
        // Strength component focusing on specified area
        if (focusArea.equals("Upper Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Machine Chest Press", "Moderate weight", "Chest, Triceps", 
                    3, 12, 45, "Chest Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Row", "Focus on form", "Back, Biceps", 
                    3, 12, 45, "Row Machine", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Lower Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Leg Press", "Moderate weight", "Quadriceps, Glutes", 
                    3, 12, 45, "Leg Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Leg Curl", "Controlled movement", "Hamstrings", 
                    3, 12, 45, "Leg Curl Machine", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Core") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Ab Machine Crunches", "Controlled movement", "Abdominals", 
                    3, 15, 45, "Ab Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Stability Ball Exercises", "Balance and core work", "Core", 
                    3, 12, 45, "Stability Ball", fitnessLevel, "Strength"));
        }
        
        // Cardio component
        workout.addExercise(new Exercise("Interval Training", "Alternate between machines", "Cardio", 
                3, 3, 60, "Various Machines", fitnessLevel, "Cardio"));
        
        // Flexibility component
        workout.addExercise(new Exercise("Stretching Routine", "Use stretch area", "Flexibility", 
                1, 5, 0, "Stretching Area", fitnessLevel, "Flexibility"));
    }
    
    private static void addOverweightOutdoorCardioExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common warm-up
        workout.addExercise(new Exercise("Walking Warm-up", "5 minutes moderate pace", "Warm-up", 
                1, 5, 0, "None", fitnessLevel, "Warm-up"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Power Walking with Arm Movement", "Exaggerated arm swings", "Cardio, Upper Body", 
                    3, 3, 60, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Bench Push-ups", "Inclined position", "Chest, Triceps", 
                    3, 10, 45, "Park Bench", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Hill Walking", "Find gentle incline", "Cardio, Lower Body", 
                    3, 3, 60, "Hill", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Step-ups on Bench", "Alternate legs", "Lower Body", 
                    3, 12, 45, "Park Bench", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Brisk Walking with Core Engagement", "Focus on posture", "Cardio, Core", 
                    3, 3, 60, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Standing Rotations", "Twist with control", "Core", 
                    3, 15, 30, "None", fitnessLevel, "Strength"));
        } else {
            // Full Body or Cardio focus
            workout.addExercise(new Exercise("Interval Walking/Jogging", "Alternate pace", "Cardio", 
                    6, 3, 60, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Walking Lunges", "Take long strides", "Lower Body, Cardio", 
                    3, 20, 45, "None", fitnessLevel, "Cardio"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Relaxed Walking", "Slow pace", "Recovery", 
                1, 5, 0, "None", fitnessLevel, "Recovery"));
    }
    
    private static void addOverweightOutdoorStrengthExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Common warm-up
        workout.addExercise(new Exercise("Dynamic Stretches", "Arm swings, leg swings", "Warm-up", 
                1, 3, 0, "None", fitnessLevel, "Warm-up"));
                
        // Add focus area specific exercises
        if (focusArea.equals("Upper Body")) {
            workout.addExercise(new Exercise("Bench Push-ups", "Inclined position", "Chest, Triceps", 
                    3, 12, 60, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Park Bar Rows", "Use playground or park equipment", "Back, Biceps", 
                    3, 10, 60, "Park Equipment", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bench Dips", "Use park bench", "Triceps", 
                    3, 10, 45, "Park Bench", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Lower Body")) {
            workout.addExercise(new Exercise("Park Bench Squats", "Use bench as guide", "Quadriceps, Glutes", 
                    3, 12, 60, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Walking Lunges", "Use flat surface", "Quadriceps, Hamstrings", 
                    3, 20, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Step-ups", "Use park bench or stairs", "Quadriceps, Glutes", 
                    3, 12, 45, "Park Bench/Stairs", fitnessLevel, "Strength"));
        } else if (focusArea.equals("Core")) {
            workout.addExercise(new Exercise("Bench Leg Raises", "Seated on bench", "Lower Abs", 
                    3, 12, 45, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Oblique Bends", "Side bends", "Obliques", 
                    3, 12, 45, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Plank on Bench", "Elevated position", "Core", 
                    3, 30, 45, "Park Bench", fitnessLevel, "Strength"));
        } else {
            // Full Body focus
            workout.addExercise(new Exercise("Park Circuit Training", "Use various park features", "Full Body", 
                    3, 5, 60, "Park Equipment", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bodyweight Exercises", "Outdoor variations", "Full Body", 
                    3, 12, 45, "None", fitnessLevel, "Strength"));
        }
        
        // Cool down for all
        workout.addExercise(new Exercise("Static Stretching", "Hold each stretch", "Flexibility", 
                1, 5, 0, "None", fitnessLevel, "Flexibility"));
    }
    
    private static void addOverweightOutdoorBalancedExercises(Workout workout, String fitnessLevel, String focusArea) {
        // Warm-up
        workout.addExercise(new Exercise("Walking Warm-up", "5 minutes moderate pace", "Warm-up", 
                1, 5, 0, "None", fitnessLevel, "Warm-up"));
        
        // Strength component focusing on specified area
        if (focusArea.equals("Upper Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Bench Push-ups", "Inclined position", "Chest, Triceps", 
                    3, 10, 45, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Resistance Band Pulls", "Anchor to tree or post", "Back, Biceps", 
                    3, 12, 45, "Resistance Band, Tree/Post", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Lower Body") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Bench Squats", "Use bench as depth guide", "Quadriceps, Glutes", 
                    3, 12, 45, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Walking Lunges", "Use flat surface", "Quadriceps, Hamstrings", 
                    3, 10, 45, "None", fitnessLevel, "Strength"));
        }
        
        if (focusArea.equals("Core") || focusArea.equals("Full Body")) {
            workout.addExercise(new Exercise("Bench Seated Twists", "Rotate torso", "Obliques", 
                    3, 15, 30, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Modified Plank on Bench", "Inclined position", "Core", 
                    3, 30, 45, "Park Bench", fitnessLevel, "Strength"));
        }
        
        // Cardio component
        workout.addExercise(new Exercise("Interval Walking", "Alternate between casual and brisk pace", "Cardio", 
                6, 1, 30, "None", fitnessLevel, "Cardio"));
        
        // Flexibility component
        workout.addExercise(new Exercise("Outdoor Stretching Routine", "Use bench for support if needed", "Flexibility", 
                1, 5, 0, "Park Bench (optional)", fitnessLevel, "Flexibility"));
    }
} 