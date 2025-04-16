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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flextrack_ianation.R;
import com.example.flextrack_ianation.adapters.WorkoutSessionAdapter;
import com.example.flextrack_ianation.models.WorkoutSession;
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

public class DailyProgressFragment extends Fragment {
    private static final String TAG = "DailyProgressFragment";
    
    // UI elements
    private TextView dateDisplay;
    private ImageButton prevDayBtn;
    private ImageButton nextDayBtn;
    private RecyclerView dailyWorkoutsList;
    private TextView noWorkoutsMessage;
    private TextView dailyCalories;
    private TextView dailyActiveTime;
    private TextView dailyWorkoutsCount;
    private View loadingIndicator;
    
    // Workout adapter
    private WorkoutSessionAdapter workoutAdapter;
    private List<WorkoutSession> workoutSessions = new ArrayList<>();
    
    // Date handling
    private Calendar currentDate = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
    private SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    
    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_progress, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        // Initialize UI elements
        initViews(view);
        
        // Setup date navigation
        setupDateNavigation();
        
        // Setup workout list
        setupWorkoutList();
        
        // Load data for current date
        loadDailyData();
    }
    
    private void initViews(View view) {
        dateDisplay = view.findViewById(R.id.date_display);
        prevDayBtn = view.findViewById(R.id.prev_day_btn);
        nextDayBtn = view.findViewById(R.id.next_day_btn);
        dailyWorkoutsList = view.findViewById(R.id.daily_workouts_list);
        noWorkoutsMessage = view.findViewById(R.id.no_workouts_message);
        dailyCalories = view.findViewById(R.id.daily_calories);
        dailyActiveTime = view.findViewById(R.id.daily_active_time);
        dailyWorkoutsCount = view.findViewById(R.id.daily_workouts_count);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        
        if (loadingIndicator == null) {
            // If loading indicator view doesn't exist, find another view to use
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                if (child instanceof ProgressBar) {
                    loadingIndicator = child;
                    break;
                }
            }
        }
        
        // Set today's date
        updateDateDisplay();
    }
    
    private void setupDateNavigation() {
        prevDayBtn.setOnClickListener(v -> {
            currentDate.add(Calendar.DAY_OF_MONTH, -1);
            updateDateDisplay();
            loadDailyData();
        });
        
        nextDayBtn.setOnClickListener(v -> {
            // Don't allow navigation to future dates
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.add(Calendar.DAY_OF_MONTH, 1);
            
            if (currentDate.before(tomorrow)) {
                currentDate.add(Calendar.DAY_OF_MONTH, 1);
                updateDateDisplay();
                loadDailyData();
            } else {
                Toast.makeText(getContext(), "Cannot navigate to future dates", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupWorkoutList() {
        // Create an anonymous implementation of OnSessionClickListener
        WorkoutSessionAdapter.OnSessionClickListener listener = new WorkoutSessionAdapter.OnSessionClickListener() {
            @Override
            public void onSessionClick(WorkoutSession session, int position) {
                // Handle click event - display workout details or statistics
                Toast.makeText(getContext(), "Viewing " + session.getWorkoutName() + " details", Toast.LENGTH_SHORT).show();
                
                // Could launch a dialog or activity with workout details here
            }
        };
        
        // Initialize the adapter with context, data, and click listener
        workoutAdapter = new WorkoutSessionAdapter(getContext(), workoutSessions, listener);
        dailyWorkoutsList.setLayoutManager(new LinearLayoutManager(getContext()));
        dailyWorkoutsList.setAdapter(workoutAdapter);
    }
    
    private void updateDateDisplay() {
        Date date = currentDate.getTime();
        dateDisplay.setText(dateFormat.format(date));
        
        // Check if this is today
        Calendar today = Calendar.getInstance();
        boolean isToday = (today.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) &&
                         (today.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR));
        
        if (isToday) {
            dateDisplay.setText("Today, " + dateFormat.format(date));
        }
    }
    
    private void loadDailyData() {
        if (getContext() == null) return;
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Please log in to view your progress", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        showLoading(true);
        
        String userId = currentUser.getUid();
        String dateString = dbDateFormat.format(currentDate.getTime());
        
        // Load workout sessions only
        loadWorkoutSessions(userId, dateString);
    }
    
    private void showLoading(boolean isLoading) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
    
    private void loadWorkoutSessions(String userId, String dateString) {
        // Clear previous workout data
        workoutSessions.clear();
        
        // First try to load from workoutSessions collection (new format)
        Query newFormatQuery = mDatabase.child("workoutSessions")
                .orderByChild("userId")
                .equalTo(userId);
        
        newFormatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean foundSessions = false;
                int totalCalories = 0;
                int totalMinutes = 0;
                int workoutCount = 0;
                
                // Filter sessions from the same day
                Date selectedDate = currentDate.getTime();
                Calendar sessionCal = Calendar.getInstance();
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    WorkoutSession session = snapshot.getValue(WorkoutSession.class);
                    if (session != null) {
                        // Check if session is from the selected date
                        sessionCal.setTimeInMillis(session.getStartTimeMillis());
                        if (isSameDay(sessionCal.getTime(), selectedDate)) {
                            workoutSessions.add(session);
                            foundSessions = true;
                            
                            // Collect stats for summary
                            totalCalories += session.getCaloriesBurned();
                            totalMinutes += session.getDurationMinutes();
                            workoutCount++;
                        }
                    }
                }
                
                if (!foundSessions) {
                    // If no sessions found in new format, try legacy format
                    loadLegacyWorkoutSessions(userId, dateString);
                } else {
                    // Update UI with the data we found
                    updateWorkoutUI(workoutCount, totalMinutes, totalCalories);
                    
                    // Update adapter
                    workoutAdapter.notifyDataSetChanged();
                    
                    // Show/hide no workouts message
                    if (workoutSessions.isEmpty()) {
                        noWorkoutsMessage.setVisibility(View.VISIBLE);
                        dailyWorkoutsList.setVisibility(View.GONE);
                    } else {
                        noWorkoutsMessage.setVisibility(View.GONE);
                        dailyWorkoutsList.setVisibility(View.VISIBLE);
                    }
                    
                    // Hide loading indicator
                    showLoading(false);
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error loading workout sessions: " + databaseError.getMessage());
                // Try legacy format as backup
                loadLegacyWorkoutSessions(userId, dateString);
            }
        });
    }
    
    private void loadLegacyWorkoutSessions(String userId, String dateString) {
        // Query workouts for this date in legacy format
        Query query = mDatabase.child("users").child(userId).child("workoutHistory")
                .orderByChild("date").equalTo(dateString);
        
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalCalories = 0;
                int totalMinutes = 0;
                int workoutCount = 0;
                
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Try to convert legacy data to WorkoutSession
                    try {
                        String workoutId = snapshot.getKey();
                        String workoutName = snapshot.child("name").getValue(String.class);
                        String workoutType = snapshot.child("type").getValue(String.class);
                        Long timestamp = snapshot.child("timestamp").getValue(Long.class);
                        Integer duration = snapshot.child("duration").getValue(Integer.class);
                        Integer calories = snapshot.child("caloriesBurned").getValue(Integer.class);
                        Boolean completed = snapshot.child("completed").getValue(Boolean.class);
                        
                        // Create WorkoutSession from legacy data
                        if (workoutId != null && workoutName != null) {
                            WorkoutSession session = new WorkoutSession(
                                workoutId,
                                workoutName,
                                workoutType != null ? workoutType : "Unknown",
                                timestamp != null ? timestamp : System.currentTimeMillis(),
                                duration != null ? duration : 30,
                                calories != null ? calories : 0,
                                completed != null ? completed : false,
                                userId
                            );
                            
                            workoutSessions.add(session);
                            
                            // Collect stats for summary
                            totalCalories += session.getCaloriesBurned();
                            totalMinutes += session.getDurationMinutes();
                            workoutCount++;
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing legacy workout: " + e.getMessage());
                    }
                }
                
                // Hide loading indicator
                showLoading(false);
                
                // Update UI
                updateWorkoutUI(workoutCount, totalMinutes, totalCalories);
                
                // Update adapter
                workoutAdapter.notifyDataSetChanged();
                
                // Show/hide no workouts message
                if (workoutSessions.isEmpty()) {
                    noWorkoutsMessage.setVisibility(View.VISIBLE);
                    dailyWorkoutsList.setVisibility(View.GONE);
                } else {
                    noWorkoutsMessage.setVisibility(View.GONE);
                    dailyWorkoutsList.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showLoading(false);
                Log.e(TAG, "Failed to load workout data: " + databaseError.getMessage());
                Toast.makeText(getContext(), "Failed to load workout data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
    
    private void updateWorkoutUI(int workoutCount, int totalMinutes, int totalCalories) {
        // Update summary stats
        dailyCalories.setText(String.format(Locale.getDefault(), "%,d", totalCalories));
        dailyActiveTime.setText(String.format(Locale.getDefault(), "%d", totalMinutes));
        dailyWorkoutsCount.setText(String.format(Locale.getDefault(), "%d", workoutCount));
    }
} 