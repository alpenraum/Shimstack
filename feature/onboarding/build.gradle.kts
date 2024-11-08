plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)

    alias(libs.plugins.google.dagger.hilt)
    id("kotlin-parcelize")
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "com.alpenraum.shimstack.onboarding"
    compileSdk = GradleConstants.TARGET_SDK

    defaultConfig {
        minSdk = GradleConstants.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = GradleConstants.JAVA_TARGET
        targetCompatibility = GradleConstants.JAVA_TARGET
    }
    kotlinOptions {
        jvmTarget = GradleConstants.JVM_TARGET
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))

    implementation(libs.androidx.material3.android)
    implementation(libs.ui.tooling.preview.android)
    implementation(project(":feature:home"))
    implementation(project(":feature:NewBike"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.com.google.dagger.hilt.android)
    ksp(libs.com.google.dagger.hilt.compiler)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
}