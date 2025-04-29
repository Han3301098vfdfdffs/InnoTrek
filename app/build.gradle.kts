plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.innotrek"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.innotrek"
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
    implementation(libs.androidx.appcompat)

    // AndroidX
    implementation (libs.androidx.core)
    implementation (libs.splashscreen)

    // Compose
    implementation (libs.compose.ui)
    implementation (libs.compose.ui.unit)
    implementation (libs.compose.material3)
    implementation (libs.compose.material3.alpha)
    implementation (libs.compose.icons.extended)
    implementation (libs.navigation.compose)

    // Maps
    implementation (libs.maps.compose)
    implementation (libs.play.services.maps)
    implementation (libs.play.services.location)

    // Firebase (usando BOM)
    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.analytics)
    implementation (libs.firebase.auth)
    implementation (libs.firebase.auth.ktx)

    // Auth
    implementation (libs.play.services.auth)
    implementation (libs.credentials)
    implementation (libs.googleid)

    // Lifecycle
    implementation (libs.lifecycle.viewmodel.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}