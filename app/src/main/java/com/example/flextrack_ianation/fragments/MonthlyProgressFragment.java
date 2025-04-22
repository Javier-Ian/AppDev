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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MonthlyProgressFragment extends Fragment {
    private static final String TAG = "MonthlyProgressFragment";
    
    // UI elements
    private TextView dateDisplay;
    private ImageButton prevMonthBtn;
    private ImageButton nextMonthBtn;
    private PieChart workoutTypesChart;
    private TextView monthlyWorkoutsTotal;
    private TextView monthlyCalories;
    private TextView monthlyActiveDays;
    private TextView monthlyWorkoutsCount;
    private ProgressBar loadingIndicator;
    
    // Date handling
    private Calendar currentMonth = Calendar.getInstance();
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    
    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monthly_progress, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        // Initialize UI elements
        initViews(view);
        
        // Set current month to first day of month
        setToStartOfMonth();
        
        // Setup date navigation
        setupMonthNavigation();
        
        // Setup charts
        setupCharts();
        
        // Load data for current month
        loadMonthlyData();
    }
    
    private void initViews(View view) {
        dateDisplay = view.findViewById(R.id.date_display);
        prevMonthBtn = view.findViewById(R.id.prev_month_btn);
        nextMonthBtn = view.findViewById(R.id.next_month_btn);
        workoutTypesChart = view.findViewById(R.id.workout_types_chart);
        monthlyWorkoutsTotal = view.findViewById(R.id.monthly_minutes_total);
        monthlyCalories = view.findViewById(R.id.monthly_calories);
        monthlyActiveDays = view.findViewById(R.id.monthly_active_days);
        monthlyWorkoutsCount = view.findViewById(R.id.monthly_workouts_count);
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
    
    private void setToStartOfMonth() {
        // Set to beginning of current month
        currentMonth.set(Calendar.DAY_OF_MONTH, 1);
        currentMonth.set(Calendar.HOUR_OF_DAY, 0);
        currentMonth.set(Calendar.MINUTE, 0);
        currentMonth.set(Calendar.SECOND, 0);
        currentMonth.set(Calendar.MILLISECOND, 0);
    }
    
    private void setupMonthNavigation() {
        prevMonthBtn.setOnClickListener(v -> {
            currentMonth.add(Calendar.MONTH, -1);
            updateDateDisplay();
            loadMonthlyData();
        });
        
        nextMonthBtn.setOnClickListener(v -> {
            // Don't allow navigation to future months
            Calendar nextMonth = Calendar.getInstance();
            nextMonth.set(Calendar.DAY_OF_MONTH, 1);
            nextMonth.add(Calendar.MONTH, 1);
            
            if (currentMonth.before(nextMonth)) {
                currentMonth.add(Calendar.MONTH, 1);
                updateDateDisplay();
                loadMonthlyData();
            } else {
                Toast.makeText(getContext(), "Cannot navigate to future months", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateDateDisplay() {
        // Format the month and year
        dateDisplay.setText(monthFormat.format(currentMonth.getTime()));
    }
    
    private void setupCharts() {
        // Workout types pie chart setup
        setupPieChart(workoutTypesChart);
    }
    
    private void setupPieChart(PieChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setUsePercentValues(true);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(android.R.color.transparent);
        chart.setHoleRadius(30f);
        chart.setTransparentCircleRadius(35f);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.setEntryLabelTextSize(12f);
        chart.setEntryLabelColor(getResources().getColor(R.color.primary_text));
        chart.getLegend().setEnabled(false);
    }
    
    private void showLoading(boolean isLoading) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
    
    private void loadMonthlyData() {
        if (getContext() == null) return;
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please log in to view your progress", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showLoading(true);
        
        String userId = currentUser.getUid();
        
        // Get dates for the month
        List<String> monthDates = getMonthDates();
        
        // Load workout data for the month only
        loadMonthlyWorkoutData(userId, monthDates);
    }
    
    private List<String> getMonthDates() {
        List<String> dates = new ArrayList<>();
        Calendar date = (Calendar) currentMonth.clone();
        
        int daysInMonth = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < daysInMonth; i++) {
            dates.add(dbDateFormat.format(date.getTime()));
            date.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return dates;
    }
    
    private void loadMonthlyWorkoutData(String userId, List<String> monthDates) {
        Map<String, Integer> workoutTypes = new HashMap<>();
        int totalMinutes = 0;
        int totalCalories = 0;
        int activeDays = 0;
        int workoutCount = 0;
        
        // Try first to query from workoutSessions collection (new format)
        mDatabase.child("workoutSessions")
            .orderByChild("userId")
            .equalTo(userId)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int minutes = 0;
                    int calories = 0;
                    int workouts = 0;
                    Map<String, Integer> types = new HashMap<>();
                    Set<Integer> activeDay = new HashSet<>();
                    
                    // Process current month data from workout sessions
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        long startTime = 0;
                        if (snapshot.child("startTimeMillis").exists()) {
                            startTime = snapshot.child("startTimeMillis").getValue(Long.class) != null ?
                                snapshot.child("startTimeMillis").getValue(Long.class) : 0;
                        }
                        
                        if (startTime > 0) {
                            // Convert to date and check if in current month
                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(startTime);
                            String dateString = dbDateFormat.format(cal.getTime());
                            
                            if (monthDates.contains(dateString)) {
                                // Get day of month for tracking active days
                                int day = cal.get(Calendar.DAY_OF_MONTH);
                                activeDay.add(day);
                                
                                Integer duration = snapshot.child("durationMinutes").getValue(Integer.class);
                                Integer caloriesBurned = snapshot.child("caloriesBurned").getValue(Integer.class);
                                String workoutType = snapshot.child("workoutType").getValue(String.class);
                                
                                if (duration != null) {
                                    minutes += duration;
                                }
                                
                                if (caloriesBurned != null) {
                                    calories += caloriesBurned;
                                }
                                
                                workouts++;
                                
                                // Update workout type counts
                                if (workoutType != null) {
                                    types.put(workoutType, types.getOrDefault(workoutType, 0) + 1);
                                }
                            }
                        }
                    }
                    
                    // If no data found, try legacy format
                    if (workouts == 0) {
                        loadLegacyWorkoutData(userId, monthDates);
                    } else {
                        // Update UI with the collected data
                        updateUI(minutes, calories, activeDay.size(), workouts, types);
                        showLoading(false);
                    }
                }
                
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Error loading workout sessions: " + databaseError.getMessage());
                    // Fall back to legacy format
                    loadLegacyWorkoutData(userId, monthDates);
                }
            });
    }
    
    private void loadLegacyWorkoutData(String userId, List<String> monthDates) {
        mDatabase.child("users").child(userId).child("workoutHistory")
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int minutes = 0;
                    int calories = 0;
                    int workouts = 0;
                    Map<String, Integer> types = new HashMap<>();
                    Set<String> activeDaySet = new HashSet<>();
                    
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String date = snapshot.child("date").getValue(String.class);
                        
                        if (date != null && monthDates.contains(date)) {
                            // This date is in the current month
                            activeDaySet.add(date);
                            
                            Integer duration = snapshot.child("durationMinutes").getValue(Integer.class);
                            Integer caloriesBurned = snapshot.child("caloriesBurned").getValue(Integer.class);
                            String workoutType = snapshot.child("workoutType").getValue(String.class);
                            Boolean completed = snapshot.child("completed").getValue(Boolean.class);
                            
                            if ((completed == null || completed)) {
                                if (duration != null) {
                                    minutes += duration;
                                }
                                
                                if (caloriesBurned != null) {
                                    calories += caloriesBurned;
                                }
                                
                                workouts++;
                                
                                // Update workout type counts
                                if (workoutType != null) {
                                    types.put(workoutType, types.getOrDefault(workoutType, 0) + 1);
                                }
                            }
                        }
                    }
                    
                    // Update UI with the collected data
                    updateUI(minutes, calories, activeDaySet.size(), workouts, types);
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
    
    private void updateUI(int totalMinutes, int totalCalories, int activeDays, int workoutCount, Map<String, Integer> workoutTypes) {
        // Update summary stats
        monthlyWorkoutsTotal.setText(String.format(Locale.getDefault(), "%d minutes", totalMinutes));
        monthlyCalories.setText(String.format(Locale.getDefault(), "%,d", totalCalories));
        monthlyActiveDays.setText(String.format(Locale.getDefault(), "%d", activeDays));
        monthlyWorkoutsCount.setText(String.format(Locale.getDefault(), "%d", workoutCount));
        
        // Update workout types pie chart
        updateWorkoutTypesChart(workoutTypes);
    }
    
    private Date parseDate(String dateString) {
        try {
            return dbDateFormat.parse(dateString);
        } catch (Exception e) {
            return new Date();
        }
    }
    
    private void updateWorkoutTypesChart(Map<String, Integer> workoutTypes) {
        List<PieEntry> entries = new ArrayList<>();
        
        // Create entries for each workout type
        for (Map.Entry<String, Integer> entry : workoutTypes.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }
        
        // If no data, add a placeholder
        if (entries.isEmpty()) {
            entries.add(new PieEntry(1, "No workouts"));
        }
        
        PieDataSet dataSet = new PieDataSet(entries, "Workout Types");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        
        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(getResources().getColor(R.color.white));
        
        workoutTypesChart.setData(data);
        workoutTypesChart.invalidate();
    }
} 