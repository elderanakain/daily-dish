import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.android.safeargs)
    alias(libs.plugins.hilt)
}

android {
    namespace = "io.krugosvet.dailydish.android"

    defaultConfig {
        versionCode = 5
        versionName = libs.versions.common.get()

        compileSdk = 34
        minSdk = 31
        targetSdk = 34

        applicationId = namespace

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs += listOf("-Xallow-result-return-type", "-Xopt-in=kotlin.RequiresOptIn")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles("proguard-rules.pro")
        }

        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }

    buildFeatures {
        dataBinding = true

        hilt {
            enableAggregatingTask = false
        }
    }

    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices {
            devices {
                maybeCreate<ManagedVirtualDevice>("ddUiTests").apply {
                    device = "Pixel 2"
                    apiLevel = 33
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}

afterEvaluate {
    val check by tasks.getting {
        val allDevicesDebugAndroidTest by tasks.getting

        dependsOn(allDevicesDebugAndroidTest)
    }
}

dependencies {
    implementation(libs.bundles.androidApp)
    kapt(libs.bundles.androidAppKapt)

    testImplementation(libs.bundles.androidUnitTest)
    androidTestImplementation(libs.bundles.androidInstrumentedTest)
}
