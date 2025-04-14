package com.example.flextrack_ianation.services;

import com.example.flextrack_ianation.models.Exercise;
import com.example.flextrack_ianation.models.Workout;
import com.example.flextrack_ianation.models.WorkoutPlan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Specialized workout generator for individuals with normal weight.
 * Focus: Balanced workouts that can be tailored to specific fitness goals.
 */
public class WorkoutGeneratorNormalWeight {
    
    public static void addMuscleBuildingWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        // Create a muscle building workout with a title specific to the focus area
        String workoutTitle = "Muscle Building";
        String muscleGroupsTarget = "Full Body";
        
        if (focusArea.equals("Upper Body")) {
            workoutTitle = "Upper Body Strength";
            muscleGroupsTarget = "Chest, Back, Shoulders, Arms";
        } else if (focusArea.equals("Lower Body")) {
            workoutTitle = "Lower Body Strength";
            muscleGroupsTarget = "Quadriceps, Hamstrings, Glutes, Calves";
        } else if (focusArea.equals("Core")) {
            workoutTitle = "Core Strength";
            muscleGroupsTarget = "Abs, Lower Back, Obliques";
        }
        
        Workout muscleWorkout = new Workout(
                workoutTitle,
                "Progressive resistance training focused on " + focusArea.toLowerCase(),
                muscleGroupsTarget,
                workoutTimeMinutes,
                fitnessLevel,
                "Strength",
                focusArea,
                environment
        );
        
