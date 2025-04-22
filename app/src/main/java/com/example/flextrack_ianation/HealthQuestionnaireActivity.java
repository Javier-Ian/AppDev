package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HealthQuestionnaireActivity extends AppCompatActivity {

    private ConstraintLayout[] questionLayouts;
    private int currentPage = 0;
    private int totalPages = 9;
    
    // UI components
    private CircularProgressIndicator progressIndicator;
    private TextView progressCountText;
    private Button nextButton, prevButton, submitButton;
    
    // Form fields
    private TextInputEditText nameEditText, ageEditText, heightEditText, weightEditText, workoutTimeEditText;
    private RadioGroup genderRadioGroup;
    private MaterialAutoCompleteTextView fitnessGoalsSpinner, fitnessLevelSpinner, intensitySpinner, 
                                        environmentSpinner, focusAreaSpinner, daysPerWeekSpinner;
    private CheckBox medicalConditionsCheckbox;
    
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide action bar to remove the package name from the top
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        
        setContentView(R.layout.activity_health_questionnaire);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        // Initialize UI components
        initializeUIComponents();
        setupSpinners();
        setupNavigation();
        
        // Set up back button
        setupBackButton();
        
        // Show the first page and update progress indicator
        updatePageVisibility();
        updateProgressIndicator();
    }
    
    private void initializeUIComponents() {
        // Initialize page layouts
        questionLayouts = new ConstraintLayout[9];
        questionLayouts[0] = findViewById(R.id.nameQuestionLayout);
        questionLayouts[1] = findViewById(R.id.ageGenderQuestionLayout);
        questionLayouts[2] = findViewById(R.id.heightWeightQuestionLayout);
        questionLayouts[3] = findViewById(R.id.fitnessGoalsQuestionLayout);
        questionLayouts[4] = findViewById(R.id.fitnessLevelQuestionLayout);
        questionLayouts[5] = findViewById(R.id.intensityQuestionLayout);
        questionLayouts[6] = findViewById(R.id.workoutTimeQuestionLayout);
        questionLayouts[7] = findViewById(R.id.environmentQuestionLayout);
        questionLayouts[8] = findViewById(R.id.medicalConditionsQuestionLayout);
        
        // Navigation buttons
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        submitButton = findViewById(R.id.submitButton);
        
        // Progress indicator
        progressIndicator = findViewById(R.id.progressIndicator);
        progressCountText = findViewById(R.id.progressCountText);
        
        // Configure progress indicator
        progressIndicator.setMax(totalPages * 100);
        
        // Form fields
        nameEditText = findViewById(R.id.nameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        workoutTimeEditText = findViewById(R.id.workoutTimeEditText);
        
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        
        fitnessGoalsSpinner = findViewById(R.id.fitnessGoalsSpinner);
        fitnessLevelSpinner = findViewById(R.id.fitnessLevelSpinner);
        intensitySpinner = findViewById(R.id.intensitySpinner);
        environmentSpinner = findViewById(R.id.environmentSpinner);
        focusAreaSpinner = findViewById(R.id.focusAreaSpinner);
        daysPerWeekSpinner = findViewById(R.id.daysPerWeekSpinner);
        
        medicalConditionsCheckbox = findViewById(R.id.medicalConditionsCheckbox);
    }
    
    private void setupSpinners() {
        // Setup adapters for all dropdown menus
        setupDropdownAdapter(fitnessGoalsSpinner, R.array.fitness_goals);
        setupDropdownAdapter(fitnessLevelSpinner, R.array.fitness_levels);
        setupDropdownAdapter(intensitySpinner, R.array.workout_intensities);
        setupDropdownAdapter(environmentSpinner, R.array.workout_environments);
        setupDropdownAdapter(focusAreaSpinner, R.array.focus_areas);
        setupDropdownAdapter(daysPerWeekSpinner, R.array.days_per_week);
    }
    
    private void setupDropdownAdapter(MaterialAutoCompleteTextView dropdown, int arrayResourceId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResourceId, android.R.layout.simple_dropdown_item_1line);
        dropdown.setAdapter(adapter);
    }
    
    private void updateProgressIndicator() {
        // Update progress indicator (0-based index to 1-based for display)
        int currentProgress = (currentPage + 1) * 100;
        progressIndicator.setProgress(currentProgress);
        progressCountText.setText((currentPage + 1) + "/" + totalPages);
    }
    
    private void setupNavigation() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCurrentPage()) {
                    currentPage++;
                    updatePageVisibility();
                    updateProgressIndicator();
                }
            }
        });
        
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage--;
                updatePageVisibility();
                updateProgressIndicator();
            }
        });
        
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCurrentPage()) {
                    saveUserData();
                }
            }
        });
    }
    
    private void updatePageVisibility() {
        // Hide all layouts first
        for (ConstraintLayout layout : questionLayouts) {
            layout.setVisibility(View.GONE);
        }
        
        // Show current layout
        questionLayouts[currentPage].setVisibility(View.VISIBLE);
        
        // Find the back button and set its visibility
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setVisibility(currentPage == 0 ? View.INVISIBLE : View.VISIBLE);
        
        // Update button visibility
        prevButton.setVisibility(currentPage > 0 ? View.VISIBLE : View.INVISIBLE);
        nextButton.setVisibility(currentPage < questionLayouts.length - 1 ? View.VISIBLE : View.GONE);
        submitButton.setVisibility(currentPage == questionLayouts.length - 1 ? View.VISIBLE : View.GONE);
    }
    
    private boolean validateCurrentPage() {
        switch (currentPage) {
            case 0:
                if (nameEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
                
            case 1:
                if (ageEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter your age", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
                
            case 2:
                if (heightEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter your height", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (weightEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
                
            case 6:
                if (workoutTimeEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter your preferred workout time", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
        }
        
        return true;
    }
    
    private void saveUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Gather all form data
        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", nameEditText.getText().toString().trim());
        userData.put("age", Integer.parseInt(ageEditText.getText().toString().trim()));
        
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGender = findViewById(selectedGenderId);
        userData.put("gender", selectedGender.getText().toString());
        
        userData.put("height", Float.parseFloat(heightEditText.getText().toString().trim()));
        userData.put("weight", Float.parseFloat(weightEditText.getText().toString().trim()));
        
        // Calculate BMI: weight (kg) / (height (m) * height (m))
        float heightInMeters = Float.parseFloat(heightEditText.getText().toString().trim()) / 100;
        float weightInKg = Float.parseFloat(weightEditText.getText().toString().trim());
        float bmi = weightInKg / (heightInMeters * heightInMeters);
        userData.put("bmi", bmi);
        
        userData.put("fitnessGoal", fitnessGoalsSpinner.getText().toString());
        userData.put("fitnessLevel", fitnessLevelSpinner.getText().toString());
        userData.put("workoutIntensity", intensitySpinner.getText().toString());
        userData.put("workoutTimePerDay", Integer.parseInt(workoutTimeEditText.getText().toString().trim()));
        userData.put("workoutEnvironment", environmentSpinner.getText().toString());
        userData.put("focusArea", focusAreaSpinner.getText().toString());
        userData.put("hasMedicalConditions", medicalConditionsCheckbox.isChecked());
        
        // Add days per week - critical for WorkoutPlanActivity
        String daysPerWeekStr = daysPerWeekSpinner.getText().toString();
        int daysPerWeek = Integer.parseInt(daysPerWeekStr.substring(0, 1)); // Extract the number (e.g., "3 days" → 3)
        userData.put("daysPerWeek", daysPerWeek);
        
        // Save data to Firebase
        mDatabase.child("users").child(currentUser.getUid()).child("healthProfile")
                .setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HealthQuestionnaireActivity.this, 
                                    "Health profile saved successfully", Toast.LENGTH_SHORT).show();
                            
                            // Navigate to workout plan
                            startActivity(new Intent(HealthQuestionnaireActivity.this, WorkoutPlanActivity.class));
                            finish();
                        } else {
                            Toast.makeText(HealthQuestionnaireActivity.this,
                                    "Failed to save health profile: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    
    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage > 0) {
                    // If not on the first page, go back to previous page
                    currentPage--;
                    updatePageVisibility();
                    updateProgressIndicator();
                } else {
                    // If on the first page, handle like a back press
                    onBackPressed();
                }
            }
        });
    }
} 