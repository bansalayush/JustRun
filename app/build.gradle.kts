plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.scorpio.distancecalculator"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.scorpio.distancecalculator"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // Kotlin coroutines core library
    implementation(libs.kotlinx.coroutines.core) // Check for the latest version

    // Kotlin coroutines support for Android
    implementation(libs.kotlinx.coroutines.android) // Check for the latest version

    // Lifecycle-aware coroutine scopes (optional but recommended for Android)
    // For ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.ktx) // Check for the latest version
    // For Lifecycle Scopes (e.g., in Activities or Fragments)
    implementation(libs.androidx.lifecycle.runtime.ktx.v262) // Check for the latest version
    // For LiveData builder
    implementation(libs.androidx.lifecycle.livedata.ktx) // Check for the latest version

    implementation(libs.play.services.location)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)


}