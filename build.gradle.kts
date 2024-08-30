buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.ktlint.gradle)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.google.dagger.hilt) apply false
    alias(libs.plugins.google.ksp) apply false

    alias(libs.plugins.gradle.dependency.analysis)
    alias(libs.plugins.jetbrainsKotlinJvm) apply false
}
allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.2.1")
        verbose.set(true)
        android.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")

        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}

dependencyAnalysis {
    structure {
        ignoreKtx(true) // default is false
    }
}