plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "com.alpenraum.shimstack.ui"
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.com.google.android.material)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(project(":core:common"))
    implementation(libs.ui.tooling.preview.android)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(project(":core:model"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)

    implementation(libs.com.google.accompanist.placeholder.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.com.google.android.material)
    implementation(libs.androidx.material3.android)
}