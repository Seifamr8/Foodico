plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.fa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fa"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures {
        viewBinding=true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.2")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.2")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation (libs.converter.gson)
    implementation (libs.glide)
    implementation (libs.sdp.android)
    implementation (libs.android.gif.drawable)
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.room:room-runtime:")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(libs.play.services.auth)
    implementation ("com.google.firebase:firebase-auth:23.0.0")
}