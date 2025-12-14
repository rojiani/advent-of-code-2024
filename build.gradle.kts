import org.codehaus.groovy.tools.shell.util.Logger.io

plugins {
    kotlin("jvm") version "2.2.21"
}

group = "com.rojiani"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotestVersion = "6.0.0.M1"

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(platform("org.junit:junit-bom:6.1.0-M1"))
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