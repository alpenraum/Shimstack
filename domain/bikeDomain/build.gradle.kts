plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
}

android {
    namespace = "com.alpenraum.shimstack.bikedomain"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.com.google.android.material)
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.hilt.navigation.compose)
}