package com.example.flextrack_ianation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * IMPORTANT: For this workaround to work, you MUST:
 * 
 * 1. Go to Firebase Console > Authentication > Sign-in method
 * 2. Enable Email/Password authentication
 * 3. Make sure you have added your app's SHA-1 fingerprint in Firebase Console:
 *    - Project Settings > Your apps > SHA certificate fingerprints
 *    - Add the fingerprint from ./gradlew signingReport
 */

/**
 * SignInActivity handles Google Sign-In authentication with Firebase.
 * The Web Client ID must be added in strings.xml as default_web_client_id.
 */
public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Configure Google Sign In with Firebase
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Check if user has completed health profile
            checkHealthProfile(currentUser);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed - log detailed information
                Log.e(TAG, "Google sign in failed: " + e.getStatusCode() + ", message: " + e.getMessage() + ", cause: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
                
                String errorMessage;
                switch (e.getStatusCode()) {
                    case 10:
                        errorMessage = "Developer Error: Check SHA-1 fingerprint and package name in Firebase console";
                        break;
                    case 12500:
                        errorMessage = "Sign in cancelled by user";
                        break;
                    case 12501:
                        errorMessage = "Sign in failed - One Tap";
                        break;
                    default:
                        errorMessage = "Error code: " + e.getStatusCode();
                }
                
                Toast.makeText(this, "Google Sign In failed: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        if (idToken == null) {
            Log.w(TAG, "Firebase auth with Google failed: ID Token is null");
            Toast.makeText(this, "Authentication failed: ID Token is null", Toast.LENGTH_LONG).show();
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // Sign in failed
                            Exception exception = task.getException();
                            Log.w(TAG, "signInWithCredential:failure", exception);
                            String errorMessage = exception != null ? exception.getMessage() : "Unknown error";
                            Toast.makeText(SignInActivity.this, "Authentication failed: " + errorMessage,
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void checkHealthProfile(FirebaseUser user) {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(user.getUid())
                .child("healthProfile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User has completed health profile, go to WorkoutPlanActivity
                            startActivity(new Intent(SignInActivity.this, WorkoutPlanActivity.class));
                        } else {
                            // User needs to complete health profile
                            startActivity(new Intent(SignInActivity.this, HealthQuestionnaireActivity.class));
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Error checking health profile: " + databaseError.getMessage());
                        Toast.makeText(SignInActivity.this, "Error checking profile status", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Check if user has completed health profile
            checkHealthProfile(user);
        }
    }
} 