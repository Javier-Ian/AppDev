package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HealthProfileActivity extends AppCompatActivity {

    private EditText fullNameEditText, ageEditText, heightEditText, weightEditText, 
                     workoutTimeEditText, daysPerWeekEditText, medicalConditionsEditText;
    private Spinner fitnessGoalSpinner, fitnessLevelSpinner, intensitySpinner, 
                    environmentSpinner, focusAreaSpinner;
    private RadioGroup genderRadioGroup;
    private Button saveProfileButton;
    
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_profile);
        
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        // Initialize UI elements
        initializeViews();
        setupSpinners();
        
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHealthProfile();
            }
        });
    }
    
    private void initializeViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        workoutTimeEditText = findViewById(R.id.workoutTimeEditText);
        daysPerWeekEditText = findViewById(R.id.daysPerWeekEditText);
        medicalConditionsEditText = findViewById(R.id.medicalConditionsEditText);
        
        fitnessGoalSpinner = findViewById(R.id.fitnessGoalSpinner);
        fitnessLevelSpinner = findViewById(R.id.fitnessLevelSpinner);
        intensitySpinner = findViewById(R.id.intensitySpinner);
        environmentSpinner = findViewById(R.id.environmentSpinner);
        focusAreaSpinner = findViewById(R.id.focusAreaSpinner);
        
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        
        saveProfileButton = findViewById(R.id.saveProfileButton);
    }
    
    private void setupSpinners() {
        // Fitness Goal Spinner
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.fitness_goals, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fitnessGoalSpinner.setAdapter(goalAdapter);
        
        // Fitness Level Spinner
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this,
                R.array.fitness_levels, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fitnessLevelSpinner.setAdapter(levelAdapter);
        
        // Intensity Spinner
        ArrayAdapter<CharSequence> intensityAdapter = ArrayAdapter.createFromResource(this,
                R.array.intensity_levels, android.R.layout.simple_spinner_item);
        intensityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intensitySpinner.setAdapter(intensityAdapter);
        
        // Environment Spinner
        ArrayAdapter<CharSequence> environmentAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_environments, android.R.layout.simple_spinner_item);
        environmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        environmentSpinner.setAdapter(environmentAdapter);
        
        // Focus Area Spinner
        ArrayAdapter<CharSequence> focusAdapter = ArrayAdapter.createFromResource(this,
                R.array.focus_areas, android.R.layout.simple_spinner_item);
        focusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        focusAreaSpinner.setAdapter(focusAdapter);
    }
    
    private void saveHealthProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to save your profile", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get selected gender
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        String gender = selectedGender != null ? selectedGender.getText().toString() : "";
        
        // Validate inputs
        if (fullNameEditText.getText().toString().trim().isEmpty() ||
            ageEditText.getText().toString().trim().isEmpty() ||
            heightEditText.getText().toString().trim().isEmpty() ||
            weightEditText.getText().toString().trim().isEmpty() ||
            workoutTimeEditText.getText().toString().trim().isEmpty() ||
            daysPerWeekEditText.getText().toString().trim().isEmpty() ||
            gender.isEmpty()) {
            
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create health profile data
        Map<String, Object> healthProfile = new HashMap<>();
        healthProfile.put("fullName", fullNameEditText.getText().toString().trim());
        healthProfile.put("age", Integer.parseInt(ageEditText.getText().toString().trim()));
        healthProfile.put("gender", gender);
        healthProfile.put("height", Float.parseFloat(heightEditText.getText().toString().trim()));
        healthProfile.put("weight", Float.parseFloat(weightEditText.getText().toString().trim()));
        healthProfile.put("fitnessGoal", fitnessGoalSpinner.getSelectedItem().toString());
        healthProfile.put("fitnessLevel", fitnessLevelSpinner.getSelectedItem().toString());
        healthProfile.put("workoutIntensity", intensitySpinner.getSelectedItem().toString());
        healthProfile.put("workoutTimePerDay", Integer.parseInt(workoutTimeEditText.getText().toString().trim()));
        healthProfile.put("daysPerWeek", Integer.parseInt(daysPerWeekEditText.getText().toString().trim()));
        healthProfile.put("workoutEnvironment", environmentSpinner.getSelectedItem().toString());
        healthProfile.put("focusArea", focusAreaSpinner.getSelectedItem().toString());
        
        // Add medical conditions if provided
        String medicalConditions = medicalConditionsEditText.getText().toString().trim();
        if (!medicalConditions.isEmpty()) {
            healthProfile.put("medicalConditions", medicalConditions);
        }
        
        // Calculate BMI
        float heightInMeters = Float.parseFloat(heightEditText.getText().toString()) / 100;
        float weight = Float.parseFloat(weightEditText.getText().toString());
        float bmi = weight / (heightInMeters * heightInMeters);
        healthProfile.put("bmi", bmi);
        
        // Save to Firebase
        String userId = currentUser.getUid();
        mDatabase.child("users").child(userId).child("healthProfile").setValue(healthProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HealthProfileActivity.this, 
                                    "Profile saved successfully", Toast.LENGTH_SHORT).show();
                            // Navigate to main activity
                            startActivity(new Intent(HealthProfileActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(HealthProfileActivity.this, 
                                    "Failed to save profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
} 