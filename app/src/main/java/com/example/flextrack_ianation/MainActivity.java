package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static boolean PERSISTENCE_ENABLED = false;
    
    private TextView userEmailTextView, nameTextView, ageTextView, genderTextView, 
                     heightTextView, weightTextView, bmiTextView, bmiCategoryTextView,
                     fitnessGoalTextView, fitnessLevelTextView;
    private Button editProfileButton, signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase - only set persistence once per app lifecycle
        initializeFirebase();
        
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        initViews();
        setupButtons();
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userEmailTextView.setText("Email: " + currentUser.getEmail());
            loadUserHealthProfile(currentUser.getUid());
        } else {
            // Not signed in, go back to sign in
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
        }
    }
    
    private void initViews() {
        userEmailTextView = findViewById(R.id.user_email);
        nameTextView = findViewById(R.id.name_text);
        ageTextView = findViewById(R.id.age_text);
        genderTextView = findViewById(R.id.gender_text);
        heightTextView = findViewById(R.id.height_text);
        weightTextView = findViewById(R.id.weight_text);
        bmiTextView = findViewById(R.id.bmi_text);
        bmiCategoryTextView = findViewById(R.id.bmi_category_text);
        fitnessGoalTextView = findViewById(R.id.fitness_goal_text);
        fitnessLevelTextView = findViewById(R.id.fitness_level_text);
        
        editProfileButton = findViewById(R.id.edit_profile_button);
        signOutButton = findViewById(R.id.sign_out_button);
    }
    
    private void setupButtons() {
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HealthProfileActivity.class));
            }
        });
        
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
    
    private void loadUserHealthProfile(String userId) {
        mDatabase.child("users").child(userId).child("healthProfile")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Map<String, Object> profileData = (Map<String, Object>) dataSnapshot.getValue();
                            
                            // Display basic profile information
                            nameTextView.setText("Name: " + profileData.get("fullName"));
                            ageTextView.setText("Age: " + profileData.get("age"));
                            genderTextView.setText("Gender: " + profileData.get("gender"));
                            heightTextView.setText("Height: " + profileData.get("height") + " cm");
                            weightTextView.setText("Weight: " + profileData.get("weight") + " kg");
                            
                            // Format and display BMI with category
                            double bmi = Double.parseDouble(profileData.get("bmi").toString());
                            DecimalFormat df = new DecimalFormat("#.##");
                            bmiTextView.setText("BMI: " + df.format(bmi));
                            bmiCategoryTextView.setText("Category: " + getBmiCategory(bmi));
                            
                            // Show fitness details
                            fitnessGoalTextView.setText("Goal: " + profileData.get("fitnessGoal"));
                            fitnessLevelTextView.setText("Level: " + profileData.get("fitnessLevel"));
                        } else {
                            Toast.makeText(MainActivity.this, "No health profile found", 
                                    Toast.LENGTH_SHORT).show();
                            
                            // No profile, go to create one
                            startActivity(new Intent(MainActivity.this, HealthProfileActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Failed to load profile: " + 
                                databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private String getBmiCategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 24.9) {
            return "Normal weight";
        } else if (bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
    
    private void initializeFirebase() {
        // Ensure persistence is only initialized once
        if (!PERSISTENCE_ENABLED) {
            try {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                PERSISTENCE_ENABLED = true;
                Log.d(TAG, "Firebase Persistence enabled");
            } catch (Exception e) {
                Log.d(TAG, "Firebase Persistence already enabled or could not be enabled: " + e.getMessage());
            }
        }
    }
} 