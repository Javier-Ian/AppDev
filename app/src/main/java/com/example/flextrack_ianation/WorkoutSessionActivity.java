package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.flextrack_ianation.adapters.WorkoutSessionAdapter;
import com.example.flextrack_ianation.models.WorkoutSession;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkoutSessionActivity extends AppCompatActivity implements WorkoutSessionAdapter.OnSessionClickListener {

    private RecyclerView recyclerView;
    private WorkoutSessionAdapter adapter;
    private List<WorkoutSession> sessionList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyStateContainer;
    private Button btnStartWorkout;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_session);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Workout History");
        }

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
        } else {
            // Handle not logged in
            Toast.makeText(this, "You need to be logged in to view workout history", 
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewSessions);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        emptyStateContainer = findViewById(R.id.emptyStateContainer);
        btnStartWorkout = findViewById(R.id.btnStartWorkout);
        FloatingActionButton fabAddSession = findViewById(R.id.fabAddSession);

        // Initialize workout session list
        sessionList = new ArrayList<>();
        adapter = new WorkoutSessionAdapter(this, sessionList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Set up pull to refresh
        swipeRefreshLayout.setOnRefreshListener(this::loadWorkoutSessions);

        // Set up click listeners
        btnStartWorkout.setOnClickListener(v -> navigateToWorkoutPlans());
        fabAddSession.setOnClickListener(v -> navigateToWorkoutPlans());

        // Load workout sessions from Firestore
        loadWorkoutSessions();
    }

    private void loadWorkoutSessions() {
        swipeRefreshLayout.setRefreshing(true);
        
        db.collection("workoutSessions")
            .whereEqualTo("userId", userId)
            .get()
            .addOnCompleteListener(task -> {
                swipeRefreshLayout.setRefreshing(false);
                
                if (task.isSuccessful()) {
                    sessionList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        WorkoutSession session = document.toObject(WorkoutSession.class);
                        sessionList.add(session);
                    }
                    
                    // Sort by start time (newest first)
                    Collections.sort(sessionList, (s1, s2) -> 
                            Long.compare(s2.getStartTimeMillis(), s1.getStartTimeMillis()));
                    
                    adapter.notifyDataSetChanged();
                    
                    // Update UI based on list state
                    updateEmptyState();
                } else {
                    Toast.makeText(WorkoutSessionActivity.this, "Error loading workout sessions", 
                            Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void updateEmptyState() {
        if (sessionList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateContainer.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateContainer.setVisibility(View.GONE);
        }
    }

    private void navigateToWorkoutPlans() {
        Intent intent = new Intent(this, WorkoutPlanActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSessionClick(WorkoutSession session, int position) {
        Intent intent = new Intent(this, WorkoutDetailsActivity.class);
        intent.putExtra("workoutId", session.getWorkoutId());
        intent.putExtra("fromHistory", true);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity
        loadWorkoutSessions();
    }
} 