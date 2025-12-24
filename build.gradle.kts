import org.codehaus.groovy.tools.shell.util.Logger.io

plugins {
    kotlin("jvm") version "2.3.0"
    id("com.autonomousapps.dependency-analysis")
    id("com.diffplug.spotless") version "8.1.0"
}

group = "com.rojiani"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

val ojAlgoVersion = "56.1.1"
val junitVersion = "6.1.0-M1"
val kotestVersion = "6.0.7"
val guavaVersion = "33.5.0-jre"

dependencies {
    implementation("org.ojalgo:ojalgo:$ojAlgoVersion")
    api("com.google.guava:guava:$guavaVersion")
    
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

kotlin {
    jvmToolchain(23)
}

tasks.test {
    useJUnitPlatform()
}

dependencyAnalysis {
    structure {
        bundle("junit-jupiter") {
            includeDependency("org.junit.jupiter:junit-jupiter")
            includeDependency("org.junit.jupiter:junit-jupiter-api")
            includeDependency("org.junit.jupiter:junit-jupiter-params")
        }
    }
}

spotless { kotlin { ktfmt("0.59").googleStyle() } }

