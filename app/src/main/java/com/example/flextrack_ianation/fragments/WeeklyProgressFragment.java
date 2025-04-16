package com.example.flextrack_ianation.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flextrack_ianation.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyProgressFragment extends Fragment {
    private static final String TAG = "WeeklyProgressFragment";
    
    // UI elements
    private TextView dateDisplay;
    private ImageButton prevWeekBtn;
    private ImageButton nextWeekBtn;
    private BarChart workoutMinutesChart;
    private TextView weeklyMinutesTotal;
    private TextView weeklyCalories;
    private TextView weeklyActiveDays;
    private TextView weeklyWorkoutsCount;
    private ProgressBar loadingIndicator;
    
    // Date handling
    private Calendar currentWeekStart = Calendar.getInstance();
    private SimpleDateFormat weekFormat = new SimpleDateFormat("MMM d - ", Locale.getDefault());
    private SimpleDateFormat weekEndFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String[] dayLabels = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    
    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly_progress, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        // Initialize UI elements
        initViews(view);
        
        // Set current week to start of week (Sunday)
        setToStartOfWeek();
        
        // Setup date navigation
        setupWeekNavigation();
        
        // Setup charts
        setupCharts();
        
        // Load data for current week
        loadWeeklyData();
    }
    
    private void initViews(View view) {
        dateDisplay = view.findViewById(R.id.date_display);
        prevWeekBtn = view.findViewById(R.id.prev_week_btn);
        nextWeekBtn = view.findViewById(R.id.next_week_btn);
        workoutMinutesChart = view.findViewById(R.id.workout_minutes_chart);
        weeklyMinutesTotal = view.findViewById(R.id.weekly_minutes_total);
        weeklyCalories = view.findViewById(R.id.weekly_calories);
        weeklyActiveDays = view.findViewById(R.id.weekly_active_days);
        weeklyWorkoutsCount = view.findViewById(R.id.weekly_workouts_count);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        
        if (loadingIndicator == null) {
            // Try to find progress bar in parent view if not directly available
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child instanceof ProgressBar) {
                    loadingIndicator = (ProgressBar) child;
                    break;
                }
            }
        }
        
        // Update date display
        updateDateDisplay();
    }
    
    private void setToStartOfWeek() {
        // Set to beginning of current week (Sunday)
        currentWeekStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        currentWeekStart.set(Calendar.HOUR_OF_DAY, 0);
        currentWeekStart.set(Calendar.MINUTE, 0);
        currentWeekStart.set(Calendar.SECOND, 0);
        currentWeekStart.set(Calendar.MILLISECOND, 0);
    }
    
    private void setupWeekNavigation() {
        prevWeekBtn.setOnClickListener(v -> {
            currentWeekStart.add(Calendar.WEEK_OF_YEAR, -1);
            updateDateDisplay();
            loadWeeklyData();
        });
        
        nextWeekBtn.setOnClickListener(v -> {
            // Don't allow navigation to future weeks
            Calendar nextWeek = Calendar.getInstance();
            nextWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            nextWeek.add(Calendar.WEEK_OF_YEAR, 1);
            
            if (currentWeekStart.before(nextWeek)) {
                currentWeekStart.add(Calendar.WEEK_OF_YEAR, 1);
                updateDateDisplay();
                loadWeeklyData();
            } else {
                Toast.makeText(getContext(), "Cannot navigate to future weeks", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateDateDisplay() {
        // Create end of week date (Saturday)
        Calendar weekEnd = (Calendar) currentWeekStart.clone();
        weekEnd.add(Calendar.DAY_OF_WEEK, 6);
        
        // Format the date range
        String dateRange = weekFormat.format(currentWeekStart.getTime()) + 
                         weekEndFormat.format(weekEnd.getTime());
        dateDisplay.setText(dateRange);
    }
    
    private void setupCharts() {
        // Workout minutes chart setup
        setupBarChart(workoutMinutesChart);
    }
    
    private void setupBarChart(BarChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);
        
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dayLabels));
        
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }
    
    private void showLoading(boolean isLoading) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
    
    private void loadWeeklyData() {
        if (getContext() == null) return;
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please log in to view your progress", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showLoading(true);
        
        String userId = currentUser.getUid();
        
        // Reset UI
        resetCharts();
        
        // Load workout data only
        loadWeeklyWorkoutData(userId);
    }
    
    private void resetCharts() {
        // Reset chart data to avoid stale data on reload
        workoutMinutesChart.clear();
        
        // Reset summary stats
        weeklyMinutesTotal.setText("0");
        weeklyCalories.setText("0");
        weeklyActiveDays.setText("0/7");
        weeklyWorkoutsCount.setText("0");
    }
    
    private void loadWeeklyWorkoutData(String userId) {
        // Create list of dates for the current week
        List<String> weekDates = getWeekDates();
        
        // Initialize data counters
        final int[] dailyMinutes = new int[7];
        final int[] totalMinutes = {0};
        final int[] totalCalories = {0};
        final int[] activeDays = {0};
        final int[] workoutCount = {0};
        final boolean[] dayHasWorkout = new boolean[7];
        
        // Try first to query from workoutSessions collection (new format)
        mDatabase.child("workoutSessions")
            .orderByChild("userId")
            .equalTo(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean foundWorkouts = false;
                    
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        long startTime = 0;
                        if (snapshot.child("startTimeMillis").exists()) {
                            startTime = snapshot.child("startTimeMillis").getValue(Long.class) != null ?
                                snapshot.child("startTimeMillis").getValue(Long.class) : 0;
                        }
                        
                        if (startTime > 0) {
                            // Convert time to date and check if in current week
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(startTime);
                            String dateString = dbDateFormat.format(cal.getTime());
                            
                            if (weekDates.contains(dateString)) {
                                foundWorkouts = true;
                                int dayIndex = weekDates.indexOf(dateString);
                                
                                Integer durationMinutes = snapshot.child("durationMinutes").getValue(Integer.class);
                                Integer caloriesBurned = snapshot.child("caloriesBurned").getValue(Integer.class);
                                Boolean completed = snapshot.child("completed").getValue(Boolean.class);
                                
                                if ((completed == null || completed) && durationMinutes != null) {
                                    dailyMinutes[dayIndex] += durationMinutes;
                                    totalMinutes[0] += durationMinutes;
                                    dayHasWorkout[dayIndex] = true;
                                    workoutCount[0]++;
                                    
                                    if (caloriesBurned != null) {
                                        totalCalories[0] += caloriesBurned;
                                    }
                                }
                            }
                        }
                    }
                    
                    if (!foundWorkouts) {
                        // If no workouts found in new format, try legacy format
                        loadLegacyWorkoutData(userId, weekDates, dailyMinutes, totalMinutes, 
                                             totalCalories, activeDays, workoutCount, dayHasWorkout);
                    } else {
                        // Count active days
                        for (boolean active : dayHasWorkout) {
                            if (active) activeDays[0]++;
                        }
                        
                        // Update UI
                        updateWorkoutMinutesChart(dailyMinutes);
                        updateWeeklyStatsUI(totalMinutes[0], totalCalories[0], activeDays[0], workoutCount[0]);
                        showLoading(false);
                    }
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Failed to load workout sessions: " + databaseError.getMessage());
                    // Fall back to legacy format
                    loadLegacyWorkoutData(userId, weekDates, dailyMinutes, totalMinutes, 
                                         totalCalories, activeDays, workoutCount, dayHasWorkout);
                }
            });
    }
    
    private void loadLegacyWorkoutData(String userId, List<String> weekDates, 
                                      int[] dailyMinutes, int[] totalMinutes,
                                      int[] totalCalories, int[] activeDays, 
                                      int[] workoutCount, boolean[] dayHasWorkout) {
        // Query workouts for the week using the legacy format
        DatabaseReference workoutRef = mDatabase.child("users").child(userId).child("workoutHistory");
        
        workoutRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String date = snapshot.child("date").getValue(String.class);
                    if (date != null && weekDates.contains(date)) {
                        int dayIndex = weekDates.indexOf(date);
                        
                        Integer durationMinutes = snapshot.child("durationMinutes").getValue(Integer.class);
                        Integer caloriesBurned = snapshot.child("caloriesBurned").getValue(Integer.class);
                        Boolean completed = snapshot.child("completed").getValue(Boolean.class);
                        
                        if (completed != null && completed && durationMinutes != null) {
                            dailyMinutes[dayIndex] += durationMinutes;
                            totalMinutes[0] += durationMinutes;
                            dayHasWorkout[dayIndex] = true;
                            workoutCount[0]++;
                            
                            if (caloriesBurned != null) {
                                totalCalories[0] += caloriesBurned;
                            }
                        }
                    }
                }
                
                // Count active days
                for (boolean active : dayHasWorkout) {
                    if (active) activeDays[0]++;
                }
                
                // Update UI
                updateWorkoutMinutesChart(dailyMinutes);
                updateWeeklyStatsUI(totalMinutes[0], totalCalories[0], activeDays[0], workoutCount[0]);
                showLoading(false);
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to load workout data: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to load workout data", Toast.LENGTH_SHORT).show();
                showLoading(false);
            }
        });
    }
    
    private List<String> getWeekDates() {
        List<String> dates = new ArrayList<>();
        Calendar date = (Calendar) currentWeekStart.clone();
        
        for (int i = 0; i < 7; i++) {
            dates.add(dbDateFormat.format(date.getTime()));
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return dates;
    }
    
    private void updateWorkoutMinutesChart(int[] dailyMinutes) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dailyMinutes.length; i++) {
            entries.add(new BarEntry(i, dailyMinutes[i]));
        }
        
        BarDataSet dataSet = new BarDataSet(entries, "Minutes");
        dataSet.setColor(getResources().getColor(R.color.accent));
        dataSet.setValueTextSize(10f);
        
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.6f);
        
        workoutMinutesChart.setData(data);
        workoutMinutesChart.invalidate();
    }
    
    private void updateWeeklyStatsUI(int totalMinutes, int totalCalories, 
                                     int activeDays, int workoutCount) {
        weeklyMinutesTotal.setText(String.format(Locale.getDefault(), "%d", totalMinutes));
        weeklyCalories.setText(String.format(Locale.getDefault(), "%,d", totalCalories));
        weeklyActiveDays.setText(String.format(Locale.getDefault(), "%d/7", activeDays));
        weeklyWorkoutsCount.setText(String.format(Locale.getDefault(), "%d", workoutCount));
    }
} 