pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

gradle.startParameter.excludedTaskNames.addAll(listOf(":buildSrc:testClasses"))

rootProject.name = "Shimstack"
include(":app")
include(":core:database")
include(":core:model")
include(":core:data")
include(":core:datastore")
include(":core:common")
include(":core:ui")
include(":feature:onboarding")
include(":core:navigation")