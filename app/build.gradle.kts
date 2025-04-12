plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.flextrack_ianation"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.flextrack_ianation"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    
    // Add Firebase Analytics
    implementation("com.google.firebase:firebase-analytics")
    
    // Add Firebase Auth
    implementation("com.google.firebase:firebase-auth")
    
    // Add Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")
    
    // Add Firebase Firestore
    implementation("com.google.firebase:firebase-firestore")
    
    // Google Sign In (specify the exact version to avoid conflicts)
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    
    // Add Material Components
    implementation("com.google.android.material:material:1.11.0")
}