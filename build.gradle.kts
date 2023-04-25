import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform") version "1.8.20"
}

allprojects {
    apply(plugin = "kotlin-multiplatform")

    group = "io.github.andreypfau"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    kotlin {
        explicitApi()
        targetHierarchy.default()

        jvm {
            withJava()
            testRuns["test"].executionTask.configure {
                useJUnitPlatform()
            }
        }
        js(IR) {
            browser()
            nodejs()
        }
        mingwX64()
        linuxX64()
        linuxArm64()
        macosArm64()
        macosX64()
        ios()
        tvos()
        watchos()

        sourceSets {
            val commonMain by getting
            val commonTest by getting {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }
    }
}