        // Set creation timestamp
        muscleWorkout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add appropriate exercises based on environment and focus area
        if (environment.equals("Home")) {
            if (focusArea.equals("Upper Body")) {
                addHomeUpperBodyMuscleExercises(muscleWorkout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addHomeLowerBodyMuscleExercises(muscleWorkout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addHomeCoreExercises(muscleWorkout, fitnessLevel);
            } else {
                addHomeMuscleExercises(muscleWorkout, fitnessLevel);
            }
        } else if (environment.equals("Gym")) {
            if (focusArea.equals("Upper Body")) {
                addGymUpperBodyMuscleExercises(muscleWorkout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addGymLowerBodyMuscleExercises(muscleWorkout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addGymCoreExercises(muscleWorkout, fitnessLevel);
            } else {
                addGymMuscleExercises(muscleWorkout, fitnessLevel);
            }
        } else {
            if (focusArea.equals("Upper Body")) {
                addOutdoorUpperBodyMuscleExercises(muscleWorkout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addOutdoorLowerBodyMuscleExercises(muscleWorkout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addOutdoorCoreExercises(muscleWorkout, fitnessLevel);
            } else {
                addOutdoorMuscleExercises(muscleWorkout, fitnessLevel);
            }
        }
        
        // Estimate calories burned
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        muscleWorkout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(muscleWorkout);
    }
    
    public static void addEnduranceWorkouts(WorkoutPlan plan, int workoutDuration, String fitnessLevel, 
            String focusArea, String environment) {
        
        // Create an endurance-focused workout with title specific to focus area
        String workoutTitle = "Endurance Training";
        String muscleGroupsTarget = "Cardiovascular System, Full Body";
        
        if (focusArea.equals("Upper Body")) {
            workoutTitle = "Upper Body Endurance";
            muscleGroupsTarget = "Cardiovascular System, Chest, Back, Shoulders, Arms";
        } else if (focusArea.equals("Lower Body")) {
            workoutTitle = "Lower Body Endurance";
            muscleGroupsTarget = "Cardiovascular System, Legs, Glutes";
        } else if (focusArea.equals("Core")) {
            workoutTitle = "Core Endurance";
            muscleGroupsTarget = "Cardiovascular System, Abs, Lower Back";
        }
        
        Workout workout = new Workout(
                workoutTitle,
                "A workout designed to improve cardiovascular endurance with focus on " + focusArea.toLowerCase(),
                muscleGroupsTarget,
                workoutDuration,
                fitnessLevel,
                "Cardio",
                focusArea,
                environment
        );
        
        // Timestamp when created
        workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add exercises based on the environment and focus area
        if (environment.equals("Home")) {
            if (focusArea.equals("Upper Body")) {
                addHomeUpperBodyEnduranceExercises(workout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addHomeLowerBodyEnduranceExercises(workout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addHomeCoreEnduranceExercises(workout, fitnessLevel);
            } else {
                addHomeEnduranceExercises(workout, fitnessLevel);
            }
        } else if (environment.equals("Gym")) {
            if (focusArea.equals("Upper Body")) {
                addGymUpperBodyEnduranceExercises(workout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addGymLowerBodyEnduranceExercises(workout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addGymCoreEnduranceExercises(workout, fitnessLevel);
            } else {
                addGymEnduranceExercises(workout, fitnessLevel);
            }
        } else {
            if (focusArea.equals("Upper Body")) {
                addOutdoorUpperBodyEnduranceExercises(workout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addOutdoorLowerBodyEnduranceExercises(workout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addOutdoorCoreEnduranceExercises(workout, fitnessLevel);
            } else {
                addOutdoorEnduranceExercises(workout, fitnessLevel);
            }
        }
        
        // Estimate calories burned based on workout duration and fitness level
        int calorieEstimate = estimateCaloriesBurn(workoutDuration, fitnessLevel, "Cardio");
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        // Add the workout to the plan
        plan.addWorkout(workout);
    }
    
    public static void addBalancedWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        // Create a balanced workout with title specific to focus area
        String workoutTitle = "Balanced Fitness";
        String muscleGroupsTarget = "Full Body";
        
        if (focusArea.equals("Upper Body")) {
            workoutTitle = "Upper Body Focus Balance";
            muscleGroupsTarget = "Full Body with Upper Body emphasis";
        } else if (focusArea.equals("Lower Body")) {
            workoutTitle = "Lower Body Focus Balance";
            muscleGroupsTarget = "Full Body with Lower Body emphasis";
        } else if (focusArea.equals("Core")) {
            workoutTitle = "Core Focus Balance";
            muscleGroupsTarget = "Full Body with Core emphasis";
        }
        
        Workout balancedWorkout = new Workout(
                workoutTitle,
                "Combination of strength, cardio, and flexibility with focus on " + focusArea.toLowerCase(),
                muscleGroupsTarget,
                workoutTimeMinutes,
                fitnessLevel,
                "Mixed",
                focusArea,
                environment
        );
        
        // Set creation timestamp
        balancedWorkout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add appropriate exercises based on environment and focus area
        if (environment.equals("Home")) {
            if (focusArea.equals("Upper Body")) {
                addHomeUpperBodyBalancedExercises(balancedWorkout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addHomeLowerBodyBalancedExercises(balancedWorkout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addHomeCoreBalancedExercises(balancedWorkout, fitnessLevel);
            } else {
                addHomeBalancedExercises(balancedWorkout, fitnessLevel);
            }
        } else if (environment.equals("Gym")) {
            if (focusArea.equals("Upper Body")) {
                addGymUpperBodyBalancedExercises(balancedWorkout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addGymLowerBodyBalancedExercises(balancedWorkout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addGymCoreBalancedExercises(balancedWorkout, fitnessLevel);
            } else {
                addGymBalancedExercises(balancedWorkout, fitnessLevel);
            }
        } else {
            if (focusArea.equals("Upper Body")) {
                addOutdoorUpperBodyBalancedExercises(balancedWorkout, fitnessLevel);
            } else if (focusArea.equals("Lower Body")) {
                addOutdoorLowerBodyBalancedExercises(balancedWorkout, fitnessLevel);
            } else if (focusArea.equals("Core")) {
                addOutdoorCoreBalancedExercises(balancedWorkout, fitnessLevel);
            } else {
                addOutdoorBalancedExercises(balancedWorkout, fitnessLevel);
            }
        }
        
        // Estimate calories burned
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Mixed");
        balancedWorkout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(balancedWorkout);
    }
    
    // Helper methods for adding exercises to workouts
    private static void addHomeMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Push-ups", "3 sets of 10-15 reps", "Chest, Triceps, Shoulders", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 15-20 reps", "Quadriceps, Hamstrings, Glutes", 
                3, 20, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Dumbbell Rows", "3 sets of 12 reps per arm", "Back, Biceps", 
                3, 12, 60, "Dumbbells", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Plank", "3 sets of 30-60 seconds", "Core", 
                3, 45, 45, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Dumbbell Shoulder Press", "3 sets of 12 reps", "Shoulders, Triceps", 
                3, 12, 60, "Dumbbells", fitnessLevel, "Strength"));
    }
    
    private static void addGymMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Barbell Bench Press", "4 sets of 8-10 reps", "Chest, Triceps, Shoulders", 
                4, 10, 90, "Barbell, Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Barbell Squat", "4 sets of 8-10 reps", "Quadriceps, Hamstrings, Glutes", 
                4, 10, 90, "Squat Rack, Barbell", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Lat Pulldown", "3 sets of 10-12 reps", "Back, Biceps", 
                3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Leg Press", "3 sets of 12-15 reps", "Quadriceps, Hamstrings, Glutes", 
                3, 15, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Seated Cable Row", "3 sets of 10-12 reps", "Back, Biceps", 
                3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
    }
    
    private static void addOutdoorMuscleExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Push-ups", "3 sets of 12-15 reps", "Chest, Triceps, Shoulders", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Park Bench Dips", "3 sets of 10-12 reps", "Triceps, Shoulders", 
                3, 12, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Pull-ups on Bar or Tree Branch", "3 sets of 6-8 reps", "Back, Biceps", 
                3, 8, 60, "Pull-up bar or tree branch", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 15-20 reps", "Quadriceps, Hamstrings, Glutes", 
                3, 20, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Plank", "3 sets of 30-60 seconds", "Core", 
                3, 45, 45, "None", fitnessLevel, "Strength"));
    }
    
    private static void addHomeEnduranceExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Stationary Jogging", "3 sets of 2 minutes", "Cardio, Full Body", 
                    3, 120, 60, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("High Knees", "3 sets of 30 seconds", "Cardio, Lower Body", 
                    3, 30, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Jumping Jacks", "4 sets of 30 seconds", "Cardio, Full Body", 
                    4, 30, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Mountain Climbers", "3 sets of 30 seconds", "Cardio, Core", 
                    3, 30, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Walking in Place", "5 minutes", "Cardio, Recovery", 
                    1, 300, 0, "None", fitnessLevel, "Cardio"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("High Knees", "3 sets of 1 minute", "Cardio, Lower Body", 
                    3, 60, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Burpees", "3 sets of 10 reps", "Cardio, Full Body", 
                    3, 10, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Jumping Jacks", "3 sets of 1 minute", "Cardio, Full Body", 
                    3, 60, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Mountain Climbers", "3 sets of 45 seconds", "Cardio, Core", 
                    3, 45, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Jump Rope (or simulated)", "5 minutes continuous", "Cardio", 
                    1, 300, 0, "Jump Rope (optional)", fitnessLevel, "Cardio"));
        } else if (fitnessLevel.equals("Advanced")) {
            workout.addExercise(new Exercise("Burpees", "3 sets of 10-15 reps", "Cardio, Full Body", 
                    3, 15, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Jump Rope (or simulated)", "5 minutes continuous", "Cardio", 
                    1, 300, 0, "Jump Rope (optional)", fitnessLevel, "Cardio"));
        }
    }
    
    private static void addGymEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Treadmill Interval Run", "15 minutes (30s sprint, 90s jog)", "Cardio", 
                1, 15, 0, "Treadmill", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Rowing Machine", "10 minutes", "Cardio, Full Body", 
                1, 10, 0, "Rowing Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Elliptical Trainer", "10 minutes", "Cardio, Lower Body", 
                1, 10, 0, "Elliptical Trainer", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Battle Ropes", "3 sets of 30 seconds", "Cardio, Upper Body", 
                3, 30, 30, "Battle Ropes", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Box Jumps", "3 sets of 12 reps", "Cardio, Lower Body", 
                3, 12, 60, "Plyo Box", fitnessLevel, "Cardio"));
    }
    
    private static void addOutdoorEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Interval Sprints", "10 sets (20s sprint, 40s walk)", "Cardio", 
                10, 20, 40, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Hill Repeats", "5 sets of hill climbs", "Cardio, Lower Body", 
                5, 1, 60, "Hill", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Stair Runs", "5 sets of stair climbs", "Cardio, Lower Body", 
                5, 1, 60, "Stairs", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Jumping Jacks", "3 sets of 1 minute", "Cardio, Full Body", 
                3, 60, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("High Knees", "3 sets of 1 minute", "Cardio, Lower Body", 
                3, 60, 30, "None", fitnessLevel, "Cardio"));
    }
    
    private static void addHomeBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Jumping Jacks", "2 minutes", "Cardio, Full Body", 
                1, 120, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Push-ups", "3 sets of 10-12 reps", "Chest, Triceps, Shoulders", 
                3, 12, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 15 reps", "Quadriceps, Hamstrings, Glutes", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Plank", "3 sets of 30 seconds", "Core", 
                3, 30, 30, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Full Body Stretch Routine", "5 minutes", "Flexibility", 
                1, 300, 0, "None", fitnessLevel, "Flexibility"));
    }
    
    private static void addGymBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Treadmill Warm-up", "5 minutes light jog", "Cardio", 
                1, 5, 0, "Treadmill", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Dumbbell Bench Press", "3 sets of 10-12 reps", "Chest, Triceps", 
                3, 12, 60, "Dumbbells, Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Leg Press", "3 sets of 12-15 reps", "Lower Body", 
                3, 15, 60, "Leg Press Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Lat Pulldown", "3 sets of 10-12 reps", "Back, Biceps", 
                3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Stretching on Mat", "5 minutes", "Flexibility", 
                1, 300, 0, "Exercise Mat", fitnessLevel, "Flexibility"));
    }
    
    private static void addOutdoorBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Light Jog", "5 minutes", "Cardio", 
                1, 5, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Park Bench Push-ups", "3 sets of 10-12 reps", "Upper Body", 
                3, 12, 60, "Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 15 reps", "Lower Body", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Mountain Climbers", "3 sets of 30 seconds", "Core, Cardio", 
                3, 30, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Full Body Stretching", "5 minutes", "Flexibility", 
                1, 300, 0, "None", fitnessLevel, "Flexibility"));
    }
    
    private static void addOutdoorCoreExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Plank on Grass", "Hold position on a flat surface", "Core", 
                    3, 30, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Leg Raises on Bench", "Lie on bench, raise legs", "Lower Abs", 
                    3, 12, 60, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bench Mountain Climbers", "Hands on bench, alternate knees", "Core", 
                    3, 45, 45, "Park Bench", fitnessLevel, "Cardio"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("V-sits on Grass", "Form a V shape with body", "Core", 
                    3, 10, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Side Plank Rotations", "Rotate from side plank position", "Obliques", 
                    3, 10, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bench Leg Thrusts", "Hands on bench, thrust legs", "Core", 
                    3, 12, 45, "Park Bench", fitnessLevel, "Strength"));
        } else {
            workout.addExercise(new Exercise("Dragon Flag on Bench", "Full body control movement", "Core", 
                    3, 8, 90, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("L-sit on Parallel Bars", "Hold L position", "Core", 
                    3, 15, 60, "Parallel Bars", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Hanging Windshield Wipers", "Hang from bar, rotate legs", "Obliques", 
                    3, 10, 60, "Pull-up Bar", fitnessLevel, "Strength"));
        }
    }
    
    // Upper Body specific muscle exercises for different environments
    
    private static void addHomeUpperBodyMuscleExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Push-ups on Knees", "3 sets of 10-12 reps", "Chest, Triceps, Shoulders", 
                    3, 12, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Bicep Curls", "3 sets of 12 reps per arm", "Biceps", 
                    3, 12, 60, "Water Bottles or Light Dumbbells", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Doorway Chest Stretch", "3 sets of 30 seconds", "Chest, Shoulders", 
                    3, 30, 30, "Doorway", fitnessLevel, "Flexibility"));
            workout.addExercise(new Exercise("Shoulder Taps", "3 sets of 10 per side", "Shoulders, Core", 
                    3, 10, 45, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Wall Push-ups", "3 sets of 15 reps", "Chest, Shoulders", 
                    3, 15, 45, "Wall", fitnessLevel, "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Standard Push-ups", "3 sets of 12-15 reps", "Chest, Triceps, Shoulders", 
                    3, 15, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Tricep Dips on Chair", "3 sets of 12 reps", "Triceps, Shoulders", 
                    3, 12, 60, "Chair", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Dumbbell Rows", "3 sets of 12 reps per arm", "Back, Biceps", 
                    3, 12, 60, "Dumbbells", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Dumbbell Shoulder Press", "3 sets of 12 reps", "Shoulders, Triceps", 
                    3, 12, 60, "Dumbbells", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Incline Push-ups", "3 sets of 12 reps", "Upper Chest, Shoulders", 
                    3, 12, 60, "Chair or Stairs", fitnessLevel, "Strength"));
        } else { // Advanced
            workout.addExercise(new Exercise("Diamond Push-ups", "4 sets of 12-15 reps", "Chest, Triceps", 
                    4, 15, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Pike Push-ups", "3 sets of 12 reps", "Shoulders, Triceps", 
                    3, 12, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Decline Push-ups", "3 sets of 12 reps", "Upper Chest, Shoulders", 
                    3, 12, 60, "Chair or Couch", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Heavy Dumbbell Rows", "4 sets of 10 reps per arm", "Back, Biceps", 
                    4, 10, 60, "Heavy Dumbbells", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Pseudo Planche Push-ups", "3 sets of 8-10 reps", "Chest, Shoulders, Core", 
                    3, 10, 90, "None", fitnessLevel, "Strength"));
        }
    }
    
    private static void addGymUpperBodyMuscleExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Chest Press Machine", "3 sets of 12-15 reps", "Chest, Triceps", 
                    3, 15, 60, "Chest Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Lat Pulldown", "3 sets of 12-15 reps", "Back, Biceps", 
                    3, 15, 60, "Cable Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Row Machine", "3 sets of 12-15 reps", "Back, Biceps", 
                    3, 15, 60, "Row Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Shoulder Press Machine", "3 sets of 12-15 reps", "Shoulders, Triceps", 
                    3, 15, 60, "Shoulder Press Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Cable Bicep Curls", "3 sets of 12-15 reps", "Biceps", 
                    3, 15, 60, "Cable Machine", fitnessLevel, "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Barbell Bench Press", "4 sets of 8-10 reps", "Chest, Triceps, Shoulders", 
                    4, 10, 90, "Barbell, Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Cable Flyes", "3 sets of 12 reps", "Chest, Shoulders", 
                    3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bent Over Barbell Rows", "3 sets of 10 reps", "Back, Biceps", 
                    3, 10, 60, "Barbell", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Seated Dumbbell Shoulder Press", "3 sets of 10-12 reps", "Shoulders, Triceps", 
                    3, 12, 60, "Dumbbells, Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Tricep Pushdowns", "3 sets of 12 reps", "Triceps", 
                    3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
        } else { // Advanced
            workout.addExercise(new Exercise("Incline Barbell Bench Press", "4 sets of 8-10 reps", "Upper Chest, Shoulders, Triceps", 
                    4, 10, 90, "Barbell, Incline Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Weighted Pull-ups", "4 sets of 8-10 reps", "Back, Biceps", 
                    4, 10, 90, "Pull-up Bar, Weight Belt", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Barbell Overhead Press", "4 sets of 8-10 reps", "Shoulders, Triceps", 
                    4, 10, 90, "Barbell", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Cable Crossovers", "3 sets of 12 reps", "Chest, Shoulders", 
                    3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("EZ Bar Skull Crushers", "3 sets of 10-12 reps", "Triceps", 
                    3, 12, 60, "EZ Bar, Bench", fitnessLevel, "Strength"));
        }
    }
    
    private static void addOutdoorUpperBodyMuscleExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Incline Push-ups on Bench", "3 sets of 12-15 reps", "Chest, Triceps, Shoulders", 
                    3, 15, 60, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bench Dips", "3 sets of 12 reps", "Triceps, Shoulders", 
                    3, 12, 60, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Australian Pull-ups", "3 sets of 10 reps", "Back, Biceps", 
                    3, 10, 60, "Horizontal Bar or Railing", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Pike Push-ups", "3 sets of 10 reps", "Shoulders, Triceps", 
                    3, 10, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Standing Arm Circles", "3 sets of 30 seconds each direction", "Shoulders", 
                    3, 30, 30, "None", fitnessLevel, "Endurance"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Standard Push-ups", "3 sets of 15-20 reps", "Chest, Triceps, Shoulders", 
                    3, 20, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Pull-ups on Bar", "3 sets of 8-10 reps", "Back, Biceps", 
                    3, 10, 60, "Pull-up Bar", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bench or Step Decline Push-ups", "3 sets of 12-15 reps", "Upper Chest, Shoulders", 
                    3, 15, 60, "Bench or Step", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Park Bench Tricep Dips", "3 sets of 12-15 reps", "Triceps, Shoulders", 
                    3, 15, 60, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Handstand Hold Against Tree", "3 sets of 20-30 seconds", "Shoulders, Core", 
                    3, 30, 60, "Tree", fitnessLevel, "Strength"));
        } else { // Advanced
            workout.addExercise(new Exercise("Diamond Push-ups", "4 sets of 15-20 reps", "Chest, Triceps", 
                    4, 20, 60, "None", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Wide Grip Pull-ups", "4 sets of 10-12 reps", "Back, Biceps", 
                    4, 12, 60, "Pull-up Bar", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Bench Decline Push-ups with Feet Elevated", "3 sets of 15 reps", "Upper Chest, Shoulders", 
                    3, 15, 60, "Park Bench", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Parallel Bar Dips", "3 sets of 12-15 reps", "Chest, Triceps, Shoulders", 
                    3, 15, 60, "Parallel Bars", fitnessLevel, "Strength"));
            workout.addExercise(new Exercise("Muscle-ups", "3 sets of 5-8 reps", "Full Upper Body", 
                    3, 8, 90, "Pull-up Bar", fitnessLevel, "Strength"));
        }
    }
    
    // Upper Body specific endurance exercises for different environments

    private static void addHomeUpperBodyEnduranceExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Arm Punches", "3 sets of 1 minute", "Shoulders, Arms", 
                    3, 60, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Wall Push-ups", "3 sets of 15-20 reps", "Chest, Triceps", 
                    3, 20, 30, "Wall", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Standing Shoulder Taps", "3 sets of 45 seconds", "Shoulders, Core", 
                    3, 45, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Arm Circles", "3 sets of 30 seconds each direction", "Shoulders", 
                    3, 30, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Modified Jumping Jacks (Arms Only)", "3 sets of 45 seconds", "Shoulders, Arms", 
                    3, 45, 30, "None", fitnessLevel, "Cardio"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Push-up to Plank Jacks", "3 sets of 45 seconds", "Chest, Arms, Core", 
                    3, 45, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Shadow Boxing", "3 sets of 1 minute", "Shoulders, Arms", 
                    3, 60, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Tricep Dips with Leg Extensions", "3 sets of 12 reps", "Triceps, Core", 
                    3, 12, 45, "Chair", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Push-up Shoulder Taps", "3 sets of 40 seconds", "Chest, Shoulders, Core", 
                    3, 40, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Dumbbell Punches", "3 sets of 1 minute", "Shoulders, Arms", 
                    3, 60, 30, "Light Dumbbells", fitnessLevel, "Cardio"));
        } else { // Advanced
            workout.addExercise(new Exercise("Burpee to Push-up", "3 sets of 45 seconds", "Full Body, Upper Body Focus", 
                    3, 45, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Plyo Push-ups", "3 sets of 10-12 reps", "Chest, Triceps, Shoulders", 
                    3, 12, 60, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Mountain Climber to Push-up", "3 sets of 45 seconds", "Core, Chest, Shoulders", 
                    3, 45, 45, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Dumbbell Thrusters", "3 sets of 12-15 reps", "Full Body, Shoulders", 
                    3, 15, 60, "Dumbbells", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("High Intensity Shadow Boxing", "3 sets of 1 minute", "Shoulders, Arms", 
                    3, 60, 30, "None", fitnessLevel, "Cardio"));
        }
    }
    
    private static void addGymUpperBodyEnduranceExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Arm Ergometer", "3 sets of 3 minutes", "Arms, Shoulders", 
                    3, 180, 60, "Arm Ergometer", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Cable Machine Punches", "3 sets of 1 minute", "Chest, Shoulders", 
                    3, 60, 45, "Cable Machine", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Light Battle Ropes", "3 sets of 30 seconds", "Arms, Shoulders", 
                    3, 30, 45, "Battle Ropes", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Seated Shoulder Press", "3 sets of 15 reps", "Shoulders", 
                    3, 15, 45, "Light Dumbbells", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("TRX Y-Raises", "3 sets of 12 reps", "Upper Back, Shoulders", 
                    3, 12, 45, "TRX Straps", fitnessLevel, "Cardio"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Rowing Machine Intervals", "5 sets (1 min hard, 30s easy)", "Back, Arms", 
                    5, 60, 30, "Rowing Machine", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Battle Ropes", "4 sets of 40 seconds", "Arms, Shoulders", 
                    4, 40, 40, "Battle Ropes", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Medicine Ball Slams", "3 sets of 15 reps", "Full Upper Body", 
                    3, 15, 45, "Medicine Ball", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Kettlebell Swings", "3 sets of 15 reps", "Shoulders, Back, Core", 
                    3, 15, 45, "Kettlebell", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Cable Machine Rotations", "3 sets of 12 per side", "Core, Shoulders", 
                    3, 12, 45, "Cable Machine", fitnessLevel, "Cardio"));
        } else { // Advanced
            workout.addExercise(new Exercise("Assault Bike Intervals", "5 sets (30s max, 30s rest)", "Full Upper Body", 
                    5, 30, 30, "Assault Bike", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Battle Rope Complex", "3 sets of 1 minute", "Arms, Shoulders, Core", 
                    3, 60, 45, "Battle Ropes", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Burpee to Pull-up", "3 sets of 10 reps", "Full Body, Upper Focus", 
                    3, 10, 60, "Pull-up Bar", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Box Push-ups", "3 sets of 15 reps", "Chest, Triceps", 
                    3, 15, 45, "Plyo Box", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Barbell Complex (Clean, Press, Row)", "3 sets of 8 each", "Full Upper Body", 
                    3, 8, 90, "Barbell", fitnessLevel, "Cardio"));
        }
    }
    
    private static void addOutdoorUpperBodyEnduranceExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Arm Swings While Walking", "5 minutes", "Shoulders, Cardio", 
                    1, 300, 0, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Bench Dips", "3 sets of 12 reps", "Triceps, Shoulders", 
                    3, 12, 45, "Park Bench", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Wall or Tree Push-ups", "3 sets of 12 reps", "Chest, Triceps", 
                    3, 12, 45, "Wall or Tree", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Arm Circles", "3 sets of 30 seconds each direction", "Shoulders", 
                    3, 30, 30, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Bench Step-ups with Arm Raise", "3 sets of 10 per side", "Full Body, Shoulders", 
                    3, 10, 45, "Park Bench", fitnessLevel, "Cardio"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Park Bench Push-up Intervals", "4 sets of 45 seconds", "Chest, Triceps", 
                    4, 45, 45, "Park Bench", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Pull-up Bar Hangs with Leg Raises", "3 sets of 30 seconds", "Back, Core", 
                    3, 30, 45, "Pull-up Bar", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Incline Mountain Climbers", "3 sets of 45 seconds", "Shoulders, Core", 
                    3, 45, 45, "Park Bench", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Bench Hop-overs with Push-up", "3 sets of 10 reps", "Full Body, Chest", 
                    3, 10, 60, "Park Bench", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Tricep Dips with Leg Extensions", "3 sets of 12 reps", "Triceps, Core", 
                    3, 12, 45, "Park Bench", fitnessLevel, "Cardio"));
        } else { // Advanced
            workout.addExercise(new Exercise("Explosive Push-ups", "4 sets of 12 reps", "Chest, Triceps, Shoulders", 
                    4, 12, 60, "None", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Pull-up Bar Muscle-up Attempts", "3 sets of 5-8 reps", "Full Upper Body", 
                    3, 8, 90, "Pull-up Bar", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Burpee Pull-ups", "3 sets of 8 reps", "Full Body, Upper Focus", 
                    3, 8, 60, "Pull-up Bar", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Handstand Push-up Practice", "3 sets of 5-8 reps", "Shoulders, Core", 
                    3, 8, 60, "Tree or Wall", fitnessLevel, "Cardio"));
            workout.addExercise(new Exercise("Bench Jump to Decline Push-up", "3 sets of 10 reps", "Full Body, Chest", 
                    3, 10, 60, "Park Bench", fitnessLevel, "Cardio"));
        }
    }
    
    // Upper Body specific balanced exercises for different environments
    
    private static void addHomeUpperBodyBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Arm Circles", "3 sets of 30 seconds each direction", "Shoulders", 
                3, 30, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Push-ups", "3 sets of 10-15 reps", "Chest, Triceps, Shoulders", 
                3, 15, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Dumbbell Rows", "3 sets of 12 reps per arm", "Back, Biceps", 
                3, 12, 60, "Dumbbells", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Shoulder Taps", "3 sets of 45 seconds", "Shoulders, Core", 
                3, 45, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Upper Body Stretches", "5 minutes", "Chest, Back, Shoulders", 
                1, 300, 0, "None", fitnessLevel, "Flexibility"));
    }
    
    private static void addGymUpperBodyBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Rowing Machine", "5 minutes moderate pace", "Back, Arms", 
                1, 300, 0, "Rowing Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Dumbbell Bench Press", "3 sets of 10-12 reps", "Chest, Triceps", 
                3, 12, 60, "Dumbbells, Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Lat Pulldown", "3 sets of 10-12 reps", "Back, Biceps", 
                3, 12, 60, "Cable Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Shoulder Press", "3 sets of 10-12 reps", "Shoulders, Triceps", 
                3, 12, 60, "Dumbbells", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Upper Body Stretch Routine", "5 minutes", "Chest, Back, Shoulders", 
                1, 300, 0, "Exercise Mat", fitnessLevel, "Flexibility"));
    }
    
    private static void addOutdoorUpperBodyBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Light Jog with Arm Movements", "5 minutes", "Cardio, Shoulders", 
                1, 300, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Park Bench Push-ups", "3 sets of 10-15 reps", "Chest, Triceps", 
                3, 15, 60, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Inverted Rows on Bar", "3 sets of 10-12 reps", "Back, Biceps", 
                3, 12, 60, "Horizontal Bar", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Tricep Dips on Bench", "3 sets of 10-12 reps", "Triceps, Shoulders", 
                3, 12, 60, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Outdoor Upper Body Stretches", "5 minutes", "Chest, Back, Shoulders", 
                1, 300, 0, "None", fitnessLevel, "Flexibility"));
    }
    
    // Lower Body specific exercises placeholders
    
    private static void addHomeLowerBodyMuscleExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular home muscle exercises
        addHomeMuscleExercises(workout, fitnessLevel);
    }
    
    private static void addGymLowerBodyMuscleExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular gym muscle exercises
        addGymMuscleExercises(workout, fitnessLevel);
    }
    
    private static void addOutdoorLowerBodyMuscleExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular outdoor muscle exercises
        addOutdoorMuscleExercises(workout, fitnessLevel);
    }
    
    private static void addHomeLowerBodyEnduranceExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular home endurance exercises
        addHomeEnduranceExercises(workout, fitnessLevel);
    }
    
    private static void addGymLowerBodyEnduranceExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular gym endurance exercises
        addGymEnduranceExercises(workout, fitnessLevel);
    }
    
    private static void addOutdoorLowerBodyEnduranceExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular outdoor endurance exercises
        addOutdoorEnduranceExercises(workout, fitnessLevel);
    }
    
    private static void addHomeLowerBodyBalancedExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular home balanced exercises
        addHomeBalancedExercises(workout, fitnessLevel);
    }
    
    private static void addGymLowerBodyBalancedExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular gym balanced exercises
        addGymBalancedExercises(workout, fitnessLevel);
    }
    
    private static void addOutdoorLowerBodyBalancedExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular outdoor balanced exercises
        addOutdoorBalancedExercises(workout, fitnessLevel);
    }
    
    // Core specific exercises placeholders
    
    private static void addHomeCoreExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular home muscle exercises
        addHomeMuscleExercises(workout, fitnessLevel);
    }
    
    private static void addGymCoreExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular gym muscle exercises
        addGymMuscleExercises(workout, fitnessLevel);
    }
    
    private static void addHomeCoreEnduranceExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular home endurance exercises
        addHomeEnduranceExercises(workout, fitnessLevel);
    }
    
    private static void addGymCoreEnduranceExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular gym endurance exercises
        addGymEnduranceExercises(workout, fitnessLevel);
    }
    
    private static void addOutdoorCoreEnduranceExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular outdoor endurance exercises
        addOutdoorEnduranceExercises(workout, fitnessLevel);
    }
    
    private static void addHomeCoreBalancedExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular home balanced exercises
        addHomeBalancedExercises(workout, fitnessLevel);
    }
    
    private static void addGymCoreBalancedExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular gym balanced exercises
        addGymBalancedExercises(workout, fitnessLevel);
    }
    
    private static void addOutdoorCoreBalancedExercises(Workout workout, String fitnessLevel) {
        // For now, use the regular outdoor balanced exercises
        addOutdoorBalancedExercises(workout, fitnessLevel);
    }
    
    // Helper method to estimate calories burned
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
} 