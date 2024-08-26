plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)

    alias(libs.plugins.google.dagger.hilt)
    id("kotlin-parcelize")
    alias(libs.plugins.google.ksp)
}

android {
    namespace = "com.alpenraum.shimstack.home"
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

    composeOptions {
        kotlinCompilerExtensionVersion = GradleConstants.COMPOSE_COMPILER_VERSION
    }
}

dependencies {

    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))

    implementation(libs.androidx.material3.android)
    implementation(libs.ui.tooling.preview.android)
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":domain:bikeDomain"))
    implementation(project(":feature:NewBike"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.com.google.dagger.hilt.android)
    debugImplementation(libs.androidx.ui.tooling)
    ksp(libs.com.google.dagger.hilt.compiler)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.org.jetbrains.kotlinx.collections.immutable)
    implementation(libs.com.google.accompanist.pager.indicators)
    implementation(libs.com.google.accompanist.placeholder.material)
    implementation(libs.androidx.compose.material3.window.size)
}