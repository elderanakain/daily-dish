plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    alias(libs.plugins.android.safeargs)
}

android {
    namespace = "io.krugosvet.dailydish.android"

    defaultConfig {
        versionCode = 5
        versionName = "0.3.0"

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
    }
}

dependencies {
    implementation(libs.bundles.androidApp)
    testImplementation(libs.bundles.androidUnitTest)
    androidTestImplementation(libs.bundles.androidInstrumentedTest)
}
