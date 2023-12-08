plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.a12_pa"
    compileSdk = 34

    buildFeatures{
        viewBinding = true
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.example.a12_pa"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("androidx.multidex:multidex:2.0.1")
    implementation ("com.google.firebase:firebase-auth-ktx:22.3.0")

    implementation ("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation ("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.firebaseui:firebase-ui-storage:8.0.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
}