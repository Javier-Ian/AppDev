package com.example.flextrack_ianation.services;

import com.example.flextrack_ianation.models.Exercise;
import com.example.flextrack_ianation.models.Workout;
import com.example.flextrack_ianation.models.WorkoutPlan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Specialized workout generator for underweight individuals.
 * Focus: Muscle building, higher calorie burn, more reps
 */
public class WorkoutGeneratorUnderweight {

    public static void addWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
                                  String fitnessLevel, String focusArea, String environment) {
        
        // For underweight individuals, we focus on muscle building exercises
        // with progressive overload and adequate recovery
        
        // Create different workouts for each training day of the week
        addPushWorkout(plan, workoutTimeMinutes, fitnessLevel, environment);
        addPullWorkout(plan, workoutTimeMinutes, fitnessLevel, environment);
        addLegWorkout(plan, workoutTimeMinutes, fitnessLevel, environment);
        
        if (plan.getWorkoutsPerWeek() > 3) {
            // If more than 3 days are available, add more specialized workouts
            if (focusArea.equals("Upper Body") || focusArea.equals("Full Body")) {
                addUpperBodyWorkout(plan, workoutTimeMinutes, fitnessLevel, environment);
            }
            
            if (focusArea.equals("Lower Body") || focusArea.equals("Full Body")) {
                addLowerBodyWorkout(plan, workoutTimeMinutes, fitnessLevel, environment);
            }
            
            if (focusArea.equals("Core") || focusArea.equals("Full Body")) {
                addCoreWorkout(plan, workoutTimeMinutes, fitnessLevel, environment);
            }
        }
    }
    
    private static void addPushWorkout(WorkoutPlan plan, int workoutTimeMinutes, 
                                     String fitnessLevel, String environment) {
        Workout workout = new Workout(
                "Push Day",
                "Focus on chest, shoulders, and triceps with compound pushing movements",
                "Chest, Shoulders, Triceps",
                workoutTimeMinutes,
                fitnessLevel,
                "Upper Body",
                "Upper Body",
                environment
        );
        
        // Set creation timestamp
        workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add exercises based on fitness level and environment
        if (environment.equals("Home")) {
            addHomePushExercises(workout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymPushExercises(workout, fitnessLevel);
        } else {
            addOutdoorPushExercises(workout, fitnessLevel);
        }
        
        // Estimate calories burned based on workout duration and intensity
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(workout);
    }
    
    private static void addPullWorkout(WorkoutPlan plan, int workoutTimeMinutes, 
                                     String fitnessLevel, String environment) {
        Workout workout = new Workout(
                "Pull Day",
                "Focus on back and biceps with compound pulling movements",
                "Back, Biceps",
                workoutTimeMinutes,
                fitnessLevel,
                "Upper Body",
                "Upper Body",
                environment
        );
        
        // Set creation timestamp
        workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add exercises based on fitness level and environment
        if (environment.equals("Home")) {
            addHomePullExercises(workout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymPullExercises(workout, fitnessLevel);
        } else {
            addOutdoorPullExercises(workout, fitnessLevel);
        }
        
        // Estimate calories burned based on workout duration and intensity
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(workout);
    }
    
    private static void addLegWorkout(WorkoutPlan plan, int workoutTimeMinutes, 
                                    String fitnessLevel, String environment) {
        Workout workout = new Workout(
                "Leg Day",
                "Focus on building lower body strength and muscle with compound movements",
                "Quadriceps, Hamstrings, Glutes, Calves",
                workoutTimeMinutes,
                fitnessLevel,
                "Lower Body",
                "Lower Body",
                environment
        );
        
        // Set creation timestamp
        workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add exercises based on fitness level and environment
        if (environment.equals("Home")) {
            addHomeLegExercises(workout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymLegExercises(workout, fitnessLevel);
        } else {
            addOutdoorLegExercises(workout, fitnessLevel);
        }
        
        // Estimate calories burned based on workout duration and intensity
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(workout);
    }
    
    private static void addUpperBodyWorkout(WorkoutPlan plan, int workoutTimeMinutes, 
                                          String fitnessLevel, String environment) {
        Workout workout = new Workout(
                "Upper Body Focus",
                "Balanced workout targeting all major upper body muscle groups",
                "Chest, Back, Shoulders, Arms",
                workoutTimeMinutes,
                fitnessLevel,
                "Upper Body",
                "Upper Body",
                environment
        );
        
        // Set creation timestamp
        workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add exercises based on fitness level and environment
        if (environment.equals("Home")) {
            addHomeUpperBodyExercises(workout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymUpperBodyExercises(workout, fitnessLevel);
        } else {
            addOutdoorUpperBodyExercises(workout, fitnessLevel);
        }
        
        // Estimate calories burned based on workout duration and intensity
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(workout);
    }
    
    private static void addLowerBodyWorkout(WorkoutPlan plan, int workoutTimeMinutes, 
                                          String fitnessLevel, String environment) {
        Workout workout = new Workout(
                "Lower Body Focus",
                "Comprehensive lower body workout to build strength and size",
                "Quadriceps, Hamstrings, Glutes, Calves",
                workoutTimeMinutes,
                fitnessLevel,
                "Lower Body",
                "Lower Body",
                environment
        );
        
        // Set creation timestamp
        workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add exercises based on fitness level and environment
        if (environment.equals("Home")) {
            addHomeLowerBodyExercises(workout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymLowerBodyExercises(workout, fitnessLevel);
        } else {
            addOutdoorLowerBodyExercises(workout, fitnessLevel);
        }
        
        // Estimate calories burned based on workout duration and intensity
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(workout);
    }
    
    private static void addCoreWorkout(WorkoutPlan plan, int workoutTimeMinutes, 
                                     String fitnessLevel, String environment) {
        Workout workout = new Workout(
                "Core Strength",
                "Targeted core workout to build a strong foundation",
                "Abs, Obliques, Lower Back",
                workoutTimeMinutes,
                fitnessLevel,
                "Core",
                "Core",
                environment
        );
        
        // Set creation timestamp
        workout.setCreatedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        
        // Add exercises based on fitness level and environment
        if (environment.equals("Home")) {
            addHomeCoreExercises(workout, fitnessLevel);
        } else if (environment.equals("Gym")) {
            addGymCoreExercises(workout, fitnessLevel);
        } else {
            addOutdoorCoreExercises(workout, fitnessLevel);
        }
        
        // Estimate calories burned based on workout duration and intensity
        int calorieEstimate = estimateCaloriesBurn(workoutTimeMinutes, fitnessLevel, "Strength");
        workout.setCaloriesBurnEstimate(calorieEstimate);
        
        plan.addWorkout(workout);
    }
    
    // Helper method to estimate calories burned
    private static int estimateCaloriesBurn(int durationMinutes, String fitnessLevel, String exerciseType) {
        // Base calories burned per minute for different exercise types
        int baseCaloriesPerMinute;
        
        // Different base rates for different exercise types - higher for underweight individuals
        // Underweight individuals typically burn more calories due to higher metabolism
        switch (exerciseType) {
            case "Cardio":
                baseCaloriesPerMinute = 12; // Higher than standard
                break;
            case "Strength":
                baseCaloriesPerMinute = 10; // Higher for muscle building focus
                break;
            case "Mixed":
                baseCaloriesPerMinute = 11;
                break;
            case "Recovery":
                baseCaloriesPerMinute = 6;
                break;
            case "Plyometric":
                baseCaloriesPerMinute = 14; // High intensity
                break;
            default:
                baseCaloriesPerMinute = 9;
        }
        
        // Adjust based on fitness level
        double levelMultiplier = 1.0;
        switch (fitnessLevel) {
            case "Beginner":
                levelMultiplier = 0.9; // Slightly higher than standard (0.8) for underweight
                break;
            case "Intermediate":
                levelMultiplier = 1.1; // Slightly higher than standard (1.0) for underweight
                break;
            case "Advanced":
                levelMultiplier = 1.3; // Slightly higher than standard (1.2) for underweight
                break;
        }
        
        // Calculate total calories
        return (int) (baseCaloriesPerMinute * durationMinutes * levelMultiplier);
    }
    
    // Example implementations of exercise adders - these would be filled with actual exercises
    private static void addHomePushExercises(Workout workout, String fitnessLevel) {
        // Push-up variations
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Push-ups", "Start on knees if needed", "Chest, Shoulders, Triceps", 3, 10, 60, "None", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Incline Push-ups", "Hands elevated on stable surface", "Upper Chest, Shoulders", 3, 12, 60, "Bench or chair", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Bench Dips", "Using a chair or bench", "Triceps", 3, 10, 60, "Chair or bench", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Standard Push-ups", "Keep body straight", "Chest, Shoulders, Triceps", 4, 15, 45, "None", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Decline Push-ups", "Feet elevated on stable surface", "Upper Chest, Shoulders", 3, 12, 45, "Chair or bench", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Diamond Push-ups", "Hand position close together", "Triceps, Chest", 3, 12, 45, "None", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Plyometric Push-ups", "Push up with force to lift hands", "Chest, Shoulders, Triceps", 4, 12, 45, "None", "Advanced", "Strength"));
            workout.addExercise(new Exercise("One-arm Push-up Progression", "Work toward one-arm push-up", "Chest, Shoulders, Core", 3, 8, 60, "None", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Handstand Push-up Progression", "Against wall", "Shoulders, Triceps", 3, 8, 60, "Wall", "Advanced", "Strength"));
        }
    }
    
    private static void addHomePullExercises(Workout workout, String fitnessLevel) {
        // Example exercises for home pull workout
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Doorway Rows", "Use a towel in a doorway", "Upper Back", 3, 12, 60, "Towel, doorway", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Towel Bicep Curls", "Use a towel with resistance", "Biceps", 3, 12, 60, "Towel", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Superman Hold", "Lying on stomach", "Lower Back", 3, 30, 45, "None", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Table Rows", "Using a sturdy table", "Back", 4, 12, 45, "Table", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Backpack Rows", "Fill backpack with books", "Back", 4, 10, 45, "Backpack with weight", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Backpack Curls", "Weighted backpack curls", "Biceps", 3, 12, 45, "Backpack with weight", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Bedsheet Rows", "Sheets anchored in door", "Back", 4, 12, 45, "Bedsheet, door", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Towel Pull-up Substitutes", "Horizontal pulling", "Back, Biceps", 4, 10, 60, "Towel, table", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Isometric Doorway Pulls", "Pull against immovable object", "Back, Biceps", 4, 30, 45, "Doorway", "Advanced", "Strength"));
        }
    }
    
    private static void addHomeLegExercises(Workout workout, String fitnessLevel) {
        // Example exercises for home leg workout
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Bodyweight Squats", "Focus on form", "Quadriceps, Glutes", 3, 15, 60, "None", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Glute Bridges", "Lying on back", "Glutes, Hamstrings", 3, 15, 45, "None", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Calf Raises", "Using stairs or flat ground", "Calves", 3, 20, 30, "Step (optional)", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Split Squats", "Lunge position", "Quadriceps, Glutes", 3, 12, 45, "Chair (optional)", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Single Leg Glute Bridges", "One leg extended", "Glutes, Hamstrings", 3, 12, 45, "None", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Walking Lunges", "Forward movement", "Quadriceps, Glutes", 3, 20, 45, "None", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Pistol Squat Progression", "Work toward single leg squats", "Quadriceps, Glutes, Balance", 4, 8, 60, "None", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Bulgarian Split Squats", "Rear foot elevated", "Quadriceps, Glutes", 4, 10, 60, "Chair or bench", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Single Leg Deadlift", "Focus on hip hinge", "Hamstrings, Glutes, Balance", 3, 10, 45, "Backpack (optional)", "Advanced", "Strength"));
        }
    }
    
    // Other exercise methods would be implemented similarly...
    private static void addGymPushExercises(Workout workout, String fitnessLevel) {
        // Implementation for gym push exercises
    }
    
    private static void addGymPullExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Lat Pulldown", "Pull bar to chest", "Back, Biceps", 3, 12, 60, "Lat Pulldown Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Seated Row", "Pull handles to waist", "Back, Biceps", 3, 12, 60, "Cable Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Face Pull", "Pull rope to face", "Upper Back, Rear Delts", 3, 15, 60, "Cable Machine", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Pull-ups", "Pull body up to bar", "Back, Biceps", 3, 8, 60, "Pull-up Bar", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Bent Over Row", "Bend at waist, pull bar to waist", "Back, Biceps", 4, 10, 60, "Barbell", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Cable Row", "Pull handles to waist", "Back, Biceps", 3, 12, 45, "Cable Machine", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Weighted Pull-ups", "Add weight to pull-ups", "Back, Biceps", 4, 6, 90, "Pull-up Bar, Weight Belt", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Pendlay Row", "Explosive row from floor", "Back, Biceps", 4, 6, 90, "Barbell", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Single-arm Cable Row", "One arm at a time", "Back, Biceps", 3, 10, 45, "Cable Machine", "Advanced", "Strength"));
        }
    }
    
    private static void addGymLegExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Leg Press", "Push platform away", "Quadriceps, Glutes", 3, 12, 60, "Leg Press Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Leg Extension", "Extend legs against resistance", "Quadriceps", 3, 12, 60, "Leg Extension Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Leg Curl", "Curl legs against resistance", "Hamstrings", 3, 12, 60, "Leg Curl Machine", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Squats", "Lower body until thighs parallel", "Quadriceps, Glutes", 4, 10, 90, "Barbell", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Romanian Deadlift", "Hinge at hips, lower bar", "Hamstrings, Glutes", 3, 10, 90, "Barbell", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Walking Lunges", "Step forward and lower", "Quadriceps, Glutes", 3, 12, 60, "Dumbbells", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Front Squats", "Bar on front of shoulders", "Quadriceps, Core", 4, 6, 120, "Barbell", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Bulgarian Split Squat", "One foot elevated", "Quadriceps, Glutes", 3, 8, 60, "Dumbbells", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Nordic Hamstring Curl", "Lower body with control", "Hamstrings", 3, 8, 60, "Partner or Machine", "Advanced", "Strength"));
        }
    }
    
    private static void addOutdoorPushExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Park Bench Push-ups", "Using park bench", "Chest, Shoulders", 3, 10, 60, "Park Bench", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Tree Trunk Press", "Push against tree trunk", "Chest, Shoulders", 3, 12, 60, "Tree Trunk", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Dips", "Using parallel bars", "Chest, Triceps", 3, 8, 60, "Parallel Bars", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Decline Push-ups", "Feet on bench", "Chest, Shoulders", 4, 12, 45, "Park Bench", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Plyometric Push-ups", "Explosive push-ups", "Chest, Shoulders", 3, 10, 60, "Ground", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Handstand Push-up Progression", "Against tree", "Shoulders", 3, 8, 60, "Tree", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("One-arm Push-up Progression", "Work toward one-arm", "Chest, Shoulders", 4, 6, 90, "Ground", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Clapping Push-ups", "Explosive with clap", "Chest, Shoulders", 3, 8, 60, "Ground", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Handstand Push-ups", "Against wall", "Shoulders", 3, 6, 90, "Wall", "Advanced", "Strength"));
        }
    }
    
    private static void addOutdoorPullExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Tree Branch Pull-ups", "Using low branch", "Back, Biceps", 3, 6, 60, "Tree Branch", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Inverted Row", "Under bar or branch", "Back, Biceps", 3, 10, 60, "Bar or Branch", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Australian Pull-ups", "Under bar or branch", "Back, Biceps", 3, 12, 60, "Bar or Branch", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Pull-ups", "Using bar or branch", "Back, Biceps", 4, 8, 60, "Bar or Branch", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Chin-ups", "Palms facing you", "Back, Biceps", 3, 8, 60, "Bar or Branch", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("L-sit Pull-ups", "Legs straight out", "Back, Core", 3, 6, 60, "Bar or Branch", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Muscle-ups", "Pull over bar", "Back, Shoulders", 3, 5, 90, "Bar", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Archer Pull-ups", "One arm straight", "Back, Biceps", 3, 6, 60, "Bar", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Typewriter Pull-ups", "Move side to side", "Back, Core", 3, 6, 60, "Bar", "Advanced", "Strength"));
        }
    }
    
    private static void addOutdoorLegExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Step-ups", "Using park bench", "Quadriceps, Glutes", 3, 12, 60, "Park Bench", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Lunges", "Forward stepping", "Quadriceps, Glutes", 3, 10, 60, "None", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Calf Raises", "On step or curb", "Calves", 3, 15, 60, "Step or Curb", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Pistol Squat Progression", "Single leg squat", "Quadriceps, Glutes", 3, 8, 60, "None", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Jump Squats", "Explosive squat jumps", "Quadriceps, Glutes", 3, 12, 60, "None", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Hill Sprints", "Sprint up hill", "Quadriceps, Glutes", 5, 20, 120, "Hill", "Intermediate", "Cardio"));
        } else {
            workout.addExercise(new Exercise("Pistol Squats", "Full single leg squat", "Quadriceps, Glutes", 3, 6, 90, "None", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Box Jumps", "Jump onto bench", "Quadriceps, Glutes", 3, 8, 60, "Park Bench", "Advanced", "Plyometric"));
            workout.addExercise(new Exercise("Nordic Hamstring Curl", "Using partner", "Hamstrings", 3, 6, 90, "Partner", "Advanced", "Strength"));
        }
    }
    
    private static void addHomeUpperBodyExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Wall Push-ups", "Against wall", "Chest, Shoulders", 3, 12, 60, "Wall", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Chair Dips", "Using chair", "Triceps", 3, 10, 60, "Chair", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Door Frame Rows", "Using door frame", "Back, Biceps", 3, 10, 60, "Door Frame", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Standard Push-ups", "On floor", "Chest, Shoulders", 4, 12, 45, "Floor", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Diamond Push-ups", "Hands close together", "Triceps, Chest", 3, 10, 45, "Floor", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Table Rows", "Under table", "Back, Biceps", 3, 12, 45, "Table", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("One-arm Push-up Progression", "Work toward one-arm", "Chest, Shoulders", 4, 6, 90, "Floor", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Handstand Push-up Progression", "Against wall", "Shoulders", 3, 8, 60, "Wall", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Towel Rows", "Using towel on door", "Back, Biceps", 3, 10, 60, "Towel, Door", "Advanced", "Strength"));
        }
    }
    
    private static void addGymUpperBodyExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Machine Chest Press", "Push handles forward", "Chest, Shoulders", 3, 12, 60, "Chest Press Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Lat Pulldown", "Pull bar to chest", "Back, Biceps", 3, 12, 60, "Lat Pulldown Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Shoulder Press Machine", "Press handles up", "Shoulders", 3, 12, 60, "Shoulder Press Machine", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Barbell Bench Press", "Lower bar to chest", "Chest, Shoulders", 4, 8, 90, "Barbell, Bench", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Pull-ups", "Pull body up", "Back, Biceps", 3, 8, 60, "Pull-up Bar", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Dumbbell Shoulder Press", "Press dumbbells up", "Shoulders", 3, 10, 60, "Dumbbells", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Incline Bench Press", "On incline bench", "Upper Chest, Shoulders", 4, 6, 90, "Barbell, Incline Bench", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Weighted Pull-ups", "Add weight", "Back, Biceps", 4, 6, 90, "Pull-up Bar, Weight Belt", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Arnold Press", "Rotating press", "Shoulders", 3, 8, 60, "Dumbbells", "Advanced", "Strength"));
        }
    }
    
    private static void addOutdoorUpperBodyExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Park Bench Push-ups", "Using bench", "Chest, Shoulders", 3, 10, 60, "Park Bench", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Tree Branch Pull-ups", "Using branch", "Back, Biceps", 3, 6, 60, "Tree Branch", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Parallel Bar Dips", "Using bars", "Chest, Triceps", 3, 8, 60, "Parallel Bars", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Decline Push-ups", "Feet on bench", "Chest, Shoulders", 4, 12, 45, "Park Bench", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Pull-ups", "Using bar", "Back, Biceps", 3, 8, 60, "Bar", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Handstand Push-up Progression", "Against tree", "Shoulders", 3, 8, 60, "Tree", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("One-arm Push-up Progression", "Work toward one-arm", "Chest, Shoulders", 4, 6, 90, "Ground", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Muscle-ups", "Pull over bar", "Back, Shoulders", 3, 5, 90, "Bar", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Handstand Push-ups", "Against wall", "Shoulders", 3, 6, 90, "Wall", "Advanced", "Strength"));
        }
    }
    
    private static void addHomeLowerBodyExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Chair Squats", "Using chair", "Quadriceps, Glutes", 3, 12, 60, "Chair", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Step-ups", "Using stairs", "Quadriceps, Glutes", 3, 10, 60, "Stairs", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Calf Raises", "On step", "Calves", 3, 15, 60, "Step", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Pistol Squat Progression", "Single leg squat", "Quadriceps, Glutes", 3, 8, 60, "None", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Jump Squats", "Explosive squat jumps", "Quadriceps, Glutes", 3, 12, 60, "None", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Single-leg Calf Raises", "One leg at a time", "Calves", 3, 12, 60, "Step", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Pistol Squats", "Full single leg squat", "Quadriceps, Glutes", 3, 6, 90, "None", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Jump Lunges", "Explosive lunges", "Quadriceps, Glutes", 3, 10, 60, "None", "Advanced", "Plyometric"));
            workout.addExercise(new Exercise("Nordic Hamstring Curl", "Using partner", "Hamstrings", 3, 6, 90, "Partner", "Advanced", "Strength"));
        }
    }
    
    private static void addGymLowerBodyExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Leg Press", "Push platform away", "Quadriceps, Glutes", 3, 12, 60, "Leg Press Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Leg Extension", "Extend legs", "Quadriceps", 3, 12, 60, "Leg Extension Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Leg Curl", "Curl legs", "Hamstrings", 3, 12, 60, "Leg Curl Machine", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Squats", "Lower body", "Quadriceps, Glutes", 4, 10, 90, "Barbell", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Romanian Deadlift", "Hinge at hips", "Hamstrings, Glutes", 3, 10, 90, "Barbell", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Walking Lunges", "Step forward", "Quadriceps, Glutes", 3, 12, 60, "Dumbbells", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Front Squats", "Bar on front", "Quadriceps, Core", 4, 6, 120, "Barbell", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Bulgarian Split Squat", "One foot elevated", "Quadriceps, Glutes", 3, 8, 60, "Dumbbells", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Nordic Hamstring Curl", "Lower with control", "Hamstrings", 3, 8, 60, "Machine or Partner", "Advanced", "Strength"));
        }
    }
    
    private static void addOutdoorLowerBodyExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Step-ups", "Using park bench", "Quadriceps, Glutes", 3, 12, 60, "Park Bench", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Lunges", "Forward stepping", "Quadriceps, Glutes", 3, 10, 60, "None", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Calf Raises", "On step or curb", "Calves", 3, 15, 60, "Step or Curb", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Pistol Squat Progression", "Single leg squat", "Quadriceps, Glutes", 3, 8, 60, "None", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Jump Squats", "Explosive squat jumps", "Quadriceps, Glutes", 3, 12, 60, "None", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Hill Sprints", "Sprint up hill", "Quadriceps, Glutes", 5, 20, 120, "Hill", "Intermediate", "Cardio"));
        } else {
            workout.addExercise(new Exercise("Pistol Squats", "Full single leg squat", "Quadriceps, Glutes", 3, 6, 90, "None", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Box Jumps", "Jump onto bench", "Quadriceps, Glutes", 3, 8, 60, "Park Bench", "Advanced", "Plyometric"));
            workout.addExercise(new Exercise("Nordic Hamstring Curl", "Using partner", "Hamstrings", 3, 6, 90, "Partner", "Advanced", "Strength"));
        }
    }
    
    private static void addHomeCoreExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Plank", "Hold position", "Core", 3, 30, 60, "Floor", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Dead Bug", "Alternate arm/leg", "Core", 3, 10, 60, "Floor", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Bird Dog", "Extend opposite limbs", "Core", 3, 10, 60, "Floor", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Side Plank", "Hold on side", "Obliques", 3, 30, 60, "Floor", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Russian Twists", "Rotate with weight", "Obliques", 3, 12, 60, "Floor, Weight", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Leg Raises", "Lift legs up", "Lower Abs", 3, 12, 60, "Floor", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Dragon Flag", "Lower body with control", "Core", 3, 8, 90, "Floor", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Hanging Leg Raises", "Hang from bar", "Core", 3, 10, 60, "Pull-up Bar", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Ab Wheel Rollout", "Roll out and back", "Core", 3, 10, 60, "Ab Wheel", "Advanced", "Strength"));
        }
    }
    
    private static void addGymCoreExercises(Workout workout, String fitnessLevel) {
        if (fitnessLevel.equals("Beginner")) {
            workout.addExercise(new Exercise("Cable Woodchoppers", "Pull cable diagonally", "Core", 3, 12, 60, "Cable Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Cable Crunches", "Curl down against cable", "Abs", 3, 12, 60, "Cable Machine", "Beginner", "Strength"));
            workout.addExercise(new Exercise("Plank", "Hold position", "Core", 3, 30, 60, "Floor", "Beginner", "Strength"));
        } else if (fitnessLevel.equals("Intermediate")) {
            workout.addExercise(new Exercise("Hanging Knee Raises", "Hang from bar", "Core", 3, 12, 60, "Pull-up Bar", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Cable Russian Twists", "Rotate with cable", "Obliques", 3, 12, 60, "Cable Machine", "Intermediate", "Strength"));
            workout.addExercise(new Exercise("Ab Wheel Rollout", "Roll out and back", "Core", 3, 10, 60, "Ab Wheel", "Intermediate", "Strength"));
        } else {
            workout.addExercise(new Exercise("Hanging Leg Raises", "Hang from bar", "Core", 3, 10, 60, "Pull-up Bar", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Dragon Flag", "Lower body with control", "Core", 3, 8, 90, "Bench", "Advanced", "Strength"));
            workout.addExercise(new Exercise("Weighted Cable Crunches", "Add weight to crunches", "Abs", 3, 12, 60, "Cable Machine", "Advanced", "Strength"));
        }
    }
    
    private static void addOutdoorCoreExercises(Workout workout, String fitnessLevel) {
        // Implementation for outdoor core exercises
    }

    // Add missing methods needed by WorkoutPlanGenerator
    public static void addEnduranceWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout enduranceWorkout = new Workout(
                "Endurance Training",
                "Focus on cardiovascular endurance while building muscle",
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
    
    public static void addBalancedWorkouts(WorkoutPlan plan, int workoutTimeMinutes, 
            String fitnessLevel, String focusArea, String environment) {
        
        Workout balancedWorkout = new Workout(
                "Balanced Training",
                "Balance of strength and cardio for overall fitness",
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
    private static void addHomeEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Jumping Jacks", "3 sets of 45 seconds", "Cardio", 
                3, 45, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("High Knees", "3 sets of 45 seconds", "Cardio", 
                3, 45, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Mountain Climbers", "3 sets of 45 seconds", "Cardio", 
                3, 45, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Burpees", "3 sets of 30 seconds", "Full Body", 
                3, 30, 60, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Jump Squats", "3 sets of 15 reps", "Lower Body", 
                3, 15, 45, "None", fitnessLevel, "Plyometric"));
    }

    private static void addGymEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Treadmill Intervals", "20 minutes (30 sec sprint, 90 sec jog)", "Cardio", 
                1, 20, 0, "Treadmill", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Rowing Machine", "10 minutes (medium pace)", "Full Body", 
                1, 10, 0, "Rowing Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Battle Ropes", "3 sets of 30 seconds", "Upper Body", 
                3, 30, 45, "Battle Ropes", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Box Jumps", "3 sets of 12 reps", "Lower Body", 
                3, 12, 60, "Plyo Box", fitnessLevel, "Plyometric"));
    }

    private static void addOutdoorEnduranceExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Running Intervals", "20 minutes (30 sec sprint, 90 sec jog)", "Cardio", 
                1, 20, 0, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Hill Sprints", "8 sprints with 2 minute rest", "Lower Body", 
                8, 1, 120, "Hill", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Walking Lunges", "3 sets of 20 steps each leg", "Lower Body", 
                3, 20, 60, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 20 reps", "Lower Body", 
                3, 20, 45, "None", fitnessLevel, "Strength"));
    }
    
    private static void addHomeBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Push-ups", "3 sets of 10-15 reps", "Upper Body", 
                3, 15, 45, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Bodyweight Squats", "3 sets of 15-20 reps", "Lower Body", 
                3, 20, 45, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Mountain Climbers", "3 sets of 30 seconds", "Core", 
                3, 30, 30, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Plank", "3 sets of 30 seconds", "Core", 
                3, 30, 30, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Jumping Jacks", "3 sets of 30 seconds", "Cardio", 
                3, 30, 30, "None", fitnessLevel, "Cardio"));
    }

    private static void addGymBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Dumbbell Lunges", "3 sets of 8 reps per leg", "Lower Body", 
                3, 8, 60, "Dumbbells", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Cable Face Pulls", "3 sets of 12 reps", "Upper Body", 
                3, 12, 45, "Cable Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Rowing Machine", "3 sets of 1 minute", "Cardio", 
                3, 60, 45, "Rowing Machine", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Machine Chest Fly", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Chest Fly Machine", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Kettlebell Swings", "3 sets of 15 reps", "Full Body", 
                3, 15, 45, "Kettlebell", fitnessLevel, "Strength"));
    }

    private static void addOutdoorBalancedExercises(Workout workout, String fitnessLevel) {
        workout.addExercise(new Exercise("Sprint Intervals", "5 sets of 20 second sprints", "Cardio", 
                5, 20, 60, "None", fitnessLevel, "Cardio"));
        workout.addExercise(new Exercise("Park Bench Decline Push-ups", "3 sets of 8 reps", "Upper Body", 
                3, 8, 60, "Park Bench", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Box Jumps onto Bench", "3 sets of 10 reps", "Lower Body", 
                3, 10, 60, "Park Bench", fitnessLevel, "Plyometric"));
        workout.addExercise(new Exercise("Russian Twists", "3 sets of 20 reps", "Core", 
                3, 20, 45, "None", fitnessLevel, "Strength"));
        workout.addExercise(new Exercise("Tricep Dips on Bench", "3 sets of 10 reps", "Upper Body", 
                3, 10, 60, "Park Bench", fitnessLevel, "Strength"));
    }
} 