import dev.shibasis.publisheasy.Version
import dev.shibasis.publisheasy.plugins.readVersion

plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("dev.shibasis.publisheasy")
//    https://plugins.gradle.org/docs/publish-plugin Do this
//    id("com.gradle.plugin-publish") version "1.2.1"
    `kotlin-dsl`
    kotlin("jvm") version "2.0.21"
}

group = "dev.shibasis"

repositories {
    google()
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    maven(url = "https://jitpack.io")

}

dependencies {
    // Align Version of all Kotlin components
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Version.SDK.Kotlin}")
    implementation("com.android.tools.build:gradle:8.4.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.SDK.Kotlin}")
    implementation("org.jetbrains.kotlin.native.cocoapods:org.jetbrains.kotlin.native.cocoapods.gradle.plugin:${Version.SDK.Kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-serialization:${Version.SDK.Kotlin}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.Serialization}")
    implementation("com.google.firebase:firebase-crashlytics-gradle:${Version.Crashlytics}")
    implementation("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.15.1")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.0.21-1.0.26")

}

gradlePlugin {
    val libraryPlugin by plugins.creating {
        id = "dev.shibasis.dependeasy.library"
        implementationClass = "dev.shibasis.dependeasy.plugins.LibraryPlugin"
    }

    val applicationPlugin by plugins.creating {
        id = "dev.shibasis.dependeasy.application"
        implementationClass = "dev.shibasis.dependeasy.plugins.ApplicationPlugin"
    }

    val settingsPlugin by plugins.creating {
        id = "dev.shibasis.dependeasy.settings"
        implementationClass = "dev.shibasis.dependeasy.plugins.SettingsPlugin"
    }
}

kotlin {
//    jvmToolchain(11)
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

tasks.withType(Jar::class.java).configureEach {
    archiveFileName.set("dependeasy.jar")
}