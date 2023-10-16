import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.sqldelight)

    id("maven-publish")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

version = "1.1.1"
group = "io.krugosvet.dailydish"

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

    listOf(iosArm64(), iosX64(), iosSimulatorArm64())
        .forEach {
            it.binaries {
                framework {
                    baseName = "common"
                }
            }
        }

    explicitApiWarning()

    sourceSets {
        val commonMain by getting { dependencies { implementation(libs.bundles.common) } }
        val commonTest by getting { dependencies { implementation(kotlin("test")) } }
        val jvmMain by getting { dependencies { implementation(libs.bundles.jvm) } }
        val mobileMain by creating { dependsOn(commonMain) }

        val iosMain by getting {
            dependsOn(mobileMain)
            dependencies { implementation(libs.bundles.native) }
        }

        val androidMain by getting {
            dependsOn(mobileMain)
            dependencies { implementation(libs.bundles.android) }
        }
    }
}

android {
    namespace = "io.krugosvet.dailydish"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("io.krugosvet.dailydish.common.repository.db")
        }
    }
}

gradle.settingsEvaluated {
    publishing {
        repositories.addLast(repositories.getByName("GitHubPackages"))
    }
}

multiplatformSwiftPackage {
    packageName("common")
    swiftToolsVersion("5.4")
    targetPlatforms {
        iOS { v("13") }
    }
    distributionMode { local() }
    outputDirectory(File("$projectDir/../../daily-dish-package", "/"))
}
