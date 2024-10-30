plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)

    alias(libs.plugins.google.dagger.hilt)
    id("kotlin-parcelize")
    alias(libs.plugins.google.ksp)
    id("kotlin-kapt")
}

android {
    namespace = "com.alpenraum.shimstack.core.database"
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
}

dependencies {

    implementation(project(":core:model"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.com.google.dagger.hilt.android)
    ksp(libs.com.google.dagger.hilt.compiler)

    implementation(libs.com.squareup.moshi)
    ksp(libs.com.squareup.moshi.kotlin.codegen)

    implementation(project(":core:common"))
}