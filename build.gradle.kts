buildscript {
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.ktlint.gradle)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("com.android.library") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
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