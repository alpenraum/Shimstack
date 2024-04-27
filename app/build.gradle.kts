plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.google.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.alpenraum.shimstack"
    compileSdk = GradleConstants.TARGET_SDK

    defaultConfig {
        applicationId = "com.alpenraum.shimstack"
        minSdk = GradleConstants.MIN_SDK
        targetSdk = GradleConstants.TARGET_SDK
        versionCode = 1
        versionName = "0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        compileOptions {
            sourceCompatibility = GradleConstants.JAVA_TARGET
            targetCompatibility = GradleConstants.JAVA_TARGET
        }
    }

    buildTypes {
        named("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        named("release") {
            isMinifyEnabled = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat.resources)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.org.jetbrains.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore.preferences)

    implementation(project(":core:database"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:datastore"))
    implementation(project(":feature:onboarding"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    implementation(libs.org.jetbrains.kotlinx.collections.immutable)
    implementation(libs.com.squareup.retrofit2.retrofit)
    implementation(libs.com.squareup.retrofit2.converter.gson)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)
    implementation(libs.com.google.dagger.hilt.android)
    ksp(libs.com.google.dagger.hilt.compiler)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.io.github.raamcosta.compose.destinations.core)
    ksp(libs.io.github.raamcosta.compose.destinations.ksp)
    implementation(libs.io.github.raamcosta.compose.destinations.animations.core)

    implementation(libs.com.google.accompanist.placeholder.material)
    implementation(libs.com.google.accompanist.navigation.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3.window.size)
    implementation(libs.com.google.android.material)
    implementation(libs.androidx.material3.android)

    implementation(libs.androidx.biometric.ktx)

    implementation(libs.com.google.accompanist.pager.indicators)

    implementation(libs.com.airbnb.android.lottie.compose)

    implementation(libs.androidx.ui.tooling.preview.android)

    //  ktlintRuleset(libs.io.nlopez.compose.rules.ktlint)
    testImplementation(libs.konsist)

    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)
}