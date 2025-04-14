package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HealthQuestionnaireActivity extends AppCompatActivity {

    private ConstraintLayout[] questionLayouts;
    private int currentPage = 0;
    private TextView pageIndicator;
    private Button nextButton, prevButton, submitButton;
    
    // Form fields
    private EditText nameEditText, ageEditText, heightEditText, weightEditText, workoutTimeEditText;
    private RadioGroup genderRadioGroup;
    private Spinner fitnessGoalsSpinner, fitnessLevelSpinner, intensitySpinner, environmentSpinner, focusAreaSpinner, daysPerWeekSpinner;
    private CheckBox medicalConditionsCheckbox;
    
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_questionnaire);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        // Initialize UI components
        initializeUIComponents();
        setupSpinners();
        setupNavigation();
        
        // Show the first page
        updatePageVisibility();
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
        pageIndicator = findViewById(R.id.pageIndicator);
        
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
        // Setup adapters for all spinners
        ArrayAdapter<CharSequence> goalsAdapter = ArrayAdapter.createFromResource(this,
                R.array.fitness_goals, android.R.layout.simple_spinner_item);
        goalsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fitnessGoalsSpinner.setAdapter(goalsAdapter);
        
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this,
                R.array.fitness_levels, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fitnessLevelSpinner.setAdapter(levelAdapter);
        
        ArrayAdapter<CharSequence> intensityAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_intensities, android.R.layout.simple_spinner_item);
        intensityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intensitySpinner.setAdapter(intensityAdapter);
        
        ArrayAdapter<CharSequence> environmentAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_environments, android.R.layout.simple_spinner_item);
        environmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        environmentSpinner.setAdapter(environmentAdapter);
        
        ArrayAdapter<CharSequence> focusAdapter = ArrayAdapter.createFromResource(this,
                R.array.focus_areas, android.R.layout.simple_spinner_item);
        focusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        focusAreaSpinner.setAdapter(focusAdapter);
        
        // Setup days per week spinner
        ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_per_week, android.R.layout.simple_spinner_item);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysPerWeekSpinner.setAdapter(daysAdapter);
    }
    
    private void setupNavigation() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateCurrentPage()) {
                    currentPage++;
                    updatePageVisibility();
                }
            }
        });
        
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage--;
                updatePageVisibility();
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
        
        // Update page indicator
        pageIndicator.setText("Page " + (currentPage + 1) + " of " + questionLayouts.length);
        
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
        userData.put("fitnessGoal", fitnessGoalsSpinner.getSelectedItem().toString());
        userData.put("fitnessLevel", fitnessLevelSpinner.getSelectedItem().toString());
        userData.put("workoutIntensity", intensitySpinner.getSelectedItem().toString());
        userData.put("workoutTimePerDay", Integer.parseInt(workoutTimeEditText.getText().toString().trim()));
        userData.put("workoutEnvironment", environmentSpinner.getSelectedItem().toString());
        userData.put("focusArea", focusAreaSpinner.getSelectedItem().toString());
        userData.put("hasMedicalConditions", medicalConditionsCheckbox.isChecked());
        
        // Add days per week - critical for WorkoutPlanActivity
        String daysPerWeekStr = daysPerWeekSpinner.getSelectedItem().toString();
        int daysPerWeek = Integer.parseInt(daysPerWeekStr.substring(0, 1)); // Extract the number (e.g., "3 days" → 3)
        userData.put("daysPerWeek", daysPerWeek);
        
        // Calculate BMI
        float heightInMeters = Float.parseFloat(heightEditText.getText().toString().trim()) / 100;
        float weight = Float.parseFloat(weightEditText.getText().toString().trim());
        float bmi = weight / (heightInMeters * heightInMeters);
        userData.put("bmi", bmi);
        
        // Save to Firebase
        String userId = currentUser.getUid();
        mDatabase.child("users").child(userId).child("healthProfile").setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(HealthQuestionnaireActivity.this, 
                                    "Health profile saved successfully", Toast.LENGTH_SHORT).show();
                            // Navigate directly to the workout plan activity
                            Intent intent = new Intent(HealthQuestionnaireActivity.this, WorkoutPlanActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(HealthQuestionnaireActivity.this, 
                                    "Failed to save health profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
} 