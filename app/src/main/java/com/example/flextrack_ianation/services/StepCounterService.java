package com.example.flextrack_ianation.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.flextrack_ianation.R;
import com.example.flextrack_ianation.StepsCounterActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StepCounterService extends Service implements SensorEventListener {
    private static final String TAG = "StepCounterService";
    private static final String CHANNEL_ID = "step_counter_channel";
    private static final int NOTIFICATION_ID = 1001;
    
    // Constants for step tracking
    public static final String PREFS_NAME = "StepCounterPrefs";
    public static final String KEY_STEP_COUNT = "step_count";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_BOOT_TIME = "boot_time";
    public static final String KEY_CALORIES = "calories_burned";
    public static final String KEY_DISTANCE = "distance_traveled";
    public static final String KEY_IS_TRACKING = "is_tracking";
    public static final String KEY_DAILY_STEPS = "daily_steps";
    public static final String KEY_DAILY_GOAL = "daily_goal";
    public static final String KEY_LAST_SAVED_DAY = "last_saved_day";
    
    // Average stride length in meters by height (can be personalized)
    private static final float DEFAULT_STRIDE_LENGTH = 0.75f;
    // Average calories burned per step (can be adjusted by weight)
    private static final float CALORIES_PER_STEP = 0.04f;
    
    // Sensitivity for step detection from accelerometer
    private static final float STEP_THRESHOLD = 12.0f;
    // Minimum time between steps in milliseconds
    private static final long MIN_TIME_BETWEEN_STEPS_MS = 250;
    
    private SensorManager sensorManager;
    private Sensor stepSensor;
    private Sensor accelerometer;
    private Sensor significantMotionSensor;
    private boolean hasStepCounter;
    private PowerManager.WakeLock wakeLock;
    
    // Step tracking variables
    private int currentSteps = 0;      // Steps in current session (resets with Reset button)
    private int previousStepCount = 0;
    private int totalSteps = 0;        // Total steps from hardware counter (never resets)
    private int dailySteps = 0;
    private int dailyGoal = 10000;
    private long lastStepTimestamp = 0;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    
    // Tracking state
    private boolean isTracking = false;
    private long startTime = 0;
    
    // Binder for activity communication
    private final IBinder binder = new StepBinder();
    
    public class StepBinder extends Binder {
        public StepCounterService getService() {
            return StepCounterService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
        // Check for step counter sensor
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        hasStepCounter = (stepSensor != null);
        
        // If no step counter, use accelerometer
        if (!hasStepCounter) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer == null) {
                Log.e(TAG, "No step counter or accelerometer found on this device");
            } else {
                Log.d(TAG, "Using accelerometer for step counting");
            }
        } else {
            Log.d(TAG, "Using device step counter sensor");
        }
        
        // Get significant motion sensor for power efficiency
        significantMotionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        
        // Acquire partial wake lock to ensure service keeps running
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, 
                "FlexTrack:StepCounterWakeLock");
        
        // Restore saved data
        loadData();
        
        // Check if day has changed and reset daily counters if needed
        checkAndResetDailyCounters();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildNotification());
        
        if (!wakeLock.isHeld()) {
            wakeLock.acquire();
        }
        
        // Always reset the current session steps to 0 when starting
        currentSteps = 0;
        startTime = System.currentTimeMillis();
        
        // Reset session metrics (calories and distance)
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(KEY_CALORIES, 0f);
        editor.putFloat(KEY_DISTANCE, 0f);
        
        // Clear any pause time and ensure tracking is enabled
        editor.putLong("pause_time", 0);
        editor.putBoolean(KEY_IS_TRACKING, true);
        
        editor.apply();
        
        // Register sensors based on availability
        if (hasStepCounter) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
        
        isTracking = true;
        
        // Save current state
        saveData();
        
        // Broadcast update with the reset session values
        broadcastStepUpdate();
        
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // If we were paused, resume tracking
        if (!isTracking) {
            // Register sensors based on availability
            if (hasStepCounter) {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            }
            
            isTracking = true;
            
            // Get the pause time to adjust the start time accordingly
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            long pauseTime = prefs.getLong("pause_time", 0);
            
            if (pauseTime > 0) {
                // Calculate how long the session was paused
                long pauseDuration = System.currentTimeMillis() - pauseTime;
                
                // Adjust start time by adding pause duration to maintain accurate elapsed time
                startTime += pauseDuration;
                
                // Reset pause time
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("pause_time", 0);
                editor.putBoolean(KEY_IS_TRACKING, true);
                editor.apply();
            }
            
            // Save updated state
            saveData();
            
            // Update notification with resumed state
            NotificationManager notificationManager = 
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Step Counting Resumed")
                    .setContentText("Session: " + currentSteps + " steps")
                    .setSmallIcon(android.R.drawable.ic_menu_directions)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setOngoing(true)
                    .build();
            
            notificationManager.notify(NOTIFICATION_ID, notification);
            
            // Broadcast update
            broadcastStepUpdate();
        }
        
        return binder;
    }
    
    @Override
    public void onDestroy() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        saveData();
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Don't process steps if tracking is paused
        if (!isTracking) {
            return;
        }
        
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            // Handle hardware step counter
            processHardwareStepCount(event);
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Handle accelerometer for software step detection
            processSoftwareStepDetection(event);
        }
        
        // Update notification periodically
        if (currentSteps % 20 == 0) {
            updateNotification();
        }
    }
    
    private void processHardwareStepCount(SensorEvent event) {
        int hardwareSteps = (int) event.values[0];
        
        // Store the first reading to calculate delta
        if (previousStepCount == 0) {
            previousStepCount = hardwareSteps;
            totalSteps = hardwareSteps; // Initialize total steps with hardware value
            return;
        }
        
        // Calculate steps taken since last reading
        int stepDelta = hardwareSteps - previousStepCount;
        if (stepDelta > 0) {
            // Sometimes when rebooting, the step counter might reset, handle this case
            if (stepDelta > 1000) {
                // Likely a device reboot, handle accordingly
                Log.d(TAG, "Large step delta detected (" + stepDelta + "), possibly from reboot");
                previousStepCount = hardwareSteps;
                return;
            }
            
            // Update counters
            currentSteps += stepDelta;  // Session steps
            dailySteps += stepDelta;
            totalSteps = hardwareSteps; // Total hardware steps
            previousStepCount = hardwareSteps;
            
            // Update metrics
            updateMetrics(stepDelta);
            broadcastStepUpdate();
        }
    }
    
    private void processSoftwareStepDetection(SensorEvent event) {
        // Apply low-pass filter to isolate gravity
        final float alpha = 0.8f;
        
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
        
        // Remove gravity component with high-pass filter
        linear_acceleration[0] = event.values[0] - gravity[0];
        linear_acceleration[1] = event.values[1] - gravity[1];
        linear_acceleration[2] = event.values[2] - gravity[2];
        
        // Calculate acceleration magnitude
        float accelerationMagnitude = (float) Math.sqrt(
                linear_acceleration[0] * linear_acceleration[0] +
                linear_acceleration[1] * linear_acceleration[1] +
                linear_acceleration[2] * linear_acceleration[2]);
        
        long currentTime = System.currentTimeMillis();
        
        // Enhanced step detection algorithm
        if (accelerationMagnitude > STEP_THRESHOLD &&
            (currentTime - lastStepTimestamp) > MIN_TIME_BETWEEN_STEPS_MS) {
            
            // Validate the step using a pattern recognition algorithm
            // Here we simply use a time-based approach, but this could be enhanced
            // with more sophisticated pattern recognition
            
            currentSteps++;
            dailySteps++;
            lastStepTimestamp = currentTime;
            updateMetrics(1);
            broadcastStepUpdate();
        }
    }
    
    private void updateMetrics(int stepDelta) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // Update calories burned (can be personalized based on user's weight)
        float calories = prefs.getFloat(KEY_CALORIES, 0f);
        calories += stepDelta * CALORIES_PER_STEP;
        
        // Update distance traveled (can be personalized based on user's height)
        float distance = prefs.getFloat(KEY_DISTANCE, 0f);
        distance += stepDelta * DEFAULT_STRIDE_LENGTH;
        
        // Save updated metrics
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(KEY_CALORIES, calories);
        editor.putFloat(KEY_DISTANCE, distance);
        editor.putInt(KEY_STEP_COUNT, currentSteps);
        editor.putInt(KEY_DAILY_STEPS, dailySteps);
        editor.apply();
    }
    
    private void broadcastStepUpdate() {
        Intent intent = new Intent("STEP_COUNTER_UPDATE");
        intent.putExtra("steps", currentSteps);         // Session steps
        intent.putExtra("totalSteps", totalSteps);      // Total hardware steps
        intent.putExtra("dailySteps", dailySteps);
        intent.putExtra("dailyGoal", dailyGoal);
        
        // Calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis() - startTime;
        intent.putExtra("time", elapsedTimeMillis);
        
        // Get updated metrics
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        float calories = prefs.getFloat(KEY_CALORIES, 0f);
        float distance = prefs.getFloat(KEY_DISTANCE, 0f);
        
        intent.putExtra("calories", calories);
        intent.putExtra("distance", distance);
        
        sendBroadcast(intent);
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Log accuracy changes for diagnostic purposes
        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER || 
            sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "Sensor accuracy changed: " + accuracy);
        }
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Step Counter",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Shows your current step counting progress");
            
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    private Notification buildNotification() {
        Intent notificationIntent = new Intent(this, StepsCounterActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 
                PendingIntent.FLAG_IMMUTABLE
        );
        
        // Get session metrics
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        float calories = prefs.getFloat(KEY_CALORIES, 0f);
        
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Total Steps: " + totalSteps)
                .setContentText("Session: " + currentSteps + " steps • " + String.format("%.1f", calories) + " kcal")
                .setSmallIcon(android.R.drawable.ic_menu_directions)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }
    
    private void updateNotification() {
        NotificationManager notificationManager = 
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, buildNotification());
    }
    
    private void loadData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentSteps = prefs.getInt(KEY_STEP_COUNT, 0);
        dailySteps = prefs.getInt(KEY_DAILY_STEPS, 0);
        dailyGoal = prefs.getInt(KEY_DAILY_GOAL, 10000);
        startTime = prefs.getLong(KEY_START_TIME, 0);
        isTracking = prefs.getBoolean(KEY_IS_TRACKING, false);
        previousStepCount = currentSteps; // Initialize with the last saved value
    }
    
    private void saveData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_STEP_COUNT, currentSteps);
        editor.putInt(KEY_DAILY_STEPS, dailySteps);
        editor.putInt(KEY_DAILY_GOAL, dailyGoal);
        editor.putLong(KEY_START_TIME, startTime);
        editor.putBoolean(KEY_IS_TRACKING, isTracking);
        
        // Save the current date for daily reset checking
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());
        editor.putString(KEY_LAST_SAVED_DAY, today);
        
        editor.apply();
    }
    
    private void checkAndResetDailyCounters() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lastSavedDay = prefs.getString(KEY_LAST_SAVED_DAY, "");
        
        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = dateFormat.format(new Date());
        
        // If the day has changed, reset daily counters
        if (!today.equals(lastSavedDay)) {
            Log.d(TAG, "New day detected. Resetting daily step count");
            dailySteps = 0;
            
            // Save the historical data for the previous day if needed
            // (could be implemented to store in a database for history tracking)
            
            // Update preferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_DAILY_STEPS, 0);
            editor.putString(KEY_LAST_SAVED_DAY, today);
            editor.apply();
        }
    }
    
    // Public methods to control the service from activity
    
    /**
     * Resets both the current session steps and saved data
     */
    public void resetSteps() {
        currentSteps = 0;
        startTime = System.currentTimeMillis();
        
        // Reset calories and distance in SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(KEY_CALORIES, 0f);
        editor.putFloat(KEY_DISTANCE, 0f);
        editor.apply();
        
        // Keep the hardware step count (totalSteps) intact
        // Reset only session tracking
        
        // Save the reset state
        saveData();
        
        // Broadcast update
        broadcastStepUpdate();
        
        // Update notification
        updateNotification();
    }
    
    /**
     * Resets only the current session steps without affecting total or daily steps
     */
    public void resetSessionSteps() {
        currentSteps = 0;
        startTime = System.currentTimeMillis();
        
        // Reset calories and distance in SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat(KEY_CALORIES, 0f);
        editor.putFloat(KEY_DISTANCE, 0f);
        editor.apply();
        
        // Save the reset state
        saveData();
        
        // Broadcast update
        broadcastStepUpdate();
        
        // Update notification
        updateNotification();
    }
    
    /**
     * Pauses the session tracking without destroying the service
     * This will stop updating steps until the service is rebound
     */
    public void pauseSession() {
        // Pause step tracking by unregistering sensor listeners
        sensorManager.unregisterListener(this);
        
        // Update tracking state
        isTracking = false;
        
        // Remember the pauseTime for proper time tracking
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("pause_time", System.currentTimeMillis());
        
        // Save the current session state with tracking flag set to false
        // This is critical so the steps don't continue being counted
        editor.putBoolean(KEY_IS_TRACKING, false);
        editor.apply();
        
        saveData();
        
        // Update notification to show paused state
        NotificationManager notificationManager = 
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Step Counting Paused")
                .setContentText("Session: " + currentSteps + " steps (paused)")
                .setSmallIcon(android.R.drawable.ic_menu_directions)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
        
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
    
    public void setDailyGoal(int goal) {
        if (goal > 0) {
            dailyGoal = goal;
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(KEY_DAILY_GOAL, dailyGoal);
            editor.apply();
            
            broadcastStepUpdate();
            updateNotification();
        }
    }
    
    public boolean isTracking() {
        return isTracking;
    }
    
    public int getCurrentSteps() {
        return currentSteps;
    }
    
    public int getTotalSteps() {
        return totalSteps;
    }
    
    public int getDailySteps() {
        return dailySteps;
    }
    
    public int getDailyGoal() {
        return dailyGoal;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public float getCaloriesBurned() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getFloat(KEY_CALORIES, 0f);
    }
    
    public float getDistanceTraveled() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return prefs.getFloat(KEY_DISTANCE, 0f);
    }
    
    /**
     * Checks if a session exists but is currently paused
     */
    public static boolean isSessionPaused(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedSteps = prefs.getInt(KEY_STEP_COUNT, 0);
        boolean isTracking = prefs.getBoolean(KEY_IS_TRACKING, false);
        
        // If we have steps but tracking is false, we're paused
        return (savedSteps > 0 && !isTracking);
    }
} 