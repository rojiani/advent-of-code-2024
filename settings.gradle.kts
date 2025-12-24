pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.autonomousapps.build-health") version "3.5.1"
    id("org.jetbrains.kotlin.jvm") version "2.3.0" apply false
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "advent-of-code-2024"